
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO


/* Rare golden bunny worth a lot of money */
class GoldenBunny extends Bunny
{
    override val bunny_graphic =
        ImageIO.read(new File(
            getClass().getResource("/mobs/goldenbunny_alt1.png").getPath()))
    override val initial_hp    = 20.0
    override val base_speed    = 8.0
    speed                      = 8.0
    override def reward        = atan_variation(500,500,1) /* Constant at 500 */
    override val damage        = 0
}
