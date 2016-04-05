
package game_mechanics.bunny

object ShieldBunny extends BunnyType
{
    override val effect_range = 2
    val shild_increase = 1.5
    override def allied_effect(bunny: Bunny): Unit = {
        bunny.shield *= 1.5
    }
}
