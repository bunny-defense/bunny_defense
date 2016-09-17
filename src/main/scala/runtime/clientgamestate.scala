
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
import tcp._
import tcp.packets._

class ClientGameState(
    _player: Player,
    _players: ListBuffer[Player],
    map: Array[Array[Boolean]],
    _server: ClientThread)
extends GuiGameState(_player, map)
{
    /* The server this client is connected to */
    val server = _server
    val multiplayer = true
    /* The player associated to this client */
    override val players = _players

    override def update(dt: Double) : Unit = {
        update_gui(dt)
        super.update(dt)
        if (TowerDefense.keymap(Key.Escape)) {
            selected_cell  = None
            selected_tower = None
        }
    }

    /* ==================== SERVER COM ==================== */

    val handle : Any => Unit = {
        packet => {
            println("GameState received " + packet.toString)
            packet match {
                case PlacedTower(towertype, pos, player_id) =>
                    val t = TowerType.deserialize(towertype)
                    this += new Tower(
                        players(player_id),
                        t, pos, this)
                var bun_update = bunnies.filter( t => t.path.path.exists(
                    u => u.x == pos.x && u.y == pos.y)).par
                bun_update.tasksupport = new ForkJoinTaskSupport(
                    new scala.concurrent.forkjoin.ForkJoinPool(8))
                val centering = new Waypoint( 0.5, 0.5 )
                for (bunny <- bun_update) {
                    bunny.path.path = new JPS(
                        (bunny.pos + centering).toInt,
                        bunny.path.last.toInt,
                        this).run().get
                    bunny.path.reset
                }
                case ("sync_bunnies", l: ListBuffer[Bunny]) => {
                    for (bunny <- l) {
                        val bunch = bunnies.find(
                            x => x.id == bunny.id && x.owner == bunny.owner)
                        if (bunch == None)
                        {
                            this += bunny
                        }
                        else
                        {
                            this -= bunch.get
                            this += bunny
                        }
                    }
                    for (bunny <- bunnies.filter(x=> (l.filter(
                        y => y.id == x.id && y.owner == y.owner)).isEmpty)) {
                        this -= bunny
                    }
                }
                case ("sync_towers", l: ListBuffer[Tower]) => {
                    for (tower <- l) {
                        val bunch = towers.find(
                            x => x.owner == tower.owner && x.id == tower.id)
                        if (bunch == None)
                        {
                            this += tower
                        }
                        else
                        {
                            this -= bunch.get
                            this += tower
                        }
                    }
                    for (tower <- towers.filter(x=> (l.filter(
                        y => y.id == x.id && y.owner == y.owner)).isEmpty)) {
                        this -= tower
                    }
                }
                case ("jumped", x: Int, y: Int, p: Waypoint) => {
                    val obunny = bunnies.find(t => ((t.owner.id == y )&&(t.id == x)))
                    if (!obunny.isEmpty) {
                        val bunny = obunny.get
                        this -= bunny
                        val anim = new SmokeAnimation(bunny.pos, this)
                        anim and_then { () =>
                            bunny.pos = p
                            this += bunny
                            this += new SmokeAnimation(bunny.pos, this)
                        }
                        this += anim
                    }
                }
                case ("removed", d: Int, p: Int) => {
                    val toRemove = bunnies.find(
                        x => (x.id == d) && (x.owner == p))
                    if (!toRemove.isEmpty) {
                        bunnies -= toRemove.get
                    }
                }
                case ("lost", d: Int, pid: Int) => {
                    players(pid).remove_hp(d)
                }
                case ("Thunder", time : Double) => {
                    val anim = new ThunderstormAnimation( time, this )
                    this += anim
                }
                case ("Rain", time : Double) => {
                    val anim = new RainAnimation( time, this )
                    this += anim
                }
            }
        }
    }
    server.handle = this.handle
    /* ==================== STRATEGIES ==================== */

    // BUNNIES
    override def bunny_death_render_strategy(bunny: Bunny) : Unit = {
        this += new GoldAnimation(
            bunny.reward(1),
            bunny.pos.clone(),
            this)
    }
    override def bunny_reach_goal_strategy(bunny: Bunny) : Unit = {}
    override def spec_ops_jump_strategy(bunny: SpecOpBunny) : Unit = {}

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
        this.server.send(PlacingTower(
            tower.serialize(), pos))
        println("Sent tower")
    }
}
