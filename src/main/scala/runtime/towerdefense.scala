
package runtime

import swing._
import swing.event._
import util.Random
import io.Source
import java.awt.event._

import collection.mutable.HashMap

import javax.swing.ImageIcon

import gui._
import game_mechanics._
import game_mechanics.path._

object TowerDefense extends SimpleSwingApplication
{

    val map_panel   = new MapPanel(new GameMap(30,15))
    val build_menu  = new BuildMenu( 4, 4 )
    val info_panel  = new InfoPanel
    val tower_panel = new TowerPanel
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
    def make_menu(): BorderPanel = {
        val play_button = new Button {
            action = Action("") { Controller.on_play_button(this) }
            listenTo(SpawnScheduler)
            reactions += {
                case WaveEnded   =>
                    enabled = true
            }
            text       = "Play"
            background = Colors.green
            preferredSize = new Dimension( 100, 100 )
        }
      val build_pane = new BoxPanel(Orientation.Vertical) {
        contents += info_panel
        contents += build_menu
        contents += Swing.VGlue
      }
      return new BorderPanel {
        add( build_pane, BorderPanel.Position.Center )
        add( play_button, BorderPanel.Position.South )
      }
    }

    def top = new MainFrame
    {
        val titles = Source.fromFile("src/main/resources/misc/titles").
            getLines().toArray
        title = titles(Random.nextInt(titles.length))
        resizable = false
        contents = new BorderPanel
        {
            add (make_map, BorderPanel.Position.Center)
            add (make_menu(), BorderPanel.Position.East)
            listenTo(this.keys)

            reactions += {
                case KeyPressed(_,key,_,_) => {
                    keymap += (key -> true)
                }
                case KeyReleased(_,key,_,_) => {
                    keymap += (key -> false)
                }
            }
            focusable = true
            requestFocus
        }
    }


    /* ========== MAIN ========== */
    override def main(args: Array[String]): Unit = {
        super.main(args)
        Controller.run()
        quit()
    }
}
