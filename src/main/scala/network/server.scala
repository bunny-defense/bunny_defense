package network

import java.io._
import java.net._

class UdpServer(port: Int)
{
    val BUFFER_SIZE = 1024

    val socket = new DatagramSocket(port)
    val buffer = new Array[Byte](BUFFER_SIZE)
    val packet = new DatagramPacket(buffer, buffer.length)

    def run() : Unit = {
        socket.receive(packet)
        val message   = new String(packet.getData())
        val ipAddress = packet.getAddress().toString
        println("Received " + message + " from " + ipAddress)
    }
}
