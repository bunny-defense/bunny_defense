
package runtime

import Math.abs

import swing.event._
import swing._

import collection.mutable.{ListBuffer,Queue}
import util.Random
import collection.parallel._

import runtime._
import game_mechanics._
import game_mechanics.path._
import game_mechanics.tower._
import game_mechanics.bunny._
import game_mechanics.utilitaries._
import gui._
import gui.animations._
import strategy._
import tcp._


case object SelectedCell extends Event
case object NoSelectedCell extends Event
case object FastForwOn extends Event
case object FastForwOff extends Event


class gamestate(given_strategy : Strategy) extends Publisher with Reactor
{
    /**
     * The main gamestate.
     * It manages the main loop, the graphics, everything
     */
    val strategy     = given_strategy
    val bunnies      = new ListBuffer[Bunny]
    val projectiles  = new ListBuffer[Projectile]
    val towers       = new ListBuffer[Tower]
    val animations   = new ListBuffer[Animatable]
    val updatables   = new ListBuffer[Updatable]
    val utilitaries  = new ListBuffer[Utilitaries]
    var wave_counter = 1
    val framerate    = 1.0/60.0 * 1000
    var started      = false
    var timer        = 5.0
    var timer_init   = 5.0
    var dt: Double   = 0.0
    var acceleration = 2
    var is_accelerated = false
    var raining      = false
    var domain       = "localhost"
    /* The tower type selected for construction */
    var selected_tower          : Option[TowerType]     = None
    /* The tower currently selected */
    private var _selected_cell  : Option[Tower]         = None
    var rng          = new Random


    listenTo(SpawnScheduler)

    reactions += {
        case WaveEnded =>
            set_accelerated( false )
    }

    /* selected_cell GETTER */
    def selected_cell_=(tower: Option[Tower]): Unit =
    {
        if (tower != None)
            publish(SelectedCell)
        else
            publish(NoSelectedCell)
        _selected_cell = tower
    }

    /* selected_cell SETTER */
    def selected_cell =  _selected_cell

    /* ==================== CALLBACKS ==================== */

    /* Triggered when a map cell is clicked */
    def on_cell_clicked( x:Int, y:Int ): Unit = {
        // Placing a new tower
        val pos = new CellPos(x,y)
        if( selected_tower != None &&
            TowerDefense.map_panel.map.valid(pos) )
        {
            if( Player.remove_gold(selected_tower.get.price) ) {
                ClientThread.add((selected_tower.tower_type.getClass.getSimpleName, (x,y), Player.id))
                /* Updates the paths of living bunnies, so they won't conflict
                 * with the new tower. Uses multi-threading to be more efficient */
                var bun_update = bunnies.filter( t => t.path.path.exists(
                    u => u.x == pos.x && u.y == pos.y)).par
                bun_update.tasksupport = new ForkJoinTaskSupport(
                    new scala.concurrent.forkjoin.ForkJoinPool(8))
                val centering = new Waypoint( 0.5, 0.5 )
                for (bunny <- bun_update) {
                    bunny.path.path = new JPS(
                        (bunny.pos + centering).toInt,
                        bunny.bunnyend).run().get
                    bunny.path.reset
                    bunny.bunnyend = bunny.path.last.toInt
                }
            }
            else
                println("Not enough money! Current money = " + Player.gold.toString)
        }
        // Selecting a placed tower
        else if ( selected_tower == None )
        {
            selected_cell = towers.find( _.pos == pos )
        }
        // Building multiple towers
        if ( !TowerDefense.keymap(Key.Shift) ) {
            selected_tower = None
        }
    }


    /* Triggered when the play button is clicked */
    def on_play_button(button : Button): Unit = {
        button.enabled = false
        val spawner = new Spawner(wave_counter)
        val spawnschedule = spawner.create()
        SpawnScheduler.set_schedule(spawnschedule)
        val anim = new WaveAnimation(wave_counter)
        anim and_then SpawnScheduler.start
        if( spawner.has_boss )
        {
            val splash_anim = new SplashAnimation()
            splash_anim and_then { () => this += anim }
            this += splash_anim
        } else {
            this += anim
        }
    }

    def set_accelerated(value: Boolean) : Unit = {
        is_accelerated = value
        acceleration = if( is_accelerated ) 5 else 2
    }

    /* Triggered when the fast forward button is clicked */
    def on_fastforward_button(): Unit = {
        set_accelerated( !is_accelerated )
    }

    def upgrade_tower(): Unit = {
       TowerDefense.gamestate.selected_cell.get.upgrades match
       {
           case None            => {}
           case Some(upgrade)   => {
               if (Player.remove_gold(upgrade.cost)) {
                   ClientThread.add(
                       ("Upgraded",
                        (selected_cell.get.x, selected_cel.get.y),
                        upgrade,
                        player.id
                       )
                    )
                   selected_cell.get.sell_cost += ((0.8 * upgrade.cost).toInt)
               }
               else {
                   println("Not enough money, you noob !")
               }
           }
       }
    }


    /* ==================== MAIN LOOP ==================== */

