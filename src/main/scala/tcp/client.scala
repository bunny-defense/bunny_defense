package tcp

import java.net.{InetAddress, Socket }
import java.io._

import scala.io._
import collection.parallel._

import runtime._
import game_mechanics._
import game_mechanics.bunny._
import game_mechanics.tower._
import game_mechanics.utilitaries._
import game_mechanics.path._
import gui._
import gui.animations._

import scala.collection.mutable.{ListBuffer,Queue}

class ClientThread(domain : String) extends Thread("Client Thread"){

    try {
        val socket = new Socket(InetAddress.getByName(domain),9999)
        val in = new ObjectInputStream(
            new DataInputStream(socket.getInputStream()))
        val out = new ObjectOutputStream(
            new DataOutputStream(socket.getOutputStream()))
        val queue  = new Queue[Any]()

        out.writeObject("New Client")
        out.flush()

        def add(arg : Any): Unit = {
            queue.enqueue(arg)
        }

        def send(arg : Any) : Unit = {
            out.writeObject(arg)
            out.flush()
        }

        def receive() : Any = {
            in.readObject() match {
                case ("sync_bunnies", l: ListBuffer[Bunny]) => {
                    for (bunny <- l) {
                        val bunch = TowerDefense.gamestate.bunnies.find(
                            x => x.id == bunny.id && x.player == bunny.player)
                        if (bunch == None)
                        {
                            TowerDefense.gamestate += bunny
                        }
                        else
                        {
                            TowerDefense.gamestate -= bunch.get
                            TowerDefense.gamestate += bunny
                        }
                    }
                    for (bunny <- TowerDefense.gamestate.bunnies.filter(x=> (l.filter(
                        y => y.id == x.id && y.player == y.player)).isEmpty)) {
                        TowerDefense.gamestate -= bunny
                    }
                }
                case ("sync_towers", l: ListBuffer[Tower]) => {
                    for (tower <- l) {
                        val bunch = TowerDefense.gamestate.towers.find(
                            x => x.player == tower.player && x.id == tower.id)
                        if (bunch == None)
                        {
                            TowerDefense.gamestate += tower
                        }
                        else
                        {
                            TowerDefense.gamestate -= bunch.get
                            TowerDefense.gamestate += tower
                        }
                    }
                    for (tower <- TowerDefense.gamestate.towers.filter(x=> (l.filter(
                        y => y.id == x.id && y.player == y.player)).isEmpty)) {
                        TowerDefense.gamestate -= tower
                    }
                }
                case ("sync_utilitaries", l: ListBuffer[Utilitary]) => {
                    for (utilitary <- l) {
                        val bunch = TowerDefense.gamestate.utilitaries.find(
                            x => x.player == utilitary.player && x.id == utilitary.id)
                        if (bunch == None)
                        {
                            TowerDefense.gamestate += utilitary
                        }
                        else
                        {
                            TowerDefense.gamestate -= bunch.get
                            TowerDefense.gamestate += utilitary
                        }
                    }
                    for (utilitary <- TowerDefense.gamestate.utilitaries.filter(x=> (l.filter(
                        y => y.id == x.id && y.player == y.player)).isEmpty)) {
                        TowerDefense.gamestate -= utilitary
                    }
                }
                case ("jumped", x: Int, y: Int, p: Waypoint) => {
                    val obunny = TowerDefense.gamestate.bunnies.find(t => ((t.player == y )&&(t.id == x)))
                    if (!obunny.isEmpty) {
                        val bunny = obunny.get
                        TowerDefense.gamestate -= bunny
                        val anim = new SmokeAnimation(bunny.pos)
                        anim and_then { () =>
                            bunny.pos = p
                            TowerDefense.gamestate += bunny
                            TowerDefense.gamestate += new SmokeAnimation(bunny.pos)
                        }
                        TowerDefense.gamestate += anim
                    }
                }
                case (l: TowerType, (x:Int, y:Int), id: Int) => {
                    TowerDefense.gamestate += new Tower(l, new CellPos(x,y),id)
                }
                case ("removed", d: Int, p: Int) => {
                    val toRemove = TowerDefense.gamestate.bunnies.find(
                        x => (x.id == d) && (x.player == p))
                    if (!toRemove.isEmpty) {
                        TowerDefense.gamestate.bunnies -= toRemove.get
                    }
                }
                case ("lost", d: Int, pid: Int) => {
                    TowerDefense.gamestate.players(pid).remove_hp(d)
                }
                case ("placing", t : TowerType, pos : CellPos, id : Int) => {
                    TowerDefense.gamestate += new Tower(t, pos, id)
                    var bun_update = TowerDefense.gamestate.bunnies.filter( t => t.path.path.exists(
                        u => u.x == pos.x && u.y == pos.y)).par
                    bun_update.tasksupport = new ForkJoinTaskSupport(
                        new scala.concurrent.forkjoin.ForkJoinPool(8))
                    val centering = new Waypoint( 0.5, 0.5 )
                    for (bunny <- bun_update) {
                        bunny.path.path = new JPS(
                            (bunny.pos + centering).toInt,
                            bunny.bunnyend).run().get
                        bunny.path.reset
                        bunny.bunnyend = bunny.path.last.toInt
                    }
                }
            }
        }

        def close() = {
            out.close()
            in.close()
            socket.close()
        }

        def run(): Unit = {
            while(true) {
                if (!queue.isEmpty) {
                    send(queue.dequeue())
                }
            }
        }
    }
    catch {
        case e: IOException =>
            e.printStackTrace()
    }
}

// vim: set ts=4 sw=4 et:
