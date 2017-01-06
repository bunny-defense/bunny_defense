
package game_mechanics.tower

import runtime.TowerDefense
import runtime.GameState
import game_mechanics._
import game_mechanics.path._
import game_mechanics.bunny._
import scala.collection.mutable._
import Math._

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Tower
{
    var current_id = 0
}
/**
 * Tower entity. All special behavior is defined in the tower_type argument
 * @param owner      The owner of that tower
 * @param tower_type The type of the tower
 * @param pos0       The position of the tower
 * @param gamstate   The current gamestate
 * @see TowerType
 */
class Tower(
    val owner: Player,
    val tower_type: TowerType,
    val pos: CellPos,
    gamestate: GameState)
{
    import Tower._
    val id : Int         = current_id
    current_id += 1
    var bunnies_spawning = tower_type.bunnies_spawning

    /** Cooldown counter */
    private var cooldown = 0.0
    /** The time it takes to the tower to cool down */
    var fire_cooldown    = tower_type.fire_cooldown
    /** The base damage caused by the tower */
    var base_damage      = tower_type.base_damage
    /** The current damage the tower causes */
    var damage           = tower_type.damage
    /** The base range of the tower */
    var base_range       = tower_type.base_range
    /** The current range of the tower */
    var range            = tower_type.range
    /** The amount of gold the tower can be sold for */
    var sell_cost        = tower_type.sell_cost
    var speed_modifier   = 1.0
    var health_modifier  = 1.0

    var upgrades : Option[UpgradeTree] = tower_type.upgrades
    def allied_effect(tower : Tower) {
        tower_type.allied_effect(tower)
    }
    def enemy_effect(bunny : Bunny) {
        tower_type.enemy_effect(bunny)
    }


    // ==========================
    // ++++ FIRING MECHANICS ++++
    // ==========================

    val attack : () => Boolean = tower_type.attack_from( this, gamestate )

    // ========================
    // ++++ UPDATING LOGIC ++++
    // ========================

    /** Updates the tower
     @param dt is the delta of time that passed since the last update
     */
    def update(dt: Double): Unit = {
        if( cooldown <= 0 && attack() )
            /* Resetting the cooldown */
            cooldown = tower_type.fire_cooldown
        else
            cooldown -= dt
    }


    // =================
    // ++++ GETTERS ++++
    // =================

    def name(): String = {
        return tower_type.name
    }

    def desc(): String = {
        return tower_type.desc
    }

    def fire_speed(): Double = {
        return tower_type.fire_speed
    }

    def price() : Int = {
        return tower_type.price
    }

    def graphic(): BufferedImage = {
        return tower_type.tower_graphic
    }

    /** This function clones this tower onto newpos
     @param newpos The position on the map of the new tower
     @return The new tower
     */
    def clone_at(newpos: CellPos): Tower = {
        return new Tower(owner, tower_type, newpos, gamestate)
    }
}
