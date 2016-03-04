trait BunnyType
{
    val bunny_graphic =
        ImageIO.read(
        new File(getClass().
        getResource("/mobs/bunny_alt1.png").getPath()))
    val initial_hp    = 10.0
    val shield        = 1.0
    val speed         = 2.0
    val reward        = 10
    val damage        = 1
}
