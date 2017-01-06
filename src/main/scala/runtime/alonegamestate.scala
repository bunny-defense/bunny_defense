
package runtime

import collection.mutable.ListBuffer
import collection.parallel._

import swing.event._

import game_mechanics._
import game_mechanics.tower._
import game_mechanics.bunny._
import game_mechanics.path._
import gui._
import gui.animations._


class AloneGameState(
    _player: Player,
    _map: Array[Array[Boolean]])
extends GuiGameState( _player, _map )
{
    val state         = this
    val multiplayer   = false
    val enemy         = new Player("Enemy")

    listenTo(SpawnScheduler)
    reactions += {
      case WaveEnded => {
        wave_counter += 1
        play_button.enabled = true
        play_button.color = Colors.green
        acceleration = 1
      }
      case _ => {}
    }

    override def update(dt: Double) : Unit = {
      for (i<-1 to acceleration) {
        update_gui(dt)
        SpawnScheduler.update(dt, state)
        super.update(dt)
        if (TowerDefense.keymap(Key.Escape)) {
          selected_cell  = None
          selected_tower = None
        }
      }
    }
    /* ====================    GUI     ==================== */

   val play_button = new TextButton(Some(gui), "Play") {
     pos = new CellPos(1040, 630)
     size = new CellPos(220, 60)
     color = Colors.green
     text_color = Colors.black
     override def action() : Unit = {
       this.enabled = false
       color = Colors.lightGrey
       val spawner       = new Spawner(wave_counter)
       val spawnschedule = spawner.create()
       SpawnScheduler.set_schedule(spawnschedule)
       val anim = new WaveAnimation(wave_counter, state )
       anim and_then {
         if (spawner.has_boss) {
           val splash_anim = new SplashAnimation( state )
           splash_anim and_then { () => state += anim }
           state += splash_anim
         } else {
           state += anim
         }
         SpawnScheduler.start
       }
     }
   }

   /* ==================== STRATEGIES ==================== */

  // BUNNIES
  override def bunny_death_render_strategy(bunny: Bunny) : Unit = {
      this += new GoldAnimation(
          bunny.reward(this.wave_counter),
          bunny.pos.clone(),
          this)
  }
  override def bunny_reach_goal_strategy(bunny: Bunny) : Unit = {
      player.remove_hp(bunny.damage)
  }
  override def spec_ops_jump_strategy(bunny: SpecOpBunny) : Unit = {
      if( rng.nextDouble < 1.0/720.0) {
          bunny.jumping = true
          this -= bunny
          val anim = new SmokeAnimation(bunny.pos, this)
          anim and_then { () =>
              bunny.path.random_choice
              bunny.pos = bunny.path.get_position
              this += bunny
              this += new SmokeAnimation(bunny.pos, this)
          }
      }
  }

  // PROJECTILES
  override def splash_projectile_hit_strategy(
      projectile: SplashProjectile) : Unit = {
          for (dir <- 0 to 12) {
              this += new SpreadAnimation(
                  projectile.target_pos,
                  projectile.radius,
                  new Waypoint (Math.cos(dir.toDouble*360.0/12.0),
                      Math.sin(dir.toDouble*360.0/12.0)),
                  this
              )
          }
  }

  // TOWER
  override  def tower_fire_strategy(tower: Tower) : Unit = {
      this += new MuzzleflashAnimation(tower.pos.toDouble, this)
  }
  override  def supp_buff_tower_animation_strategy(tower: Tower) : Unit = {
      def new_buff_anim() : Unit = {
          val anim = new BuffAnimation( tower.pos, tower.range, this)
          anim and_then new_buff_anim
          this += anim
      }
      new_buff_anim
  }

  override  def supp_slow_tower_animation_strategy(tower: Tower) : Unit = {
      def new_snow_anim() : Unit = {
          val snow_anim = new SnowAnimation( tower.pos, tower.range, this)
          snow_anim and_then new_snow_anim
          this += snow_anim
      }
      new_snow_anim
  }
  override def new_tower_strategy(tower : TowerType , pos: CellPos) : Unit = {
      this += new Tower(
          player,
          tower, pos, this)
      var bun_update = bunnies.filter( t => t.path.path.exists(
          u => u.x == pos.x && u.y == pos.y)).par
      bun_update.tasksupport = new ForkJoinTaskSupport(
          new scala.concurrent.forkjoin.ForkJoinPool(8))
      val centering = new Waypoint( 0.5, 0.5 )
      for (bunny <- bun_update) {
          bunny.path.path = new JPS(
              (bunny.pos + centering).toInt,
              bunny.path.last.toInt,
              this.map).run().get
          bunny.path.reset
      }
  }
}
