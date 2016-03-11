
package runtime

import Math.abs

import swing.event._
import swing._

import collection.mutable.{ListBuffer,Queue}

import runtime._
import game_mechanics._
import game_mechanics.path._
import gui._


case object SelectedCell extends Event
case object NoSelectedCell extends Event
case object FastForwOn extends Event
case object FastForwOff extends Event

object Controller extends Publisher
{
    val bunnies      = new ListBuffer[Bunny]
    val projectiles  = new ListBuffer[Throw]
    val towers       = new ListBuffer[Tower]
    val animations   = new ListBuffer[Animatable]
    var wave_counter = 0
    val framerate    = 1.0/30.0 * 1000
    var started      = false
    var dt: Double   = 0.0
    var acceleration = 1
    /* The tower type selected for construction */
    var selected_tower          : Option[TowerType] = None
    /* The tower currently selected */
    private var _selected_cell  : Option[Tower]     = None

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
        if( selected_tower != None &&
            !TowerDefense.map_panel.map.obstructed(x,y) )
        {
            if( Player.remove_gold(selected_tower.get.buy_cost) )
                Controller += new Tower( selected_tower.get, new CellPos(x,y) )
            else
                println("Not enough money! Current money = " + Player.gold.toString)
        }
        // Selecting a placed tower
        else if ( selected_tower == None )
        {
            val position = new CellPos(x,y)
            selected_cell = towers.find( _.pos == position )
        }
        // Building multiple towers
        if ( !TowerDefense.keymap(Key.Shift) ) {
            selected_tower = None
        }
    }

    /* Triggered when a button from the build menu is clicked */
    def on_build_button( button: BuyButton ): Unit = {
        selected_tower = button.tower
        selected_cell = None
    }

    /* Triggered when the play button is clicked */
    def on_play_button(): Unit = {
        wave_counter += 1
        var spawnschedule = new Spawner(wave_counter).create
        SpawnScheduler.set_schedule(spawnschedule)
        val anim = new WaveAnimation(wave_counter)
        anim and_then SpawnScheduler.start
        this += anim
    }

    /* Triggered when the fast forward button is clicked */
    def on_fastforward_button(): Unit = {
        if (acceleration == 5) {
            acceleration = 1
            publish( FastForwOff )
        }
        else if (acceleration == 1) {
            acceleration = 5
            publish( FastForwOn )
        }
    }

    /* ==================== MAIN LOOP ==================== */

    /* Update the game for dt time */
    def update(dt: Double): Unit = {
        /* Update animations */
        animations.foreach( _.update(dt) )
        /* Update projectiles */
        projectiles.foreach( _.update(dt) )
        /* Update towers */
        towers.foreach( _.update(dt) )
        /* Update bunnies */
        bunnies.foreach( _.update(dt) )
        /* Spawn in new bunnies */
        SpawnScheduler.update(dt)
    }

    /* Run the game */
    def run(): Unit = {
        while( true )
        {
            val start = System.currentTimeMillis
          /* Update */
            for ( i <- 1 to acceleration ) {
              update(dt)
            }
            if ( TowerDefense.keymap(Key.Escape)) {
                selected_tower = None
                selected_cell  = None
            }

            /* Render */
            TowerDefense.map_panel.repaint()
            TowerDefense.info_panel.repaint()
            TowerDefense.tower_panel.thepanel.repaint()

            /* Delta time and step time computing */
            val miliseconds = framerate.toInt - (System.currentTimeMillis - start)
            if( miliseconds < 0 )
                println( "Can't keep up !" )
            Thread.sleep(Math.abs(miliseconds)) // So that the cpu doesn't max out for nothing
            dt = (System.currentTimeMillis - start).toDouble / 1000

            /* If player loses all health */
            if (Player.hp <= 0) {
                Dialog.showMessage( TowerDefense.map_panel, "Game Over" )
                return
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

    def +=(projectile: Throw): Unit = {
        projectiles += projectile
    }

    def -=(projectile: Throw): Unit = {
        projectiles -= projectile
    }

    /* TOWERS */

    def +=(tower: Tower): Unit = {
        towers += tower
        TowerDefense.map_panel.map += tower
    }

    def -=(tower: Tower): Unit = {
        towers -= tower
        TowerDefense.map_panel.map -= tower
    }

    /* ANIMATIONS */

    def +=(animation: Animatable): Unit = {
        animations += animation
    }

    def -=(animation: Animatable): Unit = {
        animations -= animation
    }
}
