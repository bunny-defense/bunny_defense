package tcp

import java.net._
import java.io._
import scala.io._

import scala.collection.mutable.{ListBuffer,Queue}
import runtime._
import game_mechanics._
import game_mechanics.bunny._
import game_mechanics.tower._
import game_mechanics.utilitaries._
import game_mechanics.updatable._

object Server {
    def main(args : Array[String]) : Unit = {
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
}

class ServerThread(socket : Socket) extends Thread("ServerThread") {

    try {
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
                case _ => None
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
            out.writeObject(TowerDefense.gamestate.towers)
            out.writeObject(TowerDefense.gamestate.bunnies)
            out.writeObject(TowerDefense.gamestate.projectiles)
            out.writeObject(TowerDefense.gamestate.updatables)
            out.writeObject(TowerDefense.gamestate.utilitaries)
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
