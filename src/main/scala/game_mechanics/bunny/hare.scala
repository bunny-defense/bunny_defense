
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO


/* Fast "Bunny" */
object Hare extends BunnyType
{
    override val bunny_graphic =
        ImageIO.read(new File(
            getClass().getResource("/mobs/hare_alt1.png").getPath()))
    override val initial_hp  = 5.0
    override val base_shield = 0.0
    shield                   = 0.0
    override val base_speed  = 4.0
    speed                    = 4.0
}
