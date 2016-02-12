
package runtime

import swing._
import swing.event._

import javax.swing.ImageIcon

import gui._
import game_mechanics._

object TowerDefense extends SimpleSwingApplication
{

  val map_panel  = new MapPanel(new GameMap(30,25))
  val info_panel = new InfoPanel

  /* Returns a panel containing the build menu */
  def make_build_menu(): GridPanel = {
    val dimension = new Dimension( 30, 30 )
    return new GridPanel( 3, 5 ) {
      preferredSize = new Dimension( 5 * 30, 3 * 30 )
      for( i <- 0 until 15 ) {
        val button = new BuyButton { action = Action ("") { Controller.on_build_button( i ) } }
        button.preferredSize = dimension
        if( i == 0 )
          button.icon = new ImageIcon( Tower.tower_graphic )
        contents += button
      }
    }
  }

  /* Returns a panel containing the in-game menu (next to the map) */
  def make_menu(): BoxPanel = {
    return new BoxPanel(Orientation.Vertical) {
      val play_button = new Button { action = Action("") { Controller.on_play_button() } }
      play_button.preferredSize = new Dimension( 100, 50 )
      play_button.text = "Play"
      play_button.background = Colors.green
      contents += info_panel
      contents += make_build_menu()
      contents += Swing.VGlue
      contents += play_button
    }
  }

  /* ========== MAIN ========== */
  def top = new MainFrame
  {
    title = "Tower Defense"
    contents = new BorderPanel
    {
      add( map_panel, BorderPanel.Position.Center )
      add( new BoxPanel(Orientation.Vertical) {
        contents += make_menu()
      }, BorderPanel.Position.East)
    }
  }

  override def main(args: Array[String]): Unit = {
    super.main(args)
    Controller.run()
  }
}
