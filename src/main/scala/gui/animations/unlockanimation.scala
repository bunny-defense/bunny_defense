
package gui.animations

import runtime.GuiGameState
import game_mechanics.tower.TowerType
import gui._

/* This is the panel that indicates when a tower has been unlocked */

class UnlockAnimation(towertype: TowerType, gamestate: GuiGameState)
extends SlidingAnimation(() => "Unlocked " + towertype.name, gamestate)
