
package runtime

import swing._
import swing.event._
import java.awt.event._

import collection.mutable.HashMap

import javax.swing.ImageIcon

import gui._
import game_mechanics._

object TowerDefense extends SimpleSwingApplication
{

  val map_panel  = new MapPanel(new GameMap(30,25))
  val info_panel = new InfoPanel
  val keymap     = new HashMap[Key.Value,Boolean] { override def default(key: Key.Value) = false }
  val play_button = new Button { action = Action("") { Controller.on_play_button() } }

  play_button.text = "Play"
  play_button.background = Colors.green

  /* Returns a panel containing the build menu */
  def make_build_menu(): GridPanel = {
    return new GridPanel( 3, 5 ) {
      val dimension = new Dimension( 50, 50 )
      for( i <- 0 until 15 ) {
        val button = new BuyButton { action = Action ("") { Controller.on_build_button( i ) } }
        button.minimumSize = dimension
        button.maximumSize = dimension
        if( i == 0 )
          button.icon = new ImageIcon( Tower.tower_graphic )
        contents += button
      }
    }
  }

  /* Returns a panel containing the in-game menu (next to the map) */
  def make_menu(): BoxPanel = {
    return new BoxPanel(Orientation.Vertical) {
      contents += info_panel
      contents += make_build_menu()
      contents += Swing.VGlue
      contents += play_button
    }
  }

  def top = new MainFrame
  {
    title = "Bunny Defense 9001"
    contents = new BoxPanel(Orientation.Horizontal)
    {
      contents += map_panel
      contents += make_menu()

      listenTo(this.keys)

      reactions += {
        case KeyPressed(_,key,_,_) =>
          keymap += (key -> true)
        case KeyReleased(_,key,_,_) =>
          keymap += (key -> false)
      }
      focusable = true
      requestFocus
    }
  }


  /* ========== MAIN ========== */
  override def main(args: Array[String]): Unit = {
    super.main(args)
    Controller.run()
  }
}
