
package runtime

import swing._
import swing.event._
import util.Random
import io.Source
import java.awt.Graphics2D
import java.awt.event._
import java.awt.image.BufferedImage

import collection.mutable.HashMap

import javax.swing.ImageIcon

import gui._
import utils._
import game_mechanics._
import game_mechanics.path._
import game_mechanics.tower._

object TowerDefense extends SimpleSwingApplication
{
    val keymap      = new HashMap[Key.Value,Boolean] {
        override def default(key: Key.Value) = false
    }
    val gui_size = new Dimension(
        1280, 720 )
    val framerate    = 1.0/60.0 * 1000
    def top = new MainFrame
    {
        val titles = Source.fromFile("src/main/resources/misc/titles").
            getLines().toArray
        title = titles(Random.nextInt(titles.length))
        Parameters.load()
        contents = StateManager.render_surface
        size = gui_size
        resizable = false

    }
    override def main(args: Array[String]): Unit = {
        super.main(args)
        StateManager.set_state( MainMenuState )
        StateManager.run()
        top.close()
        sys.exit(0)
    }
}
