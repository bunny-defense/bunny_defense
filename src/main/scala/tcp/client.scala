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

class ClientThread(domain : String)
extends Thread("Client Thread")
{
    val socket = new Socket(InetAddress.getByName(domain),Server.default_port)
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

    var handle : Any => Unit = { packet => () }

    def receive() : Unit = {
        handle(in.readObject())
    }

    def close() = {
        out.close()
        in.close()
        socket.close()
    }

    override def run(): Unit = {
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
