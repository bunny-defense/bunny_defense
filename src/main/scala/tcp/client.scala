package tcp

import java.net.{InetAddress, Socket }
import java.io._

import scala.io._


import runtime._
import game_mechanics._
import game_mechanics.bunny._
import game_mechanics.tower._
import game_mechanics.utilitaries._
import game_mechanics.updatable._
import gui._
import gui.animations._

import scala.collection.mutable.{ListBuffer,Queue}

class ClientThread(domain : String) extends Thread("Client Thread"){

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
            in.readObject() match {
                case l:ListBuffer[Bunny] => TowerDefense.gamestate.bunnies = l
                case l:ListBuffer[Tower] => TowerDefense.gamestate.towers  = l
                case l:ListBuffer[Projectile] => TowerDefense.gamestate.projectiles = l
                case l:ListBuffer[Updatable]  => TowerDefense.gamestate.updatables = l
                case l:ListBuffer[Utilitaries] => TowerDefense.gamestate.utilitaries = l
                case (l: String, (x:Int, y:Int), id: Int) => if (("(T|t)ower".r findAllIn l) != None) {
                    TowerDefense.gamestate.tower += new Tower(Class.forName(l), new CellPos(x,y),id)
                }
            }
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
