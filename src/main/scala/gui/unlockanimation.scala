
package gui

import game_mechanics.tower.TowerType

/* This is the panel that indicates when a tower has been unlocked */

class UnlockAnimation(towertype : TowerType)
extends SlidingAnimation( () => "Unlocked " + towertype.name )
