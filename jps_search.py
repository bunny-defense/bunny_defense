import heapq

# Map size
XCOUNT   = 50  # Number of columns
YCOUNT   = 50  # Number of rows

# Cell contents
NOTHING  = 0   # Unvisited/free node
OBSTACLE = 1   # Wall
START    = 2   # Start point of the search
DEST     = 3   # Destination of the search

# Movement costs
HORVERT_COST  = 5
DIAGONAL_COST = 7

LINES = [
    "..................................................",
    "..................................................",
    "..................................................",
    "..................................................",
    "..................................................",
    "........................#.........................",
    "........................#.........................",
    "........................#.........................",
    "........................#.........................",
    "..........#.............#.........................",
    "..........#.............#.........................",
    "..........#.............#.........................",
    "..........#.............#.........................",
    "..........#.............#.........................",
    ".....@....#.............#.........................",
    "..........#.............##........................",
    "..........#..............#........................",
    "..........#..............#........................",
    ".....#################..#####################.....",
    ".....................#...............#......##....",
    ".....................#...............#....*..#....",
    ".....................#...............#.......##...",
    ".....................#...............#...#....#...",
    ".....................#.......#.......#...#....#...",
    ".....................#.......#.......#...#....#...",
    ".....................#.......#.......#...#....#...",
    ".....................#.......#.......#...#....#...",
    ".....................#.......#.......#...#....#...",
    ".....................#.......#.......#...#........",
    ".....................#.......#...........#........",
    ".............................#...........#........",
    "............................##...........#........",
    "............................#............#........",
    "..........................###............#........",
    "......................#####..............#........",
    "...............########..................#........",
    "...........#####.........................#........",
    "........................................##........",
    ".......................................##.........",
    "......................................##..........",
    ".....................................##...........",
    "....................................##............",
    "............................#########.............",
    "..................................................",
    "..................................................",
    "..................................................",
    "..................................................",
    "..................................................",
    "..................................................",
    "..................................................",
]


grid = [] # Game field.
for x in range(XCOUNT):
    grid.append([NOTHING] * YCOUNT)

def load():
    """Load the grid"""
    start = None
    dest = None

    global grid
    for x in range(XCOUNT):
        for y in range(YCOUNT):
            grid[x][y] = NOTHING

    y = 0
    for line in LINES:
        if y >= YCOUNT:
            break

        x = 0
        for s in line.rstrip():
            if x >= XCOUNT:
                break

            if s == '*':
                start = (x, y)
                grid[x][y] = NOTHING
            elif s == '@':
                dest = (x, y)
                grid[x][y] = NOTHING
            elif s == '#':
                grid[x][y] = OBSTACLE
            else:
                grid[x][y] = NOTHING

            x = x + 1
        y = y + 1
    return start, dest

class PosDir:
    """
    Jump point, pair of position and direction.

    @ivar pos: Position of the jump point (xpos, ypos).
    @type pos: Pair of (C{int}, C{int})

    @ivar dir: Direction of the jump point (hor_dir, vert_dir).
    @type dir: Pair of (C{int}, C{int})

    @ivar parent: Parent jump point (for finding path back to the start).
    @type parent: C{None} or C{PosDir}
    """
    def __init__(self, pos, dir):
        self.pos = pos
        self.dir = dir
        self.parent = None

    def set_parent(self, parent):
        assert self.parent is None
        self.parent = parent

    def __eq__(self, other):
        if not isinstance(other, PosDir):
            return False
        return self.pos == other.pos and self.dir == other.dir

    def __lt__(self, other):
        if not isinstance(other, PosDir):
            return False
        if self.pos != other.pos:
            return self.pos < other.pos
        return self.dir < other.dir

    def __hash__(self):
        return hash(self.pos) + hash(self.dir) * 737

    def __repr__(self):
        return "PosDir({}, {})".format(self.pos, self.dir)

class AStarBase:
    def __init__(self, start, dest):
        self.start = start
        self.dest = dest

        self.all_list = {} # PosDir to best travel-cost
        self.queue = []    # Priority queue on traveled + estimate

    def estimate(self, pos, dir):
        dx = abs(pos[0] - self.dest[0])
        dy = abs(pos[1] - self.dest[1])
        common = min(dx, dy)
        return DIAGONAL_COST * common + HORVERT_COST * ((dx - common) + (dy - common))

    def add_node(self, x, y, dir, dist):
        """
        Add an open jump point node at the given position and direction (if no better node exists).

        @param x: Horizontal position of the new node.
        @type  x: C{int}

        @param y: Vertical position of the new node.
        @type  y: C{int}

        @param dir: Direction of movement from the new position (signs).
        @type  dir: Pair of (C{int}, C{int})

        @param dist: Distance traveled so far.
        @type  dist: C{int}

        @return: The new node.
        @rtype:  L{PosDir}
        """
        pd = PosDir((x, y), dir)
        current = self.all_list.get(pd)
        if current is None or current > dist:
            total = dist + self.estimate(pd.pos, dir)
            self.all_list[pd] = dist
            self.add_open(total, pd, dist)

        return pd

    def get_closed_node(self, x, y, dir, dist):
        """
        Add a closed node at the given position. Returns an existing or a new node.

        @param x: Horizontal position of the node.
        @type  x: C{int}

        @param y: Vertical position of the node.
        @type  y: C{int}

        @param dir: Direction of movement from the new position (signs).
        @type  dir: Pair of (C{int}, C{int})

        @param dist: Distance traveled so far.
        @type  dist: C{int}

        @return: The new or existing node.
        @rtype:  L{PosDir}
        """
        pd = PosDir((x, y), dir)
        current = self.all_list.get(pd)
        if current is not None and current <= dist:
            return current

        self.all_list[pd] = dist
        return pd

    def add_open(self, total, pd, dist):
        """
        Add element to the open list.
        """
        heapq.heappush(self.queue, (total, pd, dist))

    def get_open(self):
        """
        Get smallest element from the open list.
        """
        while True:
            if len(self.queue) == 0:
                return None, None, None

            total, pd, dist = heapq.heappop(self.queue)
            current = self.all_list.get(pd)
            if dist == current:
                return total, pd, dist

            # else: a better solution was found to this position, ignore this one.

    def on_map(self, x, y):
        """
        Is the given position on the map?
        """
        return x >= 0 and x < XCOUNT and y >= 0 and y < YCOUNT

    def obstacle(self, x, y):
        """
        Is the given position off-map, or does it contain an obstacle (wall)?
        """
        return not self.on_map(x, y) or grid[x][y] == OBSTACLE

    def run(self, alg_name):
        """
        Find the path, and print it.
        """
        while True:
            total, pd, dist = self.get_open()
            if total is None:
                break

            pd = self.step(dist, pd)
            if pd is not None:
                break

        open_count = 0
        while True:
            total, pd, dist = self.get_open()
            if total is None:
                break

            open_count = open_count + 1

        print("{}: open queue = {}".format(alg_name, open_count))
        print("{}: all_length = {}".format(alg_name, len(self.all_list)))


