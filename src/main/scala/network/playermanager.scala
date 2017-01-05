package network

import collection.mutable.HashMap
import java.net._

/** The PlayerManager associates player ids with IP addresses */
class PlayerManager
{
    /** Maps player address to player id
     */
    private val playerMap = new HashMap[InetAddress, Int]

    /** Incremented for each new player id, this way all player ids are unique
     */
    private var playerIdCounter = 0

    /** Generates a new player id
     @return A unique player id
     */
    private def newPlayerId() : Int = {
        val newId = playerIdCounter
        playerIdCounter += 1;
        return newId
    }

    /** Adds a player to the list.
     @param addr The address of the player
     */
    def addPlayer(addr: InetAddress) : Unit = {
        if(!playerMap.contains(addr))
        {
            val id = newPlayerId()
            playerMap += ((addr, id))
        }
    }

    /** Gets the id of a player from its address
     @param addr The address of the player whose id will be returned
     @return Some int if player exists, None otherwise
     */
    def getFromAddr(addr: InetAddress) : Option[Int] = {
        return playerMap.get(addr)
    }

    /** Gets the address of a player from its id
     @param id The id of the player whose address will be returned
     @return Some InetAddres if the player exists, None otherwise
     */
    def getFromId(id: Int) : Option[InetAddress] = {
        def match_id(pair: (InetAddress,Int)) : Boolean = {
            val (_addr,_id) = pair
            return _id == id
        }
        val pair = playerMap.find(match_id)
        return pair match {
            case Some ((_addr,_)) => Some(_addr)
            case None       => None
        }
    }
}
