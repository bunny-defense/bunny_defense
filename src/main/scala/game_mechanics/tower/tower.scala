
package game_mechanics.tower

import runtime.Controller
import runtime.Spawner
import game_mechanics._
import game_mechanics.path._
import game_mechanics.bunny._
import scala.collection.mutable._
import Math._

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class Tower(tower_type : TowerType, pos0 : CellPos, player_id: Int) {
    /**
     * Tower superclass from which evey special tower is derived
     * @param tower_type: The type of the tower
     * @param pos0      : The position of the tower
     */
    val player         = player_id
    val pos            = pos0
    /* Cooldown counter */
    var cooldown       = 0.0
    val towertype      = tower_type
    var base_damage    = tower_type.base_damage
    var damage         = tower_type.damage
    var base_range     = tower_type.base_range
    var range          = tower_type.range
    var sell_cost      = tower_type.sell_cost
    var upgrades : Option[UpgradeTree] = tower_type.upgrades
    def allied_effect(tower : Tower) {
        tower_type.allied_effect(tower)
    }
    def enemy_effect(bunny : Bunny) {
        tower_type.enemy_effect(bunny)
    }

    // ===========================
    // ++++ UPGRADE MECHANICS ++++
    // ===========================
    tower_type match
    {
        case BaseTower  => upgrades = Some(BaseTowerUpgrades)
        case QuickTower => upgrades = Some(QuickTowerUpgrades)
        case _          => upgrades = Some(BaseTowerUpgrades)
    }

    // ==========================
    // ++++ FIRING MECHANICS ++++
    // ==========================

    val attack : () => Boolean = tower_type.attack_from( this )

    // ========================
    // ++++ UPDATING LOGIC ++++
    // ========================

    /* Updates the tower */
    def update(dt: Double): Unit = {
        if( cooldown <= 0 && attack() )
            /* Resetting the cooldown */
            cooldown = tower_type.throw_cooldown
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

    def throw_speed(): Double = {
        return tower_type.throw_speed
    }

    def price() : Int = {
        return tower_type.price
    }

    def graphic(): BufferedImage = {
        return tower_type.tower_graphic
    }

    def clone_at(newpos: CellPos): Tower = {
        return new Tower(tower_type, newpos, player)
    }
}
