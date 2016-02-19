
package runtime

import swing.event._

import collection.mutable.{ListBuffer,Queue}

import runtime._
import game_mechanics._
import game_mechanics.path._
import gui._

object Controller
{
  val bunnies     = new ListBuffer[Bunny]
  val projectiles = new ListBuffer[Throw]
  val towers      = new ListBuffer[Tower]
  val animations  = new ListBuffer[Animatable]
  var wave_counter = 0
  val framerate = 1.0/30.0 * 1000
  var started = false
  var selected_tower : Option[Tower] = None

  /* Triggered when a map cell is clicked */
  def on_cell_clicked( x:Int, y:Int ): Unit = {
    if( selected_tower != None &&
      !TowerDefense.map_panel.map.obstructed(x,y) &&
      Player.remove_gold(selected_tower.get.buy_cost))
    {
      Controller += selected_tower.get.clone_at( new CellPos(x,y) )
      TowerDefense.map_panel.map += towers(0)
    }
    else if ( selected_tower != None &&
      TowerDefense.map_panel.map.obstructed(x,y)) {
      println("Cell obstructed ("+x.toString+","+y.toString+")")
    }
    else if (selected_tower != None){
      println("Not enough money! Current money = "+ Player.gold.toString)
    }
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
      TowerDefense.map_panel.repaint()
      TowerDefense.info_panel.repaint()
      val miliseconds = framerate.toInt - (System.currentTimeMillis - start)
      Thread.sleep(miliseconds)
      dt = (System.currentTimeMillis - start).toDouble / 1000
      if (Player.hp <= 0) {
          println("You lose")
          return
        }
      /* Debugging stuff */
      /*
      counter += 1
      if( counter >= 30 )
      {
        counter = 0
        if( !bunnies.isEmpty )
          println( dt )
          println( bunnies.head.pos )
      }
       */
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
  }

  def -=(tower: Tower): Unit = {
    towers -= tower
  }

  /* ANIMATIONS */

  def +=(animation: Animatable): Unit = {
    animations += animation
  }

  def -=(animation: Animatable): Unit = {
    animations -= animation
  }
}
