package tcp

import java.net._
import java.io._
import scala.io._


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

        def close() : Unit = {
            out.close()
            in.close()
            socket.close()
        }

        def send(arg : Any): Unit = {
            out.writeObject(arg)
            out.flush()
        }

        def receive() : Any = {
            return in.readObject()
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
