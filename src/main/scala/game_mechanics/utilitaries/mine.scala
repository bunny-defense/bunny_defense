
package game_mechanics.utilitaries

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.TowerDefense
import runtime.GameState._
import game_mechanics.tower.TowerType
import game_mechanics.path.Waypoint
import game_mechanics.bunny.Bunny
import game_mechanics.Purchasable
import gui.animations.SpreadAnimation


object Utilitary {
    var id = 0
    var player  = 1
}

/* The class of a utilitary purchasable item */
class Utilitary(origin_pos: Waypoint) extends Purchasable {
    import Utilitary._
    var speed         = 1.0
    var damage        = 5.0
    var pos           = origin_pos
    val hitradius     = 0.7
    val radius        = 5
    val price         = 15
    val id            = Utilitary.id
    val player        = Utilitary.player

    def on_hit(target : Option[Bunny], gamestate : GameState): Unit = {
        val targets = gamestate.bunnies
            .filter( bunny => pos.distance_to( bunny.pos ) < radius )
        targets.foreach( _.remove_hp( damage ) )
        for (dir <- 0 to 12) {
            gamestate.animations += new SpreadAnimation(
                pos,
                radius,
                new Waypoint (Math.cos(dir.toDouble *360.0/8.0),Math.sin(dir.toDouble*360.0/8))
            )
        }
        gamestate -= this
    }

    /* One step of progress */
    def update(gamestate : GameState): Unit = {
        gamestate.bunnies.find( x => x.pos.distance_to(pos) < hitradius ) match
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
