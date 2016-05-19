
package game_mechanics

import runtime.TowerDefense
import runtime.GameState
import game_mechanics.path.Waypoint
import game_mechanics.tower.TowerType
import game_mechanics.bunny.Bunny
import gui.animations._
import Math._

/* This projectile causes splash damage */
class SplashProjectile(
    owner: Player,
    targetpos: Waypoint,
    origin: Waypoint,
    firing_tower: TowerType,
    gamestate: GameState)
extends Projectile(
    owner,
    targetpos,
    origin,
    firing_tower,
    gamestate)
{
    val radius = 5
    damage = 3
    override def on_hit(target : Option[Bunny]): Unit = {
        val targets = gamestate.bunnies
            .filter( bunny => pos.distance_to( bunny.pos ) < radius )
        targets.foreach( _.remove_hp( damage, owner ) )
        gamestate.splash_projectile_hit_strategy(this)
        gamestate -= this
    }
}
