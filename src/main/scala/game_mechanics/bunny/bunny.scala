
package game_mechanics.bunny

import java.awt.image.BufferedImage

import game_mechanics.path._
import gui.MapPanel


class Bunny(bunny_type_init: BunnyType,path0: Path) {
    /**
     * Bunny superclass from which every ennemy is derived.
     * @param bunny_type: The type of the bunny
     * @param path0: The initial path of the bunny
     */
    var bunny_type      = bunny_type_init
    var hp              = bunny_type.initial_hp
    var pos : Waypoint  = path0.waypoints(0)
    var path            = new Progress(path0)
    val base_shield     = bunny_type.base_shield
    var shield          = bunny_type.shield
    val base_speed      = bunny_type.base_speed
    var speed           = bunny_type.speed
    val spread          = (Waypoint.random() * 2 - new Waypoint(1,1)) / MapPanel.cellsize * 2

    def allied_effect(bunny: Bunny): Unit = {
        bunny_type.allied_effect(bunny)
    }

    def reward          = bunny_type.reward

    /* Prototype design pattern */
    def copy(): Bunny = {
        return new Bunny( bunny_type, path.path )
    }

    def remove_hp(dmg: Double): Unit = {
        this.hp -= dmg * (1.0 - this.shield/10.0)
    }

    def alive() : Boolean = {
        hp > 0
    }

    /* Moves the bunny along the path */
    def move(dt: Double): Unit = {
        path.move( dt * this.speed )
        pos = path.get_position + spread
    }

    def update(dt: Double): Unit = {
        /** Updates the bunny thanks to the update method from BunnyType */
        bunny_type.update(this,dt)
    }

    // =================
    // ++++ GETTERS ++++
    // =================
    def initial_hp(): Double = {
        return bunny_type.initial_hp
    }

    def graphic(): BufferedImage = {
        return bunny_type.bunny_graphic
    }

    def effect_range(): Double = {
        return bunny_type.effect_range
    }
}


