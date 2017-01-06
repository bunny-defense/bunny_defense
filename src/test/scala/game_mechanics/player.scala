import org.scalatest.FlatSpec

import game_mechanics.Player
import game_mechanics.path.CellPos

class PlayerSpec extends FlatSpec
{
    "A Player" should "be created with an id, a name, some life, some gold, a killcount and a base cell." in
    {
        val player = new Player("TestPlayer")
        assertResult(0) {player.id}
        assertResult("TestPlayer") {player.name}
        assertResult(10) {player.hp}
        assertResult(150) {player.gold}
        assertResult(0) {player.killcount}
        assert (new CellPos(0, 0) == player.base)
    }

    it should "have its life and gold resetted when asked" in
    {
      val player = new Player("TestPlayer")
      player.hp= 15
      player.gold = 1337
      player.reset()
      assertResult(10) {player.hp}
      assertResult(150) {player.gold}
    }

    it should "lose life when removing hp" in
    {
      val player = new Player("TestPlayer")
      player.remove_hp(5)
      assertResult(5) {player.hp}
    }

    it should "gain life when adding hp" in
    {
      val player = new Player("TestPlayer")
      player.add_hp(5)
      assertResult(15) {player.hp}
    }

    it should "lose gold when removing gold when it has enough gold" in
    {
      val player = new Player("TestPlayer")
      player.remove_gold(5)
      assertResult(145) {player.gold}
    }

    it should "gain gold when adding gold" in
    {
      val player = new Player("TestPlayer")
      player.add_gold(5)
      assertResult(155) {player.gold}
    }

    it should "not lose gold if it hasn't enough money" in
    {
      val player = new Player("TestPlayer")
      player.remove_gold(1337)
      assertResult(150) {player.gold}
    }

    it should "have its base changed when setting a new base" in
    {
      val player = new Player("TestPlayer")
      player.set_base(42,42)
      assert (new CellPos(42, 42) == player.base)
    }

    "ID counter" should "be incremented by one when a player is created" in
    {
      val counter = Player.id_counter
      val player = new Player("TestPlayer")
      assertResult(1) { Player.id_counter - counter}
    }
}
