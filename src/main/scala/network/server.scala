package network

import java.io._
import java.net._

/** The UdpServer represents a UDP server instance.
 @param port The port on which the server listens for packets.
 */
class UdpServer(port: Int)
{
    /** The size of the reception buffer. 1024 is plenty */
    private val BUFFER_SIZE = 1024

    /** The socket used to send and receive packets */
    private val socket = new DatagramSocket(port)

    /** The buffer that contains the packet data */
    private val buffer = new Array[Byte](BUFFER_SIZE)

    /** An object representing a received packet */
    private val packet = new DatagramPacket(buffer, buffer.length)

    /** Run the server and handle incoming packets */
    def run() : Unit = {
        socket.receive(packet)
        val message   = new String(packet.getData())
        val ipAddress = packet.getAddress().toString
        println("Received " + message + " from " + ipAddress)
    }
}
