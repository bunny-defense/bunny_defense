package tcp

import java.net.{InetAddress, Socket }
import java.io._

import scala.io._
import collection.parallel._

import runtime._
import game_mechanics._
import game_mechanics.bunny._
import game_mechanics.tower._
import game_mechanics.path._
import gui._
import gui.animations._
import tcp.packets._

import scala.collection.mutable.{ListBuffer,Queue}

class ClientThread(domain : String)
extends Thread("Client Thread")
{
    val socket = new Socket(InetAddress.getByName(domain),Server.default_port)
    val in = new ObjectInputStream(
        new DataInputStream(socket.getInputStream()))
    val out = new ObjectOutputStream(
        new DataOutputStream(socket.getOutputStream()))
    val queue  = new Queue[Any]()
    val player = new Player("Unamed")

    def add(arg : Any): Unit = {
        queue.enqueue(arg)
    }

    def send(arg : Any) : Unit = {
        out.writeObject(arg)
        out.flush()
    }

    var handle : Any => Unit = { packet =>
        println("Lobby")
        println(packet)
        packet match {
            case PlayerIdPacket(id) => {
                player.id = id
            }
            case GameStartPacket(map,serplayers) => {
                val players = new ListBuffer[Player]
                for( (id,name,base) <- serplayers )
                {
                    val ply = new Player(name)
                    ply.id = id
                    ply.base = base
                    players += ply
                }
                StateManager.set_state(
                    new ClientGameState(players(player.id),
                        players, map, this) )
            }
        }
    }

    def receive() : Unit = {
        handle(in.readObject())
    }

    class Receiver extends Thread("ServerReceiver")
    {
        override def run() : Unit = {
            try {
                while(true)
                    receive()
            }
            catch
            {
                case e : IOException =>
                    StateManager.set_state( new ErrorMenuState(
                        e.toString, MultiplayerMenuState ) )
            }
        }
    }

    def close() = {
        out.close()
        in.close()
        socket.close()
    }

    override def run(): Unit = {
        new Receiver().start()
        send(PlayerInfoPacket("RaptorBunny1234"))
        while(true) {
            if (!queue.isEmpty) {
                send(queue.dequeue())
            }
        }
    }
        /*
    }
    catch {
        case e: IOException =>
            e.printStackTrace()
    }
    */
}

// vim: set ts=4 sw=4 et:
