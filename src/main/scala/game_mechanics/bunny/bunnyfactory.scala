package game_mechanics.bunny

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

    def create(bunny_type : Int, player_id: Int, pos: CellPos, arrival: CellPos): Bunny = {
        bunny_id += 1
        bunny_type match {
            case NORMAL_BUNNY    =>
                new NormalBunny(player_id, bunny_id, pos, arrival)
            case HEAVY_BUNNY     =>
                new HeavyBunny(player_id, bunny_id, pos, arrival)
            case HARE_           =>
                new Hare(player_id, bunny_id, pos, arrival)
            case OTTER_          =>
                new Otter(player_id, bunny_id, pos, arrival)
            case GOLDEN_BUNNY    =>
                new GoldenBunny(player_id, bunny_id, pos, arrival)
            case BADASS_BUNNY    =>
                new BadassBunny(player_id, bunny_id, pos, arrival)
            case SPECOP_BUNNY    =>
                new SpecOpBunny(player_id, bunny_id, pos, arrival)
            case FLYING_SQUIRREL =>
                new FlyingSquirrel(player_id, bunny_id, pos, arrival)
            case SHIELD_BUNNY    =>
                new ShieldBunny(player_id, bunny_id, pos, arrival)
        }
    }
}
