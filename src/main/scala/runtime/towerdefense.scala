
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

  val map_panel  = new MapPanel(new GameMap(30,25))
  val info_panel = new InfoPanel
  val keymap     = new HashMap[Key.Value,Boolean] { override def default(key: Key.Value) = false }

  /* Returns a panel containing the build menu */
  def make_build_menu(): GridPanel = {
    val dimension = new Dimension( 50, 50 )
    val waypoint  = new Waypoint( 0, 0 )

    /* Tower types list */
    import collection.mutable.Queue
    val towers = new Queue[Tower]
    towers += new Tower(BaseTower, waypoint)
    towers += new Tower(QuickTower,waypoint)
    towers += new Tower(HeavyTower,waypoint)
    towers += new AOETower(ScarecrowTower,waypoint)
    /* To fill... */

    return new GridPanel( 3, 5 ) {
      for( i <- 0 until 15 ) {
        val tower  = try { Some(towers.dequeue) } catch { case e: Exception => None }
        val button = new BuyButton( tower )
        button.minimumSize = dimension
        button.maximumSize = dimension
        contents += button
      }
    }
  }

  /* Returns a panel containing the in-game menu (next to the map) */
  def make_menu(): BoxPanel = {
    return new BoxPanel(Orientation.Vertical) {
      val play_button = new Button {
        action = Action("") { Controller.on_play_button() }
        listenTo(SpawnScheduler)
        reactions += {
          case WaveStarted =>
            enabled = false
          case WaveEnded   =>
            enabled = true
        }
        text       = "Play"
        background = Colors.green
      }

      contents += info_panel
      contents += make_build_menu()
      contents += Swing.VGlue
      contents += play_button
    }
  }

  def top = new MainFrame
  {
    val titles = Source.fromFile("src/main/resources/misc/titles").getLines().toArray
    title = titles(Random.nextInt(titles.length))
    contents = new BoxPanel(Orientation.Horizontal)
    {
      contents += map_panel
      contents += make_menu()

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
  }
}
