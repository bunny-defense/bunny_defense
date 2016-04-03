
package gui

import game_mechanics.tower.TowerType

class UnlockAnimation(towertype : TowerType)
extends SlidingAnimation( () => "Unlocked " + towertype.name )
