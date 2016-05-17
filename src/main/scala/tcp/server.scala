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
                    override def on_disconnect(peer: ServerThread) : Unit = {
                        on_disconnect(peer)
                    }
                }
                peers += new_peer
                on_connect(new_peer)
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

    var handle : (ServerThread,Any) => Unit = {
        (peer, packer) => ()
    }

    def receive() : Any = {
        handle(this, in.readObject())
    }

    def on_disconnect(peer: ServerThread) : Unit

    override def run(): Unit = {
        send(("player_name","Gus"))
        while (true) {
            if (!queue.isEmpty) {
                send(queue.dequeue())
            }
        }
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
