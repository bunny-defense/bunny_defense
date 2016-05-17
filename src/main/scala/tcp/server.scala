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

object Server
{
    val default_port = 1234
}
abstract class Server extends Thread("AcceptanceThread") {
    import Server._
    val peers = new ListBuffer[ServerThread]
    def on_connect(peer: ServerThread) : Unit
    def on_disconnect(peer: ServerThread) : Unit
    override def run() : Unit = {
        try {
            val listener = new ServerSocket(default_port)
            while (true)
            {
                val new_peer = new ServerThread(listener.accept())
                {
                    override def on_connect(peer: ServerThread) : Unit = {
                        on_connect(peer)
                    }
                    override def on_disconnect(peer: ServerThread) : Unit = {
                        on_disconnect(peer)
                    }
                }
                peers += new_peer
                new_peer.start()
            }
            listener.close()
        }
        catch {
            case e: IOException =>
                System.err.println("Could not listen on port: 9999.")
                System.exit(1)
        }
    }
    def send(peer: ServerThread, packet: Any) : Unit = {
        peer.add(packet)
    }
    def broadcast(packet: Any) : Unit = {
        peers.foreach( _.add(packet) )
    }
    this.start()
}

abstract class ServerThread(socket : Socket)
extends Thread("ServerThread")
{
    val out = new ObjectOutputStream(
        new DataOutputStream(socket.getOutputStream()))
    val in  = new ObjectInputStream(
        new DataInputStream(socket.getInputStream()))
    val queue = new Queue[Any]()
    var running = true
    var player_name = "Unnamed"

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
    var handle : (ServerThread,Any) => Unit = { (peer, packet) =>
        println( packet )
        packet match {
            case ("player_info",name: String) => {
                println( "His name is " + name )
                player_name = name
                on_connect(this)
            }
            case _ => ()
        }
    }
    def receive() : Any = {
        handle(this, in.readObject())
    }
    class Receiver extends Thread("ClientReceiver")
    {
        override def run() : Unit = {
            try
            {
                while(true)
                {
                    receive()
                }
            }
            catch
            {
                case _: java.io.EOFException =>
                    running = false
                case e: Exception =>
                    StateManager.set_state( new ErrorMenuState( e.toString,
                        MultiplayerMenuState ) )
            }
        }
    }
    def on_connect(peer: ServerThread) : Unit
    def on_disconnect(peer: ServerThread) : Unit
    override def run(): Unit = {
        new Receiver().start()
        while (running) {
            if (!queue.isEmpty) {
                send(queue.dequeue())
            }
        }
        println( "Client disconnected" )
        on_disconnect(this)
    }

        /*
    } catch {
        case e: SocketException =>
            println(e.toString)
        case e: IOException =>
            e.printStackTrace();
    }
    */
}
// vim: set ts=4 sw=4 et:
