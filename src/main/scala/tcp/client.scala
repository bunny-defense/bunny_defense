package tcp_server

import java.net.{InetAddress, Socket }
import java.io._

import scala.io._

import runtime.Controller

class Client(socket : Socket ) {

    try {
        val in = new ObjectInputStream(s.getInputStream())
        val out = new ObjectOutputStream(
            new DataOutputStream(s.getOutputStream()))

        out.writeObject("New Client")
        out.flush()

    }
    catch {
        case e: IOException =>
            e.printStackTrace()
    }

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
        s.close()
    }

}

// vim: set ts=4 sw=4 et:
