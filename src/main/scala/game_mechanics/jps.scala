
package game_mechanics

/* Game imports */
import game_mechanics.path._
import runtime._

/* Scala imports */
import Math._
import scala.util.control.Breaks._
import collection.mutable.{ListBuffer,ListMap,PriorityQueue, Stack}
/* Needed for PriorityQueue */
import scala.math.Ordering.Implicits._


class CellPosed(cell_init: CellPos, dir_init : (Int,Int))
{
    /**
     * Jump point, it is a cell with a possible parent and a direction
     * @param cell: the cell
     * @param parent: the parent or nothing
     * @param dir: the direction of the next jump point
     */
    var cell = cell_init
    var dir  = dir_init
    var parent: Option[CellPosed] = None

    def set_parent(father : CellPosed) : Unit = {
        if (this.parent.isEmpty) {
            this.parent = Some(father)
        }
    }

    def backtrace() : Path = {
        /** Find all the nodes between the node and its father */
        val buffer = new Path()
        if ( this.parent != None ) {
            var dir_path = new Waypoint (
                this.parent.get.dir._1,
                this.parent.get.dir._2)
            var current = this.cell.toDouble
            buffer.push(current)
            while (current != this.parent.get.cell.toDouble) {
                current -= dir_path
                buffer.push(current)
            }
        }
        return buffer
    }

    def <(other: CellPosed) : Boolean = {
        /** Compares two CellPosed. First the cell and then the direction */
        if (this.cell != other.cell) {
            return this.cell < other.cell
        }
        return (this.dir._1 < other.dir._1 ||
            (this.dir._1 == other.dir._1 && this.dir._2 < other.dir._2))
    }


    override def toString(): String = {
        return "(" + cell.x.toString + "," + cell.y.toString + ")," +
        (this.parent match
        {
            case None     => "np"
            case Some(cp) => "<-" + cp.cell.x.toString + "," + cp.cell.y.toString
        }) + "-(" + dir._1.toString + "," + dir._2.toString + ")"
    }

    override def equals(other: Any): Boolean = other match {
        /** Equality between two CellPosed */
        case that: CellPosed =>
            return this.cell == that.cell && this.dir == that.dir
        case _ => false
    }
}

class JPS(start: CellPos, objective: CellPos, gamestate: GameState)
{
    /**
     * The main class to calculating the JPS Algorithm
     * @param start: The starting CellPos
     * @param objective: The objective of the JPS Algorithm
     */

    val horvert_dist = 1.0
    val diag_dist = Math.sqrt(2)
    var all_list: ListMap[CellPosed,Double] = new ListMap()

    /*Priority queues in scala are shitty */
   var queue =
       new PriorityQueue[(Double,CellPosed,Double)]()(Ordering.by
       {case(d1,c,d2) => (-d1,((-c.cell.x,-c.cell.y),(-c.dir._1,-c.dir._2)),-d2)})

   this.add_node( this.start.x, this.start.y, Some((1, 0)), 0 )
   this.add_node( this.start.x, this.start.y, Some((1, 1)), 0 )
   this.add_node( this.start.x, this.start.y, Some((1,-1)), 0 )
   this.add_node( this.start.x, this.start.y, Some((0, 1)), 0 )
   this.add_node( this.start.x, this.start.y, Some((0,-1)), 0 )
   this.add_node( this.start.x, this.start.y, Some((-1,-1)), 0 )
   this.add_node( this.start.x, this.start.y, Some((-1,1)), 0 )
   this.add_node( this.start.x, this.start.y, Some((-1,0)), 0 )


   def estimate(x: Int, y: Int, dir: Option[(Int,Int)]) : Double = {
       var xx  = x + dir.get._1
       var yy  = y + dir.get._2
       var add = Math.sqrt(Math.pow(dir.get._1,2) + Math.pow(dir.get._2,2))
       val dx  = Math.abs(xx - this.objective.x)
       val dy  = Math.abs(yy - this.objective.y)
       return add + Math.sqrt( Math.pow(xx,2) + Math.pow(yy,2))
   }


   def add_node(
       x: Int, y: Int,
       dir: Option[(Int,Int)],
       dist: Double): CellPosed = {
           val pd = new CellPosed(new CellPos(x,y),dir.get)
           val current  = this.all_list.get(pd)
           println(current, dist,x,y)
           if ( current.isEmpty || current.get > dist) {
               val total = dist + this.estimate(pd.cell.x, pd.cell.y,dir)
               this.all_list.update(pd,dist)
               this.add_open(total,pd,dist)
           }
           return pd
   }

   def get_closed_node(
       x: Int, y: Int,
       dir: (Int,Int),
       dist: Double) : CellPosed = {
           val pd = new CellPosed(new CellPos(x,y),dir)
           val current = this.all_list.get(pd)
           if ((!current.isEmpty)&& current.get <= dist) {
               return(pd)
           }
           this.all_list.update(pd,dist)
           return pd
   }

    def add_open(total: Double, pd: CellPosed, dist: Double): Unit = {
        this.queue.enqueue(new Tuple3(total,pd,dist))
    }

    def get_open() : (Option[Double],Option[CellPosed],Option[Double]) = {
        while (true){
            if (this.queue.isEmpty) {
                return ((None,None,None))
            }
            val (total,pd,dist) = this.queue.dequeue
            val current = this.all_list.get(pd)
            if (dist == current.get) {
                println( "Trying with", total, pd, dist )
                return ((Some(total),Some(pd),Some(dist)))
            }
        }
        return((None,None,None))
    }

    def hor_search(
        pos: (Int,Int),
        hor_dir: Int,
        dist_init: Double): ListBuffer[CellPosed] =
    {
        /**
         * Manages the horizontal search of jump points
         * @param pos: current position
         * @param hor_dir : Horizontal direction (+/- 1)
         * @param dist : Distance traveled so far
         */
        var x0   = pos._1
        var y0   = pos._2
        var dist = dist_init

        while (true) {
            var x1 = x0 + hor_dir
            println( "Horizontal movement to " + x1.toString + "," + y0.toString )
            /* The cell is obstructed */
           if (gamestate.map.obstructed(x1,y0)) {
               println( x1, y0, "is obstructed" )
               return (new ListBuffer[CellPosed]())
           }
           /* The cell is the core objective, we return the last point of
            * the path */
           if ((new CellPos(x1,y0)) == objective ) {
               val res = new ListBuffer[CellPosed]()
               res += this.add_node(x1, y0, Some((0,0)), dist+horvert_dist)
               return res
           }

           /*We have an open space in (x1,y0) */
          dist += horvert_dist
          var x2 = x1+ hor_dir

          var nodes = new ListBuffer[CellPosed]()

          /* Choose the nodes to explore */
         if (gamestate.map.obstructed(x1,y0-1) &&
             !gamestate.map.obstructed(x2,y0-1)) {
                 println( "Jump point !" )
                 nodes += this.add_node(x1, y0, Some(hor_dir,-1), dist)
             }

             if (gamestate.map.obstructed(x1,y0+1) &&
                 !gamestate.map.obstructed(x2,y0+1)) {
                     println( "Jump point !" )
                     nodes += this.add_node(x1, y0, Some(hor_dir,1), dist)
                 }

                 if (!nodes.isEmpty) {
                     nodes += this.add_node(x1, y0, Some(hor_dir,0), dist)
                     return nodes
                 }

                 x0 = x1
        }
        return (new ListBuffer[CellPosed]())
    }

    /* Same but for vertical search */
    def vert_search(
        pos: (Int,Int),
        vert_dir: Int,
        dist_init: Double): ListBuffer[CellPosed] =
    {
        /** Manages the horizontal search of jump points
         *  @param pos: current position
         *  @param vert_dir : Horizontal direction (+/- 1)
         *  @param dist : Distance traveled so far
         */
        var x0 = pos._1
        var y0 = pos._2
        var dist = dist_init

        while (true) {
            var y1 = y0 + vert_dir
            println( "Vertical movement to " + x0.toString + "," + y1.toString )
            /* The cell is obstructed */
           if (gamestate.map.obstructed(x0,y1)){
               return (new ListBuffer[CellPosed]())
           }
           /* The cell is the core objective, we return the last point of
            * the path */
           if ((new CellPos(x0,y1))== objective ) {
               println("objective reached")
               val res = new ListBuffer[CellPosed]()
               res += this.add_node(x0, y1, Some((0,0)), dist + horvert_dist)
               return res
           }

           /*We have an open space in (x0,y1) */
          dist = dist + horvert_dist
          var y2 = y1 + vert_dir

          var nodes = new ListBuffer[CellPosed]()

          /* Choose the nodes to explore */
         if (gamestate.map.obstructed(x0-1,y1) &&
             !gamestate.map.obstructed(x0-1,y2)) {
                 nodes += this.add_node(x0, y1, Some(-1,vert_dir), dist)
             }

             if (gamestate.map.obstructed(x0+1,y1) &&
                 !gamestate.map.obstructed(x0+1,y2)) {
                     nodes += this.add_node(x0, y1, Some(1,vert_dir), dist)
                 }

                 if (!nodes.isEmpty) {
                     nodes += this.add_node(x0, y1, Some(0,vert_dir), dist)
                     return nodes
                 }

                 y0 = y1
        }
        return (new ListBuffer[CellPosed]())
    }

    def diag_search(
        pos: (Int,Int),
        hor_dir: Int,
        vert_dir: Int,
        dist_init: Double): ListBuffer[CellPosed] =
    {
        /** Manages the diagonal path search
         *  @param pos: Start position
         *  @param hor_dir: horizontal direction (+/- 1)
         *  @param vert_dir: vertical direction (+/- 1)
         *  @param dist : distance traveled so far
         */
        var x0 = pos._1
        var y0 = pos._2
        var dist = dist_init

        while (true) {
            var x1 = x0 + hor_dir
            var y1 = y0 + vert_dir
            /* The cell is obstructed */
           if (gamestate.map.obstructed(x1,y1) || (
               gamestate.map.obstructed(x1,y0) &&
           gamestate.map.obstructed(x0,y1) ) ) {
               return (new ListBuffer[CellPosed]())
           }
           /* The cell is the core objective, we return the last point of
            * the path */
           if (new CellPos(x1,y1) == this.objective) {
               var res = new ListBuffer[CellPosed]()
               res += this.add_node(x1,y1, Some((0,0)), dist + diag_dist)
               return res
           }
           /* There is open space at (x1,y1) */

          dist  = dist + diag_dist
          var x2 = x1 + hor_dir
          var y2 = y1 + vert_dir
          var nodes: ListBuffer[CellPosed] = new ListBuffer()

          if (gamestate.map.obstructed(x0,y1) &&
              !gamestate.map.obstructed(x0,y2)) {
                  nodes += add_node(x1, y1, Some(-hor_dir, vert_dir), dist)
              }

              if (gamestate.map.obstructed(x1,y0) &&
                  !gamestate.map.obstructed(x2,y0)) {
                      nodes += add_node(x1, y1, Some(hor_dir, -vert_dir), dist)
                  }

                  var hor_done = false
                  var vert_done = false

                  if (nodes.isEmpty) {
                      val sub_nodes = this.hor_search((x1,y1), hor_dir, dist)
                      hor_done = true

                      if (!sub_nodes.isEmpty) {
                          /* Horizontal search ended with some results */
                         val parent_node = this.get_closed_node(x1,y1, (hor_dir, 0), dist)
                         for (snode <- sub_nodes) {
                             snode.set_parent(parent_node)
                         }
                         nodes += parent_node
                      }
                  }

                  if (nodes.isEmpty) {
                      val sub_nodes = this.vert_search((x1,y1), vert_dir, dist)
                      var vert_done = true

                      if (!sub_nodes.isEmpty) {
                          val parent_node = this.get_closed_node(x1, y1, (0, vert_dir), dist)
                          for (snode <- sub_nodes) {
                              snode.set_parent(parent_node)
                          }
                          nodes += parent_node
                      }
                  }

                  if (!nodes.isEmpty) {
                      if (!hor_done) {
                          nodes += this.add_node(x1, y1, Some(hor_dir, 0), dist)
                      }
                      if (!vert_done) {
                          nodes += this.add_node(x1, y1, Some(0, vert_dir), dist)
                      }
                      nodes += this.add_node(x1, y1, Some(hor_dir, vert_dir), dist)
                      return nodes
                  }
                  x0 = x1
                  y0 = y1
        }
        return (new ListBuffer[CellPosed]())
    }

    def step(dist : Double, elem: CellPosed): Option[CellPosed] = {
        /** Performs a step of the algorithm, id est it finds the next
         *  jump point. It don't return none iff the jump point is the objective
         */
        if ((elem.cell.x, elem.cell.y) == (this.objective.x,this.objective.y)) {
            return Some(elem)
        }

        val hor_dir  = elem.dir._1
        val vert_dir = elem.dir._2
        var nodes = ListBuffer[CellPosed]()

        if (hor_dir != 0 && vert_dir != 0) {
            nodes = this.diag_search(
                (elem.cell.x, elem.cell.y),
                hor_dir, vert_dir,
                dist
            )
        }

        else if (vert_dir != 0) {
            nodes = this.vert_search(
                (elem.cell.x, elem.cell.y),
                vert_dir,
                dist
            )
        }
        else {
            if (hor_dir != 0) {
                nodes = this.hor_search(
                    (elem.cell.x, elem.cell.y),
                    hor_dir,
                    dist
                )
            }
        }

        for (node <- nodes) {
            val dist = this.all_list( node )
            this.all_list -= node
            node.set_parent(elem)
            this.all_list.update(node, dist)
        }

        return None
    }

    def toPath(pd : CellPosed): Path = {
        /** Traces back from the objective to the start,
         *  in order to find the best path */
        var path = new Path()
        var dep = pd
        path += pd.cell.toDouble
        while (dep.cell != this.start ) {
            path ++= dep.backtrace.reverse
            dep = dep.parent.get
        }
        return path
    }

    def run() : Option[Path] = {
        /** Runs the algorithm. It returns Some(path) if the path exists,
         *  None if not
         */
        while (true) {
            var (total, pd, dist) = this.get_open()
            if (total.isEmpty) {
                return None
            }

            var pd_bis = this.step(dist.get, pd.get)
            if (!pd_bis.isEmpty) {
                println("Path found")
                return Some(this.toPath(pd_bis.get))
            }
        }
        return None
    }
}
