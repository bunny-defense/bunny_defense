
package runtime

import swing._
import swing.event._

import gui._
import game_mechanics._

object TowerDefense extends SimpleSwingApplication
{

  /* Triggered when a map cell is clicked */
  def on_cell_clicked( x:Int, y:Int ): Unit = {
    println( x, y )
  }

  /* Triggered when a button from the build menu is clicked */
  def on_build_button( id:Int ): Unit = {}

  /* Triggered when the play button is clicked */
  def on_play_button(): Unit = {}

  /* Returns a panel containing the build menu */
  def make_build_menu(): GridPanel = {
    val dimension = new Dimension( 30, 30 )
    return new GridPanel( 3, 5 ) {
      for( i <- 0 until 15 ) {
        val button = new BuyButton { action = Action ("") { on_build_button( i ) } }
        button.preferredSize = dimension
        contents += button
      }
    }
  }

  /* Returns a panel containing the in-game menu (next to the map) */
  def make_menu(): BorderPanel = {
    return new BorderPanel {
      val play_button = new Button { action = Action("") { on_play_button() } }
      play_button.preferredSize = new Dimension( 50, 50 )
      play_button.text = "Play"
      play_button.background = Colors.red
      add( play_button, BorderPanel.Position.South )
      add( make_build_menu(), BorderPanel.Position.West )
    }
  }

  /* ========== MAIN ========== */
  def top = new MainFrame
  {
    title = "Tower Defense"
    contents = new BorderPanel
    {
      add( new MapPanel(new GameMap(10,20)), BorderPanel.Position.Center )
      add( make_menu(), BorderPanel.Position.East)
    }
  }
}
