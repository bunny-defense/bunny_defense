package game_mechanics.upgrades
import game_mechanics._
import game_mechanics.tower._
import Math._

class Upgrades(next_upgrades: List[SingleUpgrade]) {
    var upgrades = next_upgrades
    def apply_upgrade (chosen_upgrade : SingleUpgrade, tower : Tower) = {
        this.upgrades = this.upgrades diff List(chosen_upgrade) /* Fancy way of deleting chosen_upgrade from this.upgrades */
        this.upgrades = this.upgrades ::: chosen_upgrade.children
        chosen_upgrade.effect(tower)
    }
    def isEmpty() : Boolean = {
        return(this.upgrades.isEmpty)
    }
    def nth_upgrade(n: Int) : SingleUpgrade = {
        return(this.upgrades(n))
    }
}
