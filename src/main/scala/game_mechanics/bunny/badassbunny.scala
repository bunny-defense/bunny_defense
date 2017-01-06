package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO

import game_mechanics.path._
import game_mechanics.Player
import game_mechanics.JPS

import runtime.TowerDefense
import runtime.GameState

/* A badass bunny, really strong, will become the "default" mob in late game */
case class BadassBunny(
    _owner: Player,
    val bunny_id: Int,
    _path: Progress,
    _health_modifier: Double = 1.0)
extends Bunny(
    _owner,
    _path,
    30.0,
    _health_modifier)
{
    override val id            = bunny_id
    override val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/badassbunny.png").getPath()))
    pos = path.path.head
    override val base_shield   = 2.0
    shield                     = 2.0
    base_speed                 = 1.5
    speed                      = 1.5
    override def reward        = atan_variation(15,3,15)
}
