package runtime

import swing.event._

import game_mechanics.path._
import game_mechanics.tower._
import game_mechanics._

import gui._
import gui.animations._

abstract class GuiGameState(
    _player : Player,
    map: Array[Array[Boolean]])
extends GameState(map) {

    val player = _player
    val multiplayer: Boolean
    var selected_tower          : Option[TowerType] = None
    var acceleration = 1
    /* The tower currently selected */
    private var _selected_cell  : Option[Tower]     = None

    /* selected_cell GETTER */
    def selected_cell_=(tower: Option[Tower]): Unit =
    {
        if (tower != None)
            publish(SelectedCell)
        else
            publish(NoSelectedCell)
        _selected_cell = tower
    }

    /* selected_cell SETTER */
    def selected_cell = _selected_cell

    /* ==================== GUI ==================== */
    /* Creates the gui components */
    override val gui = new TDComponent(None)
    {
        override def toString : String = "gui"
    }
    val map_panel   = new MapPanel(Some(gui), this)
    {
        override def toString : String = "map_panel"
    }
    val build_menu  = new BuildMenu(Some(gui), this, 4, 4 )
    {
        pos = new CellPos( map_panel.size.x, InfoPanel.default_size.y )
        override def toString : String = "build_menu"
    }
    val info_panel  = new InfoPanel(Some(gui), this)
    {
        size = new CellPos( build_menu.size.x, size.y )
        pos  = new CellPos( map_panel.size.x, 0 )
    }
    val tower_panel = new TowerInfoPanel(Some(gui), this)
    {
        size = new CellPos(
          map_panel.size.x,
          TowerDefense.gui_size.height - map_panel.size.y
        )
        pos  = new CellPos( 0, map_panel.size.y )
    }
    /* Scrolls the map view */
    def scroll(dt: Double): Unit = {
        val scroll_speed = 512
        /* Handling input */
        if( TowerDefense.keymap(Key.J) )
        {
            val scroll_distance = Math.min(
                map_panel.rows * MapPanel.cellsize -
                    map_panel.size.y,
                map_panel.viewpos.y + dt * scroll_speed )
            map_panel.viewpos =
                new Waypoint(0, scroll_distance)
        }
        if( TowerDefense.keymap(Key.K) )
        {
            val scroll_distance = Math.max( 0,
                map_panel.viewpos.y - dt * scroll_speed )
            map_panel.viewpos =
                new Waypoint(0, scroll_distance)
        }
        if( TowerDefense.keymap(Key.H) )
        {
            val scroll_distance = Math.max( 0,
                map_panel.viewpos.x - dt * scroll_speed )
            map_panel.viewpos =
                new Waypoint(scroll_distance, 0)
        }
        if( TowerDefense.keymap(Key.L) )
        {
            val scroll_distance = Math.min(
                map_panel.cols * MapPanel.cellsize -
                    map_panel.size.x,
                map_panel.viewpos.x + dt * scroll_speed )
            map_panel.viewpos =
                new Waypoint(scroll_distance, 0)
        }
    }
    def update_gui(dt: Double) : Unit = {
        /* Scroll the map view */
        scroll(dt)
        /* Update animations */
        animations.foreach( _.update(dt) )
    }
    override def update(dt: Double) : Unit = {
        update_gui(dt)
        super.update(dt)
        if (TowerDefense.keymap(Key.Escape)) {
          tower_panel.update_selection(false)
        }
    }

    def new_tower_strategy(tower : TowerType , pos: CellPos) : Unit = {}
}
