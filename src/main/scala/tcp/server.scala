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
    val peers = new ListBuffer[ServerThread]
    try {
        val listener = new ServerSocket(9999)
        while (true) { val new_peer = new ServerThread(listener.accept())
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
    def send(peer: ServerThread, packet: Any) : Unit = {
        peer.add(packet)
    }
    def broadcast(packet: Any) : Unit = {
        peers.foreach( _.add(packet) )
    }
}

class ServerThread(socket : Socket)
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

    override def run(): Unit = {
        while (true) {
            if (!queue.isEmpty) {
                send(queue.dequeue())
            }
        }
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