class JPS(AStarBase):
    def __init__(self, start, dest):
        AStarBase.__init__(self, start, dest)

        for dx in [-1, 0, 1]:
            for dy in [-1, 0, 1]:
                if dx != 0 or dy != 0:
                    self.add_node(self.start[0], self.start[1], (dx, dy), 0)

    def estimate(self, pos, dir):
        if dir is None or dir == (0, 0):
            add = 0
        else:
            add = 7
            pos = pos[0] + dir[0], pos[1] + dir[1]
        dx = abs(pos[0] - self.dest[0])
        dy = abs(pos[1] - self.dest[1])
        common = min(dx, dy)
        return add + 7 * common + 5 * ((dx - common) + (dy - common))

    def step(self, dist, elm):
        """
        Perform a scan step in the JPS algorithm.

        @param dist: Distance traveled so far.
        @type  dist: C{int}

        @param elm: Jump point element.
        @type  elm: L{PosDir}

        @return: Jump point at the destination, or C{None}.
        @rtype:  C{PosDir} or C{None}
        """
        if elm.pos == self.dest:
            return elm # Found destination, finished searching.

        hor_dir, vert_dir = elm.dir

        if hor_dir != 0 and vert_dir != 0:
            nodes = self.search_diagonal(elm.pos, hor_dir, vert_dir, dist)

        elif vert_dir != 0:
            nodes = self.search_vert(elm.pos, vert_dir, dist)

        else:
            assert hor_dir != 0
            nodes = self.search_hor(elm.pos, hor_dir, dist)

        for nd in nodes:
            nd.set_parent(elm)

        return None # No destination found yet.

    def search_hor(self, pos, hor_dir, dist):
        """
        Search in horizontal direction, return the newly added open nodes

        @param pos: Start position of the horizontal scan.
        @type  pos: Pair (C{int}, C{int})

        @param hor_dir: Horizontal direction (+1 or -1).
        @type  hor_dir: C{int}

        @param dist: Distance traveled so far.
        @type  dist: C{int}

        @return: New jump point nodes (which need a parent).
        @rtype:  C{list} of L{PosDir}
        """
        x0, y0 = pos
        while True:
            x1 = x0 + hor_dir
            if not self.on_map(x1, y0):
                return [] # Off-map, done.

            g = grid[x1][y0]
            if g == OBSTACLE:
                return [] # Done.

            if (x1, y0) == self.dest:
                return [self.add_node(x1, y0, None, dist + HORVERT_COST)]

            # Open space at (x1, y0).
            dist = dist + HORVERT_COST
            x2 = x1 + hor_dir

            nodes = []
            if self.obstacle(x1, y0 - 1) and not self.obstacle(x2, y0 - 1):
                nodes.append(self.add_node(x1, y0, (hor_dir, -1), dist))

            if self.obstacle(x1, y0 + 1) and not self.obstacle(x2, y0 + 1):
                nodes.append(self.add_node(x1, y0, (hor_dir, 1), dist))

            if len(nodes) > 0:
                nodes.append(self.add_node(x1, y0, (hor_dir, 0), dist))
                return nodes

            # Process next tile.
            x0 = x1

    def search_vert(self, pos, vert_dir, dist):
        """
        Search in vertical direction, return whether a new open node was added.

        @param pos: Start position of the horizontal scan.
        @type  pos: Pair (C{int}, C{int})

        @param vert_dir: Vertical direction (+1 or -1).
        @type  vert_dir: C{int}

        @param dist: Distance traveled so far.
        @type  dist: C{int}

        @return: New jump point nodes (which need a parent).
        @rtype:  C{list} of L{PosDir}
        """
        x0, y0 = pos
        while True:
            y1 = y0 + vert_dir
            if not self.on_map(x0, y1):
                return [] # Off-map, done.

            g = grid[x0][y1]
            if g == OBSTACLE:
                return [] # Done.

            if (x0, y1) == self.dest:
                return [self.add_node(x0, y1, None, dist + 5)]

            # Open space at (x0, y1).
            dist = dist + 5
            y2 = y1 + vert_dir

            nodes = []
            if self.obstacle(x0 - 1, y1) and not self.obstacle(x0 - 1, y2):
                nodes.append(self.add_node(x0, y1, (-1, vert_dir), dist))

            if self.obstacle(x0 + 1, y1) and not self.obstacle(x0 + 1, y2):
                nodes.append(self.add_node(x0, y1, (1, vert_dir), dist))

            if len(nodes) > 0:
                nodes.append(self.add_node(x0, y1, (0, vert_dir), dist))
                return nodes

            # Process next tile.
            y0 = y1

    def search_diagonal(self, pos, hor_dir, vert_dir, dist):
        """
        Search diagonally, spawning horizontal and vertical searches.
        Returns newly added open nodes.

        @param pos: Start position.
        @type  pos: Pair of C{int}, C{int}

        @param hor_dir: Horizontal search direction (+1 or -1).
        @type  hor_dir: C{int}

        @param vert_dir: Vertical search direction (+1 or -1).
        @type  vert_dir: C{int}

        @param dist: Distance traveled so far.
        @type  dist: C{int}

        @return: Jump points created during this scan (which need to get a parent jump point).
        @rtype:  C{list} of L{PosDir}
        """
        x0, y0 = pos
        while True:
            x1, y1 = x0 + hor_dir, y0 + vert_dir
            if not self.on_map(x1, y1):
                return [] # Off-map, done.

            g = grid[x1][y1]
            if g == OBSTACLE:
                return []

            if (x1, y1) == self.dest:
                return [self.add_node(x1, y1, None, dist + DIAGONAL_COST)]

            # Open space at (x1, y1)
            dist = dist + DIAGONAL_COST
            x2, y2 = x1 + hor_dir, y1 + vert_dir

            nodes = []
            if self.obstacle(x0, y1) and not self.obstacle(x0, y2):
                nodes.append(self.add_node(x1, y1, (-hor_dir, vert_dir), dist))

            if self.obstacle(x1, y0) and not self.obstacle(x2, y0):
                nodes.append(self.add_node(x1, y1, (hor_dir, -vert_dir), dist))

            hor_done, vert_done = False, False
            if len(nodes) == 0:
                sub_nodes = self.search_hor((x1, y1), hor_dir, dist)
                hor_done = True

                if len(sub_nodes) > 0:
                    # Horizontal search ended with a spawn point.
                    pd = self.get_closed_node(x1, y1, (hor_dir, 0), dist)
                    for sub in sub_nodes:
                        sub.set_parent(pd)

                    nodes.append(pd)

            if len(nodes) == 0:
                sub_nodes = self.search_vert((x1, y1), vert_dir, dist)
                vert_done = True

                if len(sub_nodes) > 0:
                    # Vertical search ended with a spawn point.
                    pd = self.get_closed_node(x1, y1, (0, vert_dir), dist)
                    for sub in sub_nodes:
                        sub.set_parent(pd)

                    nodes.append(pd)

            if len(nodes) > 0:
                if not hor_done:
                    nodes.append(self.add_node(x1, y1, (hor_dir, 0), dist))

                if not vert_done:
                    nodes.append(self.add_node(x1, y1, (0, vert_dir), dist))

                nodes.append(self.add_node(x1, y1, (hor_dir, vert_dir), dist))
                return nodes

            # Tile done, move to next tile.
            x0, y0 = x1, y1

