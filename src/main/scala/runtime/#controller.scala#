
package runtime

import swing.event._
import swing._

import collection.mutable.{ListBuffer,Queue}

import runtime._
import game_mechanics._
import game_mechanics.path._
import gui._


case object SelectedCell extends Event
case object NoSelectedCell extends Event

object Controller extends Publisher
{
  val bunnies      = new ListBuffer[Bunny]
  val projectiles  = new ListBuffer[Throw]
  val towers       = new ListBuffer[Tower]
  val animations   = new ListBuffer[Animatable]
  var wave_counter = 0
  val framerate    = 1.0/30.0 * 1000
  var started      = false
  var selected_tower : Option[Tower] = None
  private var _selected_cell  : Option[Tower] = None

  def selected_cell_=(tower: Option[Tower]): Unit =
  {
    if (tower != None)
      publish(SelectedCell)
    else
      publish(NoSelectedCell)
    _selected_cell = tower
  }

  def selected_cell =  _selected_cell
  /* Triggered when a map cell is clicked */
  def on_cell_clicked( x:Int, y:Int ): Unit = {
    // Placing a new tower
    if( selected_tower != None &&
      !TowerDefense.map_panel.map.obstructed(x,y) )
    {
      if( Player.remove_gold(selected_tower.get.buy_cost) )
          Controller += selected_tower.get.clone_at( new CellPos(x,y) )
      else
        println("Not enough money! Current money = "+ Player.gold.toString)
    }
    // Selecting a placed tower
    else if ( selected_tower == None )
    {
      val position = new CellPos(x,y)
      selected_cell = towers.find( _.pos == position )
    }
    // Building multiple towers
    if ( !TowerDefense.keymap(Key.Shift) ) {
      selected_tower = None
    }
  }

  /* Triggered when a button from the build menu is clicked */
  def on_build_button( button: BuyButton ): Unit = {
    selected_tower = button.tower
  }

  /* Triggered when the play button is clicked */
  def on_play_button(): Unit = {
      println( "New wave")
      wave_counter += 1
      var spawnschedule = new Spawner(wave_counter).create
      SpawnScheduler.set_schedule(spawnschedule)
      SpawnScheduler.start()
  }

  def update(dt: Double): Unit = {
    /* Update animations */
    for( animation <- animations )
      animation.update(dt)
    /* Update projectiles */
    for( projectile <- projectiles )
      projectile.update(dt)
    /* Update towers */
    for( tower <- towers )
      tower.update(dt)
    /* Update bunnies */
    for( bunny <- bunnies )
      bunny.update(dt)
    /* Spawn in new bunnies */
    SpawnScheduler.update(dt)
  }

  def run(): Unit = {
    println( "Running" )
    var dt: Double = 0.0
    var counter = 0
    while( true )
      {
      val start = System.currentTimeMillis
      update(dt)
      if ( TowerDefense.keymap(Key.Escape)) {
        selected_tower = None
        selected_cell  = None
      }
      TowerDefense.map_panel.repaint()
      TowerDefense.info_panel.repaint()
      TowerDefense.tower_panel.thepanel.repaint()
      val miliseconds = framerate.toInt - (System.currentTimeMillis - start)
      Thread.sleep(miliseconds)
      dt = (System.currentTimeMillis - start).toDouble / 1000
      if (Player.hp <= 0) {
          println("You lose")
          return
        }
    }
  }

  /* BUNNIES */

  def +=(bunny: Bunny): Unit = {
    bunnies += bunny
  }

  def -=(bunny: Bunny): Unit = {
    bunnies -= bunny
  }

  /* PROJECTILES */

  def +=(projectile: Throw): Unit = {
    projectiles += projectile
  }

  def -=(projectile: Throw): Unit = {
    projectiles -= projectile
  }

  /* TOWERS */

  def +=(tower: Tower): Unit = {
    towers += tower
    TowerDefense.map_panel.map += tower
  }

  def -=(tower: Tower): Unit = {
    towers -= tower
    TowerDefense.map_panel.map -= tower
  }

  /* ANIMATIONS */

  def +=(animation: Animatable): Unit = {
    animations += animation
  }

  def -=(animation: Animatable): Unit = {
    animations -= animation
  }
}
