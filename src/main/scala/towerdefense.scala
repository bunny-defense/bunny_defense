
import swing._
import swing.event._

object MapCell {
  val cellsize = 40
  val dimension = new Dimension( cellsize, cellsize )
  val white = new Color( 255, 255, 255 )
  val blue = new Color( 0, 0, 255 )
}

class MapCell extends Button {
  this.preferredSize = MapCell.dimension
  this.background = MapCell.white
  //this.border = Swing.LineBorder( MapCell.blue )
}

object TowerDefense extends SimpleSwingApplication
{

  def on_cell_clicked( x:Int, y:Int ): Unit = {
    println( x, y )

  }

  def make_grid(): GridPanel = {
    return new GridPanel(10, 20) {
      for( i <- 0 until 200 ) {
        contents += new MapCell {
          action = Action("") {
            on_cell_clicked( i % 20, i / 20 ) }
        }
      }
    }
  }

  def on_build_button( id:Int ): Unit = {}

  def on_play_button(): Unit = {}

  def make_build_menu(): GridPanel = {
    val dimension = new Dimension( 30, 30 )
    return new GridPanel( 3, 5 ) {
      for( i <- 0 until 15 ) {
        val button = new Button { action = Action ("") { on_build_button( i ) } }
        button.preferredSize = dimension
        button.background = MapCell.white
        contents += button
      }
    }
  }

  def make_menu(): BorderPanel = {
    return new BorderPanel {
      val play_button = new Button { action = Action("") { on_play_button() } }
      play_button.preferredSize = MapCell.dimension
      play_button.background = MapCell.white
      //play_button.border = Swing.LineBorder( MapCell.blue )
      add( play_button, BorderPanel.Position.Center )
      add( make_build_menu(), BorderPanel.Position.West )
    }
  }

  def top = new MainFrame
  {
    title = "Tower Defense"
    contents = new BorderPanel
    {
      add( make_grid(), BorderPanel.Position.Center )
      add( make_menu(), BorderPanel.Position.South )
    }
  }
}