class AStar(AStarBase):
    def __init__(self, start, dest):
        AStarBase.__init__(self, start, dest)

        self.add_node(self.start[0], self.start[1], None, 0)

    def get_neighbours(self, xpos, ypos, dist):
        """Return iterator for the direct neighbours around (xpos, ypos)"""
        for dx, dy, dd in [( 1,  1, 7), (-1, 0, 5), ( 1,-1, 7),
                           ( 0, -1, 5),             ( 0, 1, 5),
                           (-1,  1, 7), ( 1, 0, 5), (-1,-1, 7)]:
            nx, ny = dx + xpos, dy + ypos
            if not self.on_map(nx, ny):
                continue

            yield nx, ny, dd + dist

    def step(self, dist, elm):
        """
        Perform a step in the plain A* algorithm.
        """
        if elm.pos == self.dest:
            return elm

        x, y  = elm.pos
        for nx, ny, nd in self.get_neighbours(x, y, dist):
            self.add_node(nx, ny, None, dist)

        return None

class Dijkstra(AStar):
    def estimate(self, pos, dir):
        return 0


start, dest = load() # Load grid

print()
dijkstra = Dijkstra(start, dest)
dijkstra.run("Dijkstra")

print()
astar = AStar(start, dest)
astar.run("A*")

print()
jps = JPS(start, dest)
jps.run("JPS")

