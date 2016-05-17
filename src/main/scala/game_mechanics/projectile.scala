// What..... is a throw ?
// A bullet... No! A carrot! Ouaahhhhhhh

package game_mechanics

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.TowerDefense
import runtime.GameState
import game_mechanics.tower.TowerType
import game_mechanics.path.Waypoint
import game_mechanics.bunny.Bunny
import gui.animations.DamageAnimation

/* The class of a projectile */
class Projectile(
    owner: Player,
    targetpos: Waypoint, origin: Waypoint, firing_tower: TowerType,
    gamestate: GameState)
{
    var speed    = 1.0
    var damage   = 5.0
    var pos      = origin
    var hit      = false
    val carrot_sprite = firing_tower.throw_graphic
    val direction     = (targetpos - origin).normalize()
    val target_pos    = targetpos + direction * 2
    val hitradius     = 0.7

    /* Update of the position of the throw */
    def move(dt : Double): Unit = {
        val next_pos = pos + direction * (speed * dt)
        hit = ((target_pos - pos) & (target_pos - next_pos)) < 0.0
        pos = next_pos
    }

    /* Executed when the projectile hits (the ground or an ennemy) */
    def on_hit(hit_target : Option[Bunny]): Unit = {
        hit_target match {
            case None => ()
            case Some(bunny) => bunny.remove_hp(damage, owner)
        }
        gamestate -= this
    }

    /* One step of progress */
    def update(dt: Double): Unit = {
        move(dt)
        gamestate.bunnies.find( x => x.pos.distance_to(pos) < hitradius ) match
        {
            case None => ()
            case Some(bunny) => on_hit(Some(bunny))
        }
        if( hit ) { on_hit( None ) }
    }

    def graphic(): BufferedImage = {
        return carrot_sprite
    }
}
