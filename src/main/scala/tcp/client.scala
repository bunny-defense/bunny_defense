package tcp_server

import java.net.{InetAddress, Socket }
import java.io._

import scala.io._

class Client extends Thread {
    def main(args : Array[String]) : Unit = {
        val s = new Socket(InetAddress.getByName("localhost"),9999)
        lazy val in = new BufferedSource(s.getInputStream()).getLines()
        val out = new PrintStream(s.getOutputStream())

        out.println("Hello World")
        out.flush()
        println("Received: " + in.next())

        s.close()
    }
}

// vim: set ts=4 sw=4 et:
