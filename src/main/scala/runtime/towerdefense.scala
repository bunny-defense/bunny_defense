
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
import strategy._


object TowerDefense extends SimpleSwingApplication
{
    val gamestate   = new GameState(new Strategy())
    val keymap      = new HashMap[Key.Value,Boolean] {
        override def default(key: Key.Value) = false
    }
    val gui_size = new Dimension(
        gamestate.map_panel.size.x + gamestate.build_menu.size.x,
        gamestate.map_panel.size.y + gamestate.tower_panel.size.y )
    val framerate    = 1.0/60.0 * 1000

    /*
    val mainpanel = new Panel
    {
        preferredSize = new Dimension(
            map_panel.width + build_menu.width,
            map_panel.height )
        val map_panel_rect = new Rectangle( 0, 0,
            map_panel.width, map_panel.height )
        val build_menu_rect = new Rectangle(
            map_panel.width, 0,
            build_menu.width, build_menu.height )
        override def paintComponent(g: Graphics2D) : Unit = {
            super.paintComponent(g)
            val transform = g.getTransform()
            val clip      = g.getClip()
            g.clip( map_panel_rect )
            map_panel.paintComponent(g)
            g.setTransform( transform )
            g.setClip(clip)
        }
    }
    */

    def top = new MainFrame
    {
        val titles = Source.fromFile("src/main/resources/misc/titles").
            getLines().toArray
        title = titles(Random.nextInt(titles.length))
        Parameters.load()
        gamestate.player.reset()
        contents = StateManager.render_surface
        size = gui_size
        resizable = false

    }


    /* ========== MAIN ========== */
    override def main(args: Array[String]): Unit = {
        super.main(args)
        StateManager.set_state( new MainMenuState() )
        StateManager.run()
        top.close()
        sys.exit(0)
    }
}