    def scroll(dt: Double): Unit = {
        val scroll_speed = 128
        /* Handling input */
        if( TowerDefense.keymap(Key.J) )
        {
            val scroll_distance = Math.min(
                TowerDefense.map_panel.rows * MapPanel.cellsize -
                    TowerDefense.map_panel.size.height,
                TowerDefense.map_panel.viewpos.y + dt * scroll_speed )
            TowerDefense.map_panel.viewpos =
                new Waypoint(0, scroll_distance)
        }
        if( TowerDefense.keymap(Key.K) )
        {
            val scroll_distance = Math.max( 0,
                TowerDefense.map_panel.viewpos.y - dt * scroll_speed )
            TowerDefense.map_panel.viewpos =
                new Waypoint(0, scroll_distance)
        }
        if( TowerDefense.keymap(Key.H) )
        {
            val scroll_distance = Math.max( 0,
                TowerDefense.map_panel.viewpos.x - dt * scroll_speed )
            TowerDefense.map_panel.viewpos =
                new Waypoint(scroll_distance, 0)
        }
        if( TowerDefense.keymap(Key.L) )
        {
            val scroll_distance = Math.min(
                TowerDefense.map_panel.cols * MapPanel.cellsize -
                    TowerDefense.map_panel.size.width,
                TowerDefense.map_panel.viewpos.x + dt * scroll_speed )
            TowerDefense.map_panel.viewpos =
                new Waypoint(scroll_distance, 0)
        }
    }

    /* Update the game for dt time */
    def update(dt: Double): Unit = {
        this.strategy.displaystrategy.scroll(dt)
        /* Update animations */
        animations.foreach( _.update(dt) )
        /* Update misc items */
        updatables.foreach( _.update(dt) )
        /* Updates utilitaries */
        utilitaries.foreach(_.update())
        /* Update projectiles */
        projectiles.foreach( _.update(dt) )
        /* Update towers */

        /* Reinitialize the damage and range of all towers */
        towers.foreach (x => x.damage = x.base_damage)
        towers.foreach (x => x.range = x.base_range)
        /* Apply all tower effects to towers*/
        towers.foreach( tower =>
            towers.foreach( x => if( (x.pos - tower.pos).norm <= tower.range )
                { tower.allied_effect(x)})
        )
        towers.foreach( _.update(dt) )
        /* Update bunnies */

         /* Reinitialize the speed and shield of all bunnies */
        bunnies.foreach (x => x.speed = x.base_speed)
        bunnies.foreach (x => x.shield = x.base_shield)
        /* Apply all tower effects to bunnies*/
        towers.foreach( tower =>
            bunnies.foreach( x => if( (x.pos - tower.pos).norm <= tower.range )
                { tower.enemy_effect(x) })
        )
        bunnies.foreach( bunny =>
                bunnies.foreach( x => if(
                    (x.pos - bunny.pos).norm <= bunny.effect_range &&
                     x.player == bunny.player
                 )
                { bunny.allied_effect(x) })
        )
        bunnies.foreach( _.update(dt) )
        /* Spawn in new bunnies */
        SpawnScheduler.update(dt)
        /* Random chance of rain */
       this.strategy.displaystrategy.rain(dt)
    }

    /* Run the game */
    def run(): Unit = {
        while( true )
        {
            val start = System.currentTimeMillis
            this.strategy.updatestrategy.update_timer(dt)
            /* Update */
            for ( i <- 1 to acceleration ) {
              update(dt)
            }
            if ( TowerDefense.keymap(Key.Escape)) {
                selected_tower = None
                selected_cell  = None
            }

            /* Render */
            this.strategy.displaystrategy.paint

            /* Delta time and step time computing */
            val miliseconds = framerate.toInt - (System.currentTimeMillis - start)
            if( miliseconds < 0 )
                println( "Can't keep up !" )
            Thread.sleep(Math.abs(miliseconds)) // So that the cpu doesn't max out for nothing
            dt = (System.currentTimeMillis - start).toDouble / 1000

            /* If player loses all health */
            if (Player.hp <= 0) {
                this.strategy.displaystrategy.next
            }
        }
    }

    /* ==================== COLLECTION-LIKE BEHAVIOR ==================== */

    /* BUNNIES */

    def +=(bunny: Bunny): Unit = {
        bunnies += bunny
    }

    def -=(bunny: Bunny): Unit = {
        bunnies -= bunny
    }

    /* PROJECTILES */

    def +=(projectile: Projectile): Unit = {
        projectiles += projectile
    }

    def -=(projectile: Projectile): Unit = {
        projectiles -= projectile
    }

    /* TOWERS */

    def +=(tower: Tower): Unit = {
        /*
        towers.foreach( x => if( (x.pos - tower.pos).norm <= tower.range ) { tower.allied_effect(x)})
        bunnies.foreach( x => if((x.pos - tower.pos).norm <= tower.range) {tower.enemy_effect(x)})
         */
        towers += tower
        tower.towertype.amount += 1
        TowerDefense.map_panel.map += tower
    }

    def -=(tower: Tower): Unit = {
        towers -= tower
        tower.towertype.amount -= 1
        TowerDefense.map_panel.map -= tower
    }

    /* ANIMATIONS */

    def +=(animation: Animatable): Unit = {
        animations += animation
    }

    def -=(animation: Animatable): Unit = {
        animations -= animation
    }

    /* MISC ITEMS */
    def +=(updatable: Updatable): Unit = {
        updatables += updatable
    }

    def -=(updatable: Updatable): Unit = {
        updatables -= updatable
    }

    /* UTILITARIES ITEMS */
    def +=(utilitary: Utilitaries): Unit = {
        utilitaries += utilitary
    }

    def -=(utilitary: Utilitaries): Unit = {
        utilitaries -= utilitary
    }
}
