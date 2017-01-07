package manager

import collection.mutable.ListBuffer

import game_mechanics.bunny.Bunny

/** The BunnyManager manages a collection of bunnies and updates them
 */
trait BunnyManager
{
    /** Adds a bunny to the collection
     @param bunny to add to the collection
     */
    def addBunny(bunny: Bunny) : Unit
    /** Removes a bunny from the collection
     @param bunny to remove from the collection
     */
    def removeBunny(bunny: Bunny) : Unit
    /** Updates all bunnies
     @param dt delta time since last update
     */
    def update(dt: Double) : Unit
}

/** The ListBunnyManager is an implementation of BunnyManager backed by a
 mutable ListBuffer
 */
class ListBunnyManager
extends BunnyManager
{
    val bunnies = new ListBuffer[Bunny]()
    override def addBunny(bunny: Bunny) : Unit = {
        bunnies += bunny
    }
    override def removeBunny(bunny: Bunny) : Unit = {
        bunnies -= bunny
    }
    override def update(dt: Double) : Unit = {
        bunnies.foreach(_.update(dt))
    }
}
