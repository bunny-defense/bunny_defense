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

    def create(bunny_type : Int): Bunny = {
        bunny_type match {
            case NORMAL_BUNNY    =>
                new NormalBunny()
            case HEAVY_BUNNY     =>
                new HeavyBunny()
            case HARE_           =>
                new Hare()
            case OTTER_          =>
                new Otter()
            case GOLDEN_BUNNY    =>
                new GoldenBunny()
            case BADASS_BUNNY    =>
                new BadassBunny()
            case SPECOP_BUNNY    =>
                new SpecOpBunny()
            case FLYING_SQUIRREL =>
                new FlyingSquirrel()
            case SHIELD_BUNNY    =>
                new ShieldBunny()
        }
    }
}
