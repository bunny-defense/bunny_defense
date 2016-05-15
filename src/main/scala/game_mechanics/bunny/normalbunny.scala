
package game_mechanics.bunny


class NormalBunny(player_id: Int, bunny_id: Int) extends Bunny
{
    override val id     = bunny_id
    override val player = player_id
}
