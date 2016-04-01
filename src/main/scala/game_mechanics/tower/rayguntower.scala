
package game_mechanics.tower

import runtime.Controller

import gui.RaygunAnimation

import game_mechanics.Bunny

object RaygunTower extends TowerType
{
    override val throw_cooldown = RaygunAnimation.duration

    override def fire_from(tower: Tower)(bunny: Bunny): Unit = {
        Controller += new RaygunAnimation( tower.pos )
    }
}
