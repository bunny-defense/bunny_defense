package game_mechanics.bunny

import runtime.GameState
import game_mechanics.Player
import game_mechanics.path._

object BunnyFactory
{
    var bunny_id        = 0
    val NORMAL_BUNNY    = 0
    val HEAVY_BUNNY     = 1
    val HARE_           = 2
    val OTTER_          = 3
    val GOLDEN_BUNNY    = 4
    val BADASS_BUNNY    = 5
    val SPECOP_BUNNY    = 6
    val FLYING_SQUIRREL = 7
    val SHIELD_BUNNY    = 8

    def create(bunny_type : Int,
        owner: Player,
        path: Progress,
        gamestate: GameState): Bunny = {
        bunny_id += 1
        bunny_type match {
            case NORMAL_BUNNY    =>
                new NormalBunny(owner, bunny_id, path, gamestate)
            case HEAVY_BUNNY     =>
                new HeavyBunny(owner, bunny_id, path, gamestate)
            case HARE_           =>
                new Hare(owner, bunny_id, path, gamestate)
            case OTTER_          =>
                new Otter(owner, bunny_id, path, gamestate)
            case GOLDEN_BUNNY    =>
                new GoldenBunny(owner, bunny_id, path, gamestate)
            case BADASS_BUNNY    =>
                new BadassBunny(owner, bunny_id, path, gamestate)
            case SPECOP_BUNNY    =>
                new SpecOpBunny(owner, bunny_id, path, gamestate)
            case FLYING_SQUIRREL =>
                new FlyingSquirrel(owner, bunny_id, path, gamestate)
            case SHIELD_BUNNY    =>
                new ShieldBunny(owner, bunny_id, path, gamestate)
        }
    }
}
