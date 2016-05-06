package game_mechanics.bunny

object BunnyFactory
{
    val NORMAL_BUNNY    = 0
    val HEAVY_BUNNY     = 1
    val HARE_           = 2
    val OTTER_          = 3
    val GOLDEN_BUNNY    = 4
    val BADASS_BUNNY    = 5
    val SPECOP_BUNNY    = 6
    val FLYING_SQUIRREL = 7
    val SHIELD_BUNNY    = 8

    def create(bunny_type : Int, player_id: Int): Bunny = {
        bunny_type match {
            case NORMAL_BUNNY    =>
                new NormalBunny(player_id)
            case HEAVY_BUNNY     =>
                new HeavyBunny(player_id)
            case HARE_           =>
                new Hare(player_id)
            case OTTER_          =>
                new Otter(player_id)
            case GOLDEN_BUNNY    =>
                new GoldenBunny(player_id)
            case BADASS_BUNNY    =>
                new BadassBunny(player_id)
            case SPECOP_BUNNY    =>
                new SpecOpBunny(player_id)
            case FLYING_SQUIRREL =>
                new FlyingSquirrel(player_id)
            case SHIELD_BUNNY    =>
                new ShieldBunny(player_id)
        }
    }
}
