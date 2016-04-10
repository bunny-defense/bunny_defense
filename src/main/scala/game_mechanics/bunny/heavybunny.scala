
package game_mechanics.bunny


/* Large and tough but slow bunny */
class HeavyBunny extends Bunny
{
    override val initial_hp  = 20.0
    override val base_shield = 1.5
    shield                   = 1.5
    override val base_speed  = 1.0
    speed                    = 1.0
    override def reward      = atan_variation(10,2,10)
}
