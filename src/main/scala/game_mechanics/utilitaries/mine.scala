
package game_mechanics.utilitaries

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.Controller
import game_mechanics.tower.TowerType
import game_mechanics.path.Waypoint
import game_mechanics.bunny.Bunny
import game_mechanics.Purchasable
import gui.animations.SpreadAnimation


/* The class of a projectile */
class Utilitaries(origin_pos: Waypoint) extends Purchasable {
    var speed         = 1.0
    var damage        = 5.0
    var pos           = origin_pos
    val hitradius     = 0.7
    val radius        = 5
    val price         = 15

    def on_hit(target : Option[Bunny]): Unit = {
        val targets = Controller.bunnies
            .filter( bunny => pos.distance_to( bunny.pos ) < radius )
        targets.foreach( _.remove_hp( damage ) )
        for (dir <- 0 to 12) {
            Controller.animations += new SpreadAnimation(
                pos,
                radius,
                new Waypoint (Math.cos(dir.toDouble *360.0/8.0),Math.sin(dir.toDouble*360.0/8))
            )
        }
        Controller -= this
    }

    /* One step of progress */
    def update(): Unit = {
        Controller.bunnies.find( x => x.pos.distance_to(pos) < hitradius ) match
        {
            case None => ()
            case Some(bunny) => on_hit( Some(bunny) )
        }
    }
/*
    def graphic(): BufferedImage = {
        return carrot_sprite
    }
    */
}
