
package game_mechanics.bunny

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ShieldBunny extends Bunny
{
    override val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/shield_bunny.png").getPath()))
    override val effect_range = 2
    val shield_increase = 1.5
    override def allied_effect(bunny: Bunny): Unit = {
        bunny.shield *= 1.5
    }
}
