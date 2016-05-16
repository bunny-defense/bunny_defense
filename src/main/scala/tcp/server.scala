package tcp

import java.net._
import java.io._
import scala.io._
import collection.parallel._

import scala.collection.mutable.{ListBuffer,Queue}
import runtime._
import game_mechanics._
import game_mechanics.bunny._
import game_mechanics.tower._
import game_mechanics.utilitaries._
import game_mechanics.path._

class Server {
    try {
        val listener = new ServerSocket(9999)
        while (true) { new ServerThread(listener.accept()).start() }
        listener.close()
    }
    catch {
        case e: IOException =>
            System.err.println("Could not listen on port: 9999.")
            System.exit(1)
    }
}

class ServerThread(
    gamestate: GameState,
    socket : Socket)
extends Thread("ServerThread")
{
    try
    {
        val out = new ObjectOutputStream(
            new DataOutputStream(socket.getOutputStream()))
        val in  = new ObjectInputStream(
            new DataInputStream(socket.getInputStream()))
        val queue = new Queue[Any]()

        def close() : Unit = {
            out.close()
            in.close()
            socket.close()
        }

        def send(arg : Any): Unit = {
            out.writeObject(arg)
            out.flush()
        }

        def add(arg: Any): Unit = {
            queue.enqueue(arg)
        }


        def receive() : Any = {
            in.readObject() match {
                case ("removed", d: Int, p: Int) => {
                    val toRemove = gamestate.bunnies.find(
                        x => ((x.id == d) && (x.player == p)))
                    if (!toRemove.isEmpty) {
                        gamestate.bunnies -= toRemove.get
                    }
                    add(("removed", d, p))
                }
                case ("lost", d: Int, pid: Int) => {
                    add(("lost", d, pid))
                }
                case ("placing", t : TowerType, pos : CellPos, id : Int) => {
                    gamestate += new Tower(t, pos, id)
                    var bun_update = gamestate.bunnies.filter( t => t.path.path.exists(
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
                    ServerThread.add("placing", t,p,id)
                }
            }
        }

        def run(): Unit = {
            while (true) {
                if (!queue.isEmpty) {
                    send(queue.dequeue())
                }
            }
        }

        def sync(): Unit = {
            out.flush()
            out.writeObject(("sync_towers", gamestate.towers))
            out.writeObject(("sync_bunnies", gamestate.bunnies))
            out.writeObject(("sync_utilitaries", gamestate.utilitaries))
        }
    }
    catch {
        case e: SocketException =>
            println(e.toString)
        case e: IOException =>
            e.printStackTrace();
    }
}
// vim: set ts=4 sw=4 et:
