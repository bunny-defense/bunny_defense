
package runtime

import swing._
import swing.event._
import util.Random
import io.Source
import java.awt.Graphics2D
import java.awt.event._

import collection.mutable.HashMap

import javax.swing.ImageIcon

import gui._
import game_mechanics._
import game_mechanics.path._
import game_mechanics.tower._


object TowerDefense extends SimpleSwingApplication
{
    val map_panel   = new MapPanel(new GameMap(30,15))
    val build_menu  = new BuildMenu( 4, 4 )
    val info_panel  = new InfoPanel
    val tower_panel = new TowerInfoPanel
    val keymap      = new HashMap[Key.Value,Boolean] {
        override def default(key: Key.Value) = false
    }


    def make_map() : BoxPanel = {
        return new BoxPanel(Orientation.Vertical) {
            contents += map_panel
            contents += tower_panel
        }
    }

    /* Returns a panel containing the in-game menu (next to the map) */
    def make_menu(): BorderPanel =
    {
        val play_button = new Button
        {
            action = Action("") { Controller.on_play_button(this) }
            listenTo(SpawnScheduler)
            reactions += {
                case WaveEnded =>
                    enabled = true
            }
            text       = "Play"
            background = Colors.green
            preferredSize = new Dimension( 100, 100 )
            focusable = false
        }
        val build_panel = new BoxPanel(Orientation.Vertical)
        {
            contents += info_panel
            contents += build_menu
            contents += Swing.VGlue
        }
        return new BorderPanel
        {
            add( build_panel, BorderPanel.Position.Center )
            add( play_button, BorderPanel.Position.South )
        }
    }

    val mainpanel = new BorderPanel
    {
        add (make_map, BorderPanel.Position.Center)
        add (make_menu(), BorderPanel.Position.East)

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

        focusable = true
        requestFocus
        override def paintChildren(g: Graphics2D)
        {
            super.paintChildren(g)
            val transform = g.getTransform()
            val pos = locationOnScreen
            g.translate( -pos.x, -pos.y )
            build_menu.draw_tooltip(g)
            g.setTransform( transform )
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
        resizable = false
        contents = mainpanel
    }


    /* ========== MAIN ========== */
    override def main(args: Array[String]): Unit = {
        super.main(args)
        Controller.run()
        top.close()
        sys.exit(0)
    }
}
