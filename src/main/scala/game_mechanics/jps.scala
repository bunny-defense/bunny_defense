
package game_mechanics

import game_mechanics.path._
import runtime._
import Math._

import collection.mutable.{ListBuffer,ListMap,Queue}



class CellPosed(cell_init: CellPos, dir_init : (Int,Int)) {
    /**
     * Jump point, it is a cell with a possible parent
     * @param cell: the cell
     * @param parent: the parent or nothing
     */
    var cell = cell_init
    var dir  = dir_init
    var parent: Option[CellPosed] = None

    def set_parent(father : CellPosed) : Unit = {
        assert (this.parent.isEmpty, {
            this.parent = Some(father)
        }
    )
    }
}

/* TODO Debugging >< */

class JPS(start: CellPos, objectif: CellPos) {
  val vert_dist = 1.0
  val hor_dist  = 1.0
  val diag_dist = Math.sqrt(2)
  val all_list: ListMap[CellPosed,Double] = new ListMap()
  val queue : Queue[(Double,CellPosed,Double)]


  for (dx <- Iterator(0,1)) {
      for (dy <- Iterator(-1,0,1)) {
          if ( dy != 0) {
              this.add_node(this.start.x,this.start.y,Some((dx,dy)),0)
          }
      }
  }

  def estimate(x: Int, y: Int, dir: Option[(Int,Int)]) : Double = {
      var xx = x
      var yy = y
      var add = 0
      if ((dir.isEmpty)||(dir==(0,0))) {
          add = 0
      }
      else {
          add = 7
          xx = xx+dir.get._1
          yy = yy+dir.get._2
      }
      val dx = Math.abs(xx - this.objectif.x);
      val dy = Math.abs(yy - this.objectif.y);
      val mini =min(dx,dy);
      return add + 7*mini + 5* ((dx-mini) + (dy-mini))
  }


  def add_node(x: Int, y: Int, dir: Option[(Int,Int)], dist: Double): CellPosed = {
      val pd = new CellPosed(new CellPos(x,y),dir.get)
      val current  = this.all_list.get(pd)
      if ( current.isEmpty || current.get > dist) {
          val total = dist + this.estimate(pd.cell.x, pd.cell.y,dir)
          this.all_list.update(pd,dist)
          this.add_open(total,pd,dist)
      }
      return pd
  }

  def get_closed_node(x: Int, y: Int, dir: (Int,Int), dist: Double) : CellPosed = {
      val pd = new CellPosed(new CellPos(x,y),dir)
      val current = this.all_list.get(pd)
      if (!current.isEmpty && current.get <= dist) {
          return(pd)
      }
      this.all_list.update(pd,dist)
      return pd
  }

 def add_open(total: Double, pd: CellPosed, dist: Double): Unit = {
      this.queue += (new Tuple3(total,pd,dist))
  }

 def get_open() : Option[(Double,CellPosed,Double)] = {
     while (true){
         if (this.queue.isEmpty) {
             return (None)
         }
         val (total,pd,dist) = this.queue.dequeue
         val current = this.all_list.get(pd)
         if (dist == current.get) {
             return Some((total,pd,dist))
         }
         return(None)
     }
 }


  def horizontal_search(
    pos: CellPosed,
    hor_dir: Int,
    dist: Double): ListBuffer[CellPosed] =
  {
    /** Manages the horizontal search of jump points
    *  @param pos: current position
    *  @param hor_dir : Horizontal direction (+/- 1)
    *  @param dist : Distance traveled so far
    */
    var x0 = pos.cell.x
    var y0 = pos.cell.y
    while (true) {
      var x1 = pos.cell.x + hor_dir
      /* The cell is not on the map */
      if (!TowerDefense.map_panel.map.on_map(x1,y0))
        return (new ListBuffer[CellPosed]())
      /* The cell is obstructed */
      if (TowerDefense.map_panel.map.obstructed(x1,y0))
        return (new ListBuffer[CellPosed]())
      /* The cell is the core objective, we return the last point of
      * the path */
      if ((new CellPos(x1,y0)) == objectif ) {
        return (new ListBuffer[CellPosed]())
            (this.add_node(x1, y0, None, dist+hor_dist))
      }

      /*We have an open space in (x1,y0) */
      dist += hor_dist
      var x2 = x1+ hor_dir

      var nodes = new ListBuffer[CellPosed]()

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
    return (new ListBuffer[CellPosed]()p)
  }
  /* Same but for vertical search */
  def vert_search(
    pos: CellPosed,
    vert_dir: Int,
    dist_init: Double): ListBuffer[CellPosed] =
  {
    /** Manages the horizontal search of jump points
    *  @param pos: current position
    *  @param vert_dir : Horizontal direction (+/- 1)
    *  @param dist : Distance traveled so far
    */
    var x0 = pos.cell.x
    var y0 = pos.cell.y
    var dist = dist_init
    while (true) {
      var y1 = pos.cell.y + vert_dir
      /* The cell is not on the map */
      if (!TowerDefense.map_panel.map.on_map(x0,y1))
        return (new ListBuffer[CellPosed]())
      /* The cell is obstructed */
      if (TowerDefense.map_panel.map.obstructed(x0,y1))
        return (new ListBuffer[CellPosed]())
      /* The cell is the core objective, we return the last point of
      * the path */
      if ((new CellPos(x0,y1))== objectif ) {
        return (new ListBuffer[CellPosed]
                (this.add_node(x0, y1, None, dist + vert_dist)))
      return (new ListBuffer[CellPosed]())
      }

      /*We have an open space in (x0,y1) */
      dist = dist + vert_dist
      var y2 = y1 + vert_dir

      var nodes = new ListBuffer[CellPosed]()

      /* Choose the nodes to explore */
      if (TowerDefense.map_panel.map.obstructed(x0-1,y1) &&
        !TowerDefense.map_panel.map.obstructed(x0-1,y2)) {
        nodes += this.add_node(x0, y1, Some(vert_dir,-1), dist)
      }

      if (TowerDefense.map_panel.map.obstructed(x0+1,y1) &&
        !TowerDefense.map_panel.map.obstructed(x0+1,y2)) {
        nodes += this.add_node(x0,y1,Some(vert_dir,1),dist)
      }

      if (!nodes.isEmpty) {
        nodes += this.add_node(x0,y1,Some(vert_dir,0),dist)
        return nodes
      }

      y0 = y1
    }
  }

  def diag_search(
    pos: CellPosed,
    hor_dir: Int,
    vert_dir: Int,
    dist_dist: Double): ListBuffer[CellPosed] =
  /** Manages the diagonal path search
  *  @param pos: Start position
  *  @param hor_dir: horizontal direction (+/- 1)
  *  @param vert_dir: vertical direction (+/- 1)
  *  @param dist : distance traveled so far
  */
  {
    val x0 = pos.x
    val y0 = pos.y
    var dist = dist_init
    while (true) {
      var x1 = x0 + hor_dir
      var y1 = y0 + vert_dir
      if (!TowerDefense.map_panel.map.on_map(x1,y1))
        return (new ListBuffer[CellPosed]())
      /* The cell is obstructed */
      if (TowerDefense.map_panel.map.obstructed(x1,y1))
        return (new ListBuffer[CellPosed]())
      /* The cell is the core objective, we return the last point of
      * the path */
      if (new CellPos(x1,y1) == this.objectif) {
        return (new ListBuffer[CellPosed]
            (add_node(x1,y1, None, dist + diag_dist)))
        /* There is open space at (x1,y1) */

        dist  = dist + diag_dist
        var x2 = x1 + hor_dir
        var y2 = y1 + vert_dir
        var nodes: ListBuffer[CellPos] = new ListBuffer()

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
        }

          if (nodes.isEmpty) {
            val sub_nodes = this.vert_search(CellPos(x1,y1), vert_dist, dist)
            var vert_done = true

            if (!sub_nodes.isEmpty) {
              parent_node = this.get_closed_node(x1,y1,(0,vert_dir),dist)
              for (snode <- sub_nodes) {
                snode.set_parent(parent_node)
              }
              nodes += parent_node
            }
          }

            if (!nodes.isEmpty) {
              if (!hor_done) {
                nodes += this.add_node(x1, y1, (hor_dir, 0), dist)
              }
              if (!vert_node) {
                  nodes += this.add_node(x1, y1, (0, vert_dir), dist)
              }
              nodes += this.add_node(x1, y1, (hor_dir, vert_dir), dist)
              return nodes
            }


      x0 = x1
      y0 = y1
      }
    }
  }

  def step(dist : Double, elem: CellPosed ): Option[CellPosed] = {
      if ((elem.cell.x, elem.cell.y) == (this.objectif.x,this.objectif.y)) {
          return elm
      }

      val hor_dir  = elm.dir._1
      val vert_dir = elm.dir._2

      if (hor_dir != 0 && vert_dir != 0) {
          nodes = this.diag_search(elm, hor_dir, vert_dir, dist)
      }

      else if (vert_dir != 0) {
          nodes = this.vert_search(elm,vert_dir, dist)
      }
      else {
          assert(hor_dir != 0, {
          nodes = this.hor_search(elm,hor_dir, dist)
          }
          )
      }

      for (node <- nodes) {
          node.set_parent(elm)
      }

      return None
  }

    def run() : ListBuffer[CellPosed] = {
        val hammer = new Breaks
        import hammer.{break,breakable}
        breakable {
            while (true) {
                val (total, pd, dist) = this.get_open()
                    if (total.isEmpty) {
                        break()
                    }

                    pd = this.step(dist, pd)
                    if (pd.isEmpty) {
                        break()
                    }
            }
            var open_count = 0
            while (true) {
                val total,pd,dist = this.get_open()
                if (total.isEmpty) {
                    break()
                }
                open_count += 1
            }
        }
        return this.all_list
    }
}
