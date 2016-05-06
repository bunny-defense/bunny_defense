package tcp_server


import java.net._
import java.io._
import scala.io._


class Server extends Thread {
    override def run() : Unit = {
        val server = new ServerSocket(8007)
        while (true) {
            val s =  server.accept()
            val in = new BufferedSource(s.getInputStream()).getLines()
            val out = new PrintStream(s.getOutputStream())

            out.println(in.next())
            out.flush()
            s.close()
        }
    }
}
// vim: set ts=4 sw=4 et:
