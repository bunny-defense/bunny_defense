package game_mechanics.bunny

import runtime.GameState
import game_mechanics.Player
import game_mechanics.path._

object TowerFactory
{
    var tower_id        = 0

    val BASE_TOWER      = 0
    val QUICK_TOWER     = 1
    val HEAVY_TOWER     = 2
    val SCARECROW       = 3
    val ROBERTO         = 4
    val SPLASHTOWER     = 5
    val SUPP_BUFF_TOWER = 6
    val SUPP_SLOW_TOWER = 7
    val WALL            = 8

    def create(tower_type: Int,
        owner: Player,
        pos: CellPos,
        gamestate: GameState): Bunny = {
        tower_id += 1
        tower_type match {
            case BASE_TOWER      =>
                new BaseTower(owner, tower_id, pos, arrival, gamestate)
            case QUICK_TOWER     =>
                new QuickTower(owner, tower_id, pos, arrival, gamestate)
            case HEAVY_TOWER     =>
                new HeavyTower(owner, tower_id, pos, arrival, gamestate)
            case SCARECROW       =>
                new Scarecrow(owner, tower_id, pos, arrival, gamestate)
            case ROBERTO         =>
                new Roberto(owner, tower_id, pos, arrival, gamestate)
            case SPLASHTOWER     =>
                new SplashTower(owner, tower_id, pos, arrival, gamestate)
            case SUPP_BUFF_TOWER =>
                new SuppBuffTower(owner, tower_id, pos, arrival, gamestate)
            case SUPP_SLOW_TOWER =>
                new SuppSlowTower(owner, tower_id, pos, arrival, gamestate)
            case WALL            =>
                new Wall(owner, tower_id, pos, arrival, gamestate)
        }
    }
}
