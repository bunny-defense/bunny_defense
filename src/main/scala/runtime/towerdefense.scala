
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
    val gamestate   = new GameState()
    val map_panel   = new MapPanel(new GameMap(30,15))
    val build_menu  = new BuildMenu( 4, 4 )
    val info_panel  = new InfoPanel
    val tower_panel = new TowerInfoPanel
    val keymap      = new HashMap[Key.Value,Boolean] {
        override def default(key: Key.Value) = false
    }
    val gui_size = new Dimension( 800, 600 )
    val framerate    = 1.0/60.0 * 1000

    def make_map : TDComponent = {
        return new TDComponent
        {
            children += map_panel
            children += tower_panel
        }
    }

    /* Returns a panel containing the in-game menu (next to the map) */
    def make_menu() : TDComponent =
    {
        val play_button = new TextButton
        (StateManager.render_surface, 0, 0, 100, 100, "Play")
        {
            override def action() : Unit = {
                gamestate.on_play_button(this)
            }
            /*
            listenTo(SpawnScheduler)
            reactions += {
                case WaveEnded =>
                    enabled = true
            }
            text       = "Play"
            background = Colors.green
            preferredSize = new Dimension( 100, 100 )
            focusable = false
            */
            size = new CellPos( 100, 100 )
        }
        val build_panel = new TDComponent
        {
            children += info_panel
            children += build_menu
            children += Swing.VGlue
        }
        return new TDComponent
        {
            children += build_panel
            children += play_button
        }
    }

    listenTo(this.keys)
    reactions +=
    {
        case KeyPressed(_,key,_,_) => {
            keymap += (key -> true)
        }
        case KeyReleased(_,key,_,_) => {
            keymap += (key -> false)
        }
    }

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
        Player.reset()
        resizable = false
        contents = StateManager.render_surface
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
