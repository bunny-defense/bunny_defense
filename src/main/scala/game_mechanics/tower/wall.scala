
package game_mechanics.tower

import game_mechanics.Bunny
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
object Wall extends TowerType {

    override val tower_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/wall.png").getPath()))

    override val buy_cost = 5

    override def fire_from(tower: Tower)(bunny: Bunny) {}

}
