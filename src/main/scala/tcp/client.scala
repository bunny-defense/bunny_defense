package tcp

import java.net.{InetAddress, Socket }
import java.io._

import scala.io._

import runtime.TowerDefense.gamestate

class Client(domain : String) extends Thread("Client Thread"){

    try {
        val socket = new Socket(InetAddress.getByName(domain),9999)
        val in = new ObjectInputStream(
            new DataInputStream(socket.getInputStream()))
        val out = new ObjectOutputStream(
            new DataOutputStream(socket.getOutputStream()))

        out.writeObject("New Client")
        out.flush()


        def send(arg : Any) : Unit = {
            out.writeObject(arg)
            out.flush()
        }

        def receive() : Any = {
            return in.readObject()
        }


        def close() = {
            out.close()
            in.close()
            socket.close()
        }
    }
    catch {
        case e: IOException =>
            e.printStackTrace()
    }
}

// vim: set ts=4 sw=4 et:
