
package game_mechanics

import game_mechanics.path._

import collection.mutable.ListBuffer


/* TODO List: add_node, get_closed_node, parent_node */

class JPS(start: CellPos, objectif: CellPos) {
  val vert_dist = 1
  val hor_dist  = 1
  val diag_dist = (CellPos(1,1) - CellPos(0,0)).norm
  val start     = start
  val objectif  = objectif

  val horizontal_search(
    pos: CellPos,
    hor_dir: Int,
    dist: Double): ListBuffer[CellPos] =
  {
    /** Manages the horizontal search of jump points
    *  @param pos: current position
    *  @param hor_dir : Horizontal direction (+/- 1)
    *  @param dist : Distance traveled so far
    */
    val x0 = pos.x
    val y0 = pos.y
    while (true) {
      var x1 = pos.x + hor_dir
      /* The cell is not on the map */
      if (!TowerDefense.map_panel.map.on_map(x1,y0))
        return ([]: ListBuffer[CellPos])
      /* The cell is obstructed */
      if (TowerDefense.map_panel.map.obstructed(x1,y0))
        return ([]: ListBuffer[CellPos])
      /* The cell is the core objective, we return the last point of
      * the path */
      if ( CellPos(x1,y0) == objectif ) {
        return ([this.add_node(x1, y0, None, dist+hor_dist)]: ListBuffer[CellPos])
      }

      /*We have an open space in (x1,y0) */
      dist += hor_dist
      var x2 = x1+ hor_dir

      var nodes: ListBuffer[CellPos] = []

      /* Choose the nodes to explore */
      if (TowerDefense.map_panel.map.obstructed(x1,y0-1) &&
        !TowerDefense.map_panel.map.obstructed(x2,y0-1)) {
        nodes += this.add_node(x1, y0, Some(hor_dir,-1), dist)
      }

      if (TowerDefense.map_panel.map.obstructed(x1,y0+1) &&
        !TowerDefense.map_panel.map.obstructed(x2,y0+1)) {
        nodes += this.add_node(x1, y0, Some(hor_dir,1), dist)
      }

      if (!nodes.isEmpty) {
        nodes += this.add_node(x1, y0, Some(hor_dir,0), dist)
        return nodes
      }

      x0 = x1
    }
  }
  /* Same but for vertical search */
  val vertical_search(
    pos: CellPos,
    vert_dir: Int,
    dist: Double): ListBuffer[CellPos] =
  {
    /** Manages the horizontal search of jump points
    *  @param pos: current position
    *  @param vert_dir : Horizontal direction (+/- 1)
    *  @param dist : Distance traveled so far
    */
    val x0 = pos.x
    val y0 = pos.y
    while (true) {
      var y1 = pos.y + vert_dir
      /* The cell is not on the map */
      if (!TowerDefense.map_panel.map.on_map(x0,y1))
        return ([]: ListBuffer[CellPos])
      /* The cell is obstructed */
      if (TowerDefense.map_panel.map.obstructed(x0,y1))
        return []
      /* The cell is the core objective, we return the last point of
      * the path */
      if ( CellPos(x0,y1) == objectif ) {
        return [this.add_node(x0, y1, None, dist + vert_dist)]
      }

      /*We have an open space in (x0,y1) */
      dist += vert_dist
      var y2 = y1+ vert_dir

      var nodes: ListBuffer[CellPos] = []

      /* Choose the nodes to explore */
      if (TowerDefense.map_panel.map.obstructed(x0-1,y1) &&
        !TowerDefense.map_panel.map.obstructed(x0-1,y2)) {
        nodes += this.add_node(x0, y1, Some(vert_dir,-1), dist)
      }

      if (TowerDefense.map_panel.map.obstructed(x0+1,y1) &&
        !TowerDefense.map_panel.map.obstructed(x0+1,y2)) {
        nodes += this.add_node(x0,y1,(vert_dir,1),dist)
      }

      if (!nodes.isEmpty) {
        nodes += this.add_node(x0,y1,(vert_dir,0),dist)
        return nodes
      }

      y0 = y1
    }
  }

  val diag_search(
    pos: CellPos,
    hor_dir: Int,
    vert_dir: Int,
    dist: Double): ListBuffer[CellPos] =
  /** Manages the diagonal path search
  *  @param pos: Start position
  *  @param hor_dir: horizontal direction (+/- 1)
  *  @param vert_dir: vertical direction (+/- 1)
  *  @param dist : distance traveled so far
  */
  {
    val x0 = pos.x
    val y0 = pos.y
    while (true) {
      var x1 = x0 + hor_dir
      var y1 = y0 + vert_dir
      if (!TowerDefense.map_panel.map.on_map(x1,y1))
        return ([]: ListBuffer[CellPos])
      /* The cell is obstructed */
      if (TowerDefense.map_panel.map.obstructed(x1,y1))
        return ([]: ListBuffer[CellPos])
      /* The cell is the core objective, we return the last point of
      * the path */
      if (CellPos(x1,y1) == this.objectif) {
        return ([add_node(x1,y1, None, dist + diag_dist)]:ListBuffer[CellPos])
        /* There is open space at (x1,y1) */

        dist  += diag_dist
        var x2 = x1 + hor_dir
        var y2 = y2 + vert_dir
        var nodes: ListBuffer[CellPos] = []

        if (TowerDefense.map_panel.map.obstructed(x0,y1) &&
          !TowerDefense.map_panel.map.obstructed(x0,y2)) {
          nodes += add_node(x1, y1, (-hor_dir, vert_dir), dist)
        }

        if (TowerDefense.map_panel.map.obstructed(x1,y0) &&
          !TowerDefense.map_panel.map.obstructed(x2,y0)) {
          nodes += add_node(x1, y1, (hor_dir, -vert_dir), dist)
        }

        var hor_done = false
        var vert_done = false

        if (nodes.isEmpty) {
          val sub_nodes = this.horizontal_search(CellPos(x1,y1), hor_dir, dist)
          hor_done = true

          if (!sub_nodes.isEmpty) {
            /* Horizontal search ended with some results */
            parent_node = this.get_closed_node(x1,y1,(hor_dir, 0), dist)
            for (snode <- sub_nodes) {
              snode.set_parent(parent_node)
            }
            nodes += parent_node
          }

          if (nodes.isEmpty) {
            val sub_nodes = this.vertical_search(CellPos(x1,y1), vert_dist, dist)
            var vert_done = true

            if (!sub_nodes.isEmpty) {
              parent_node = this.get_closed_node(x1,y1,(0,vert_dir),dist)
              for (snode <- sub_nodes) {
                snode.set_parent(parent_node)
              }
              nodes += parent_node
            }

            if (!nodes.isEmpty) {
              if (!hor_done) {
                nodes += add_node(x1, y1, (hor_dir, 0), dist))
            }
            if (!vert_node) {
              nodes += add_node(x1, y1, (0, vert_dir), dist))
          }
          nodes += add_node(x1, y1, (hor_dir, vert_dir), dist))
        return nodes
      }

      x0, y0 = x1, y1
    }

