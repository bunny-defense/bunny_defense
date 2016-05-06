package tcp_server

import java.net.{InetAddress, Socket }
import java.io._

import scala.io._

object Client {
    def main(args : Array[String]) : Unit = {
        try {
            val s = new Socket(InetAddress.getByName("localhost"),9999)
            val in = new DataInputStream(s.getInputStream())
            val out = new ObjectOutputStream(
                new DataOutputStream(s.getOutputStream()))


            out.writeObject("Hello Server")
            out.flush()

            while (true) {
                val x = in.readChar()
                println("Received: " + x)
            }

            out.close()
            in.close()
            s.close()
        }
        catch {
            case e: IOException =>
                e.printStackTrace()
        }
    }
}

// vim: set ts=4 sw=4 et:
