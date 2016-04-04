
package gui.animations

import game_mechanics.tower.TowerType
import gui._

/* This is the panel that indicates when a tower has been unlocked */

class UnlockAnimation(towertype : TowerType)
extends SlidingAnimation( () => "Unlocked " + towertype.name )
