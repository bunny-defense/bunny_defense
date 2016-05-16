
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

abstract class GameState extends State with Publisher
{
    /**
     * The main controller.
     * It manages the main loop, the graphics, everything
     */
    val bunnies      = new ListBuffer[Bunny]
    val projectiles  = new ListBuffer[Projectile]
    val towers       = new ListBuffer[Tower]
    val animations   = new ListBuffer[Animatable]
    val utilitaries  = new ListBuffer[Utilitary]
    val players      = new ListBuffer[Player]
    var wave_counter = 1
    val framerate    = 1.0/60.0 * 1000
    var started      = false
    var dt: Double   = 0.0
    var raining      = false
    var rng          = new Random

    /* GUI */
    val gui : TDComponent

    /* ==================== CALLBACKS ==================== */

    def upgrade_tower(): Unit = {
       this.selected_cell.get.upgrades match
       {
           case None            => {}
           case Some(upgrade)   => {
               if (player.remove_gold(upgrade.cost)) {
                   upgrade.effect(selected_cell.get)
                   selected_cell.get.sell_cost += ((0.8 * upgrade.cost).toInt)
               }
               else {
                   println("Not enough money, you noob !")
               }
           }
       }
    }

    /* ==================== MAIN LOOP ==================== */
    /* Update the game for dt time */
    def step(dt: Double): Unit = {
        /* Update projectiles */
        projectiles.foreach( _.update(dt, this) )

        /* Update towers */
        /* Reinitialize the damage and range of all towers */
        towers.foreach (x => x.damage = x.base_damage)
        towers.foreach (x => x.range = x.base_range)
        /* Apply all tower effects to towers*/
        towers.foreach( tower =>
            towers.foreach( x => if( (x.pos - tower.pos).norm <= tower.range )
                { tower.allied_effect(x)})
        )
        towers.foreach( _.update(dt, this) )
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
                bunnies.foreach( x => if( (x.pos - bunny.pos).norm <= bunny.effect_range )
                { bunny.allied_effect(x) })
        )
        bunnies.foreach( _.update(dt, this ) )
        /* MOVE THIS TO ServerGameState */
        /* Random chance of rain */
        /*
        if( rng.nextDouble < (dt / 200) && !raining )
        {
            raining = true
            val anim = if(rng.nextDouble < 0.5)
                new ThunderstormAnimation( 30 + rng.nextDouble * 120 )
            else
                new RainAnimation( 30 + rng.nextDouble * 120 )
            anim and_then { () => this.raining = false }
            this += anim
        }
        if ( TowerDefense.keymap(Key.Escape)) {
            selected_tower = None
            selected_cell  = None
        }
        */
    }

    override def update(dt: Double) : Unit = {
        step(dt)
    }

    override def render(g: Graphics2D) : Unit = {
        gui.draw(g)
    }

    override def on_event(event: Event) : Unit = {
        gui.on_event(event)
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
        map_panel.map += tower
    }

    def -=(tower: Tower): Unit = {
        towers -= tower
        tower.towertype.amount -= 1
        map_panel.map -= tower
    }

    /* ANIMATIONS */

    def +=(animation: Animatable): Unit = {
        animations += animation
    }

    def -=(animation: Animatable): Unit = {
        animations -= animation
    }


    def +=(utilitary: Utilitary): Unit = {
        utilitaries += utilitary
    }

    def -=(utilitary: Utilitary): Unit = {
        utilitaries -= utilitary
    }
}
