
package game_mechanics.bunny


/* Large and tough but slow bunny */
case class HeavyBunny(player_id: Int, bunny_id: Int) extends Bunny
{
    override val id          = bunny_id
    override val player      = player_id
    override val initial_hp  = 20.0
    override val base_shield = 1.5
    shield                   = 1.5
    override val base_speed  = 1.0
    speed                    = 1.0
    override val price       = 15
    override def reward      = atan_variation(10,2,10)
}
