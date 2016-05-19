
package runtime

import collection.parallel._
import collection.mutable.ListBuffer

import game_mechanics._
import game_mechanics.bunny._
import game_mechanics.tower._
import game_mechanics.path._
import gui._
import tcp._
import tcp.packets._

class ServerGameState(_map: Array[Array[Boolean]], _server: Server)
extends GameState(_map)
{
    override val gui = new MainMenu(None)
    val server = _server
    for( peer <- server.peers )
        players += peer.player
    def init(): Unit = {
        val serplayers = new ListBuffer[(Int,String,CellPos)]
        players.foreach( x => serplayers += ((x.id,x.name,x.base)) )
        server.broadcast(GameStartPacket(_map, serplayers))
    }
    init()
    val handle : (ServerThread, Any) => Unit = {
        (peer, packet) => {
            packet match {
                case ("removed", d: Int, p: Int) => {
                    val toRemove = bunnies.find(
                        x => ((x.id == d) && (x.owner == p)))
                    if (!toRemove.isEmpty) {
                        bunnies -= toRemove.get
                    }
                    server.send(peer, ("removed", d, p))
                }
                case ("lost", d: Int, pid: Int) => {
                    server.send(peer, ("lost", d, pid))
                }
                case ("placing", t : TowerType, pos : CellPos, id : Int) => {
                    this += new Tower(players(id), t, pos, this)
                    var bun_update = bunnies.filter( t => t.path.path.exists(
                        u => u.x == pos.x && u.y == pos.y)).par
                    bun_update.tasksupport = new ForkJoinTaskSupport(
                        new scala.concurrent.forkjoin.ForkJoinPool(8))
                    val centering = new Waypoint( 0.5, 0.5 )
                    for (bunny <- bun_update) {
                        bunny.path.path = new JPS(
                            (bunny.pos + centering).toInt,
                            bunny.bunnyend,
                            this).run().get
                        bunny.path.reset
                        bunny.bunnyend = bunny.path.last.toInt
                    }
                    server.send(peer, ("placing", t, pos, id))
                }
            }
        }
    }
    for( peer <- server.peers )
    {
        peer.handle = handle
    }
    def sync(): Unit = {
        server.broadcast(("sync_towers", towers))
        server.broadcast(("sync_bunnies", bunnies))
    }
    /* ==================== STRATEGIES ==================== */

    // BUNNIES
    override def bunny_death_render_strategy(bunny: Bunny) : Unit = {
    }
    override def bunny_reach_goal_strategy(bunny: Bunny) : Unit = {
        server.broadcast(("removed", bunny.id, bunny.owner.id))
        server.broadcast(("lost", bunny.damage, bunny.owner.id))
    }
    override def spec_ops_jump_strategy(bunny: Bunny) : Unit = {}

    // PROJECTILES
    override def splash_projectile_hit_strategy(
        projectile: Projectile) : Unit = {}

    // TOWER
    override  def tower_fire_strategy(tower: Tower) : Unit = {}
    override  def supp_buff_tower_animation_strategy(tower: Tower) : Unit = {}
    override  def supp_slow_tower_animation_strategy(tower: Tower) : Unit = {}

}
