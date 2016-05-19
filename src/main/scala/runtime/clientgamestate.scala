
package runtime

import collection.mutable.ListBuffer
import collection.parallel._

import swing.event._

import game_mechanics._
import game_mechanics.tower._
import game_mechanics.bunny._
import game_mechanics.path._
import gui._
import gui.animations._
import tcp._
import tcp.packets._

class ClientGameState(
    _player: Player,
    players: ListBuffer[Player],
    map: Array[Array[Boolean]],
    _server: ClientThread)
extends GameState(map)
{
    /* The server this client is connected to */
    val server = _server
    /* The player associated to this client */
    val player = _player
    /* The tower type selected for construction */
    var selected_tower          : Option[TowerType] = None
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
        size = new CellPos( map_panel.size.x, size.y )
        pos  = new CellPos( 0, map_panel.size.y )
    }
    /* Scrolls the map view */
    def scroll(dt: Double): Unit = {
        val scroll_speed = 128
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
            selected_cell  = None
            selected_tower = None
        }
    }

    /* ==================== SERVER COM ==================== */

    val handle : Any => Unit = {
        packet => {
            println(packet)
            packet match {
                case PlacedTower(towertype, pos, player_id) =>
                    val t = TowerType.deserialize(towertype)
                    this += new Tower(
                        players(player_id),
                        t, pos, this)
                case ("sync_bunnies", l: ListBuffer[Bunny]) => {
                    for (bunny <- l) {
                        val bunch = bunnies.find(
                            x => x.id == bunny.id && x.owner == bunny.owner)
                        if (bunch == None)
                        {
                            this += bunny
                        }
                        else
                        {
                            this -= bunch.get
                            this += bunny
                        }
                    }
                    for (bunny <- bunnies.filter(x=> (l.filter(
                        y => y.id == x.id && y.owner == y.owner)).isEmpty)) {
                        this -= bunny
                    }
                }
                case ("sync_towers", l: ListBuffer[Tower]) => {
                    for (tower <- l) {
                        val bunch = towers.find(
                            x => x.owner == tower.owner && x.id == tower.id)
                        if (bunch == None)
                        {
                            this += tower
                        }
                        else
                        {
                            this -= bunch.get
                            this += tower
                        }
                    }
                    for (tower <- towers.filter(x=> (l.filter(
                        y => y.id == x.id && y.owner == y.owner)).isEmpty)) {
                        this -= tower
                    }
                }
                case ("jumped", x: Int, y: Int, p: Waypoint) => {
                    val obunny = bunnies.find(t => ((t.owner == y )&&(t.id == x)))
                    if (!obunny.isEmpty) {
                        val bunny = obunny.get
                        this -= bunny
                        val anim = new SmokeAnimation(bunny.pos, this)
                        anim and_then { () =>
                            bunny.pos = p
                            this += bunny
                            this += new SmokeAnimation(bunny.pos, this)
                        }
                        this += anim
                    }
                }
                case ("removed", d: Int, p: Int) => {
                    val toRemove = bunnies.find(
                        x => (x.id == d) && (x.owner == p))
                    if (!toRemove.isEmpty) {
                        bunnies -= toRemove.get
                    }
                }
                case ("lost", d: Int, pid: Int) => {
                    players(pid).remove_hp(d)
                }
            }
        }
    }
    server.handle = this.handle
    /* ==================== STRATEGIES ==================== */

    // BUNNIES
    override def bunny_death_render_strategy(bunny: Bunny) : Unit = {
        this += new GoldAnimation(
            bunny.reward(this.wave_counter),
            bunny.pos.clone(),
            this)
    }
    override def bunny_reach_goal_strategy(bunny: Bunny) : Unit = {}
    override def spec_ops_jump_strategy(bunny: SpecOpBunny) : Unit = {}

    // PROJECTILES
    override def splash_projectile_hit_strategy(
        projectile: SplashProjectile) : Unit = {
            for (dir <- 0 to 12) {
                this += new SpreadAnimation(
                    projectile.target_pos,
                    projectile.radius,
                    new Waypoint (Math.cos(dir.toDouble*360.0/12.0),
                                  Math.sin(dir.toDouble*360.0/12.0)),
                    this
                )
            }
    }

    // TOWER
    override  def tower_fire_strategy(tower: Tower) : Unit = {
        this += new MuzzleflashAnimation(tower.pos.toDouble, this)
    }
    override  def supp_buff_tower_animation_strategy(tower: Tower) : Unit = {
        def new_buff_anim() : Unit = {
            val anim = new BuffAnimation( tower.pos, tower.range, this)
            anim and_then new_buff_anim
            this += anim
        }
        new_buff_anim
    }

    override  def supp_slow_tower_animation_strategy(tower: Tower) : Unit = {
        def new_snow_anim() : Unit = {
            val snow_anim = new SnowAnimation( tower.pos, tower.range, this)
            snow_anim and_then new_snow_anim
            this += snow_anim
        }
        new_snow_anim
    }
}
