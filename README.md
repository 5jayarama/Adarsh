# mazes

### Maze generation algorithms implemented in Java 8

<img src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_EllerInsideOut_300x200.gif">

My interest in maze generation started after reading the [weblog of Jamis Buck](http://weblog.jamisbuck.org/archives.html) where he presents a number of maze generating algorithms implemented in Ruby.

Initially, I just wanted to implement some of these algorithms myself using Java, especially in such a way that the underlying graph algorithm should be clearly visible. In the course of doing that I discovered new ways of maze generation, for example a modified version of Eller's algorithm which generates the maze from the center towards the borders, or variations on Wilson's algorithm by using different kinds of selecting the sources of the random walks. For example, one can select the start cells of the random walks in the order defined by some space-filling-curve ([Hilbert](EasyGrid/src/de/amr/easy/grid/curves/HilbertCurve.java), [Peano](EasyGrid/src/de/amr/easy/grid/curves/PeanoCurve.java), [Moore](EasyGrid/src/de/amr/easy/grid/curves/MooreLCurve.java)). Probably of no practical use, but at least fun to watch!

This implementation emphasizes the underlying graph algorithm (creating a **spanning tree** of an undirected grid graph) and the maze generator code is free of rendering details or ad-hoc data structure implementations.

To achieve that, there is
- an API for Graph and Grid data structures 
- an efficient implementation of a 2D-Grid
- a publish-subscribe mechanism for observing graph operations used for example to animate the generation process visually

All Maze generation algorithms work only against the Grid API, renderers are attached as graph or graph traversal listeners.

The implementation uses Java 8 features (streams and lambda expressions) for better readability and has no dependencies to any UI library (AWT, Swing, JavaFX).

Also included is a Swing application demonstrating all implemented maze generators. Using a control panel you can select the generation algorithm, path finder algorithm, grid resolution and rendering style ("walls" vs. "passages") interactively.

Implemented so far:

Graph Traversal:
- [Random Breadth-First-Search](EasyMaze/src/de/amr/easy/maze/alg/RandomBFS.java)
- [Random Depth-First-Search, iterative](EasyMaze/src/de/amr/easy/maze/alg/IterativeDFS.java)
- [Random Depth-First-Search, recursive](EasyMaze/src/de/amr/easy/maze/alg/RecursiveDFS.java)

Minimum Spanning Tree: 
- [Boruvka](EasyMaze/src/de/amr/easy/maze/alg/mst/BoruvkaMST.java)
- [Kruskal](EasyMaze/src/de/amr/easy/maze/alg/mst/KruskalMST.java)
- [Prim](EasyMaze/src/de/amr/easy/maze/alg/mst/PrimMST.java)
- [Reverse-Delete, naive](EasyMaze/src/de/amr/easy/maze/alg/mst/ReverseDeleteMST.java)

Uniform Spanning Tree:
- [Aldous-Broder](EasyMaze/src/de/amr/easy/maze/alg/AldousBroderUST.java)
- [Wilson's algorithm](EasyMaze/src/de/amr/easy/maze/alg/wilson) (16 different variants)

Other algorithms:
- [Binary Tree, top-to-bottom](EasyMaze/src/de/amr/easy/maze/alg/BinaryTree.java)
- [Binary Tree, random](EasyMaze/src/de/amr/easy/maze/alg/BinaryTreeRandom.java)
- [Eller's algorithm](EasyMaze/src/de/amr/easy/maze/alg/Eller.java)
- [Armin's algorithm](EasyMaze/src/de/amr/easy/maze/alg/EllerInsideOut.java) (like Eller's but growing the maze inside-out)
- [Sidewinder](EasyMaze/src/de/amr/easy/maze/alg/Sidewinder.java)
- [Growing Tree](EasyMaze/src/de/amr/easy/maze/alg/GrowingTree.java)
- [Hunt-And-Kill, top-to-bottom](EasyMaze/src/de/amr/easy/maze/alg/HuntAndKill.java)
- [Hunt-And-Kill, random](EasyMaze/src/de/amr/easy/maze/alg/HuntAndKillRandom.java)
- [Recursive division](EasyMaze/src/de/amr/easy/maze/alg/RecursiveDivision.java)

Path finding algorithms:
- The EasyGrid library contains a DFS and BFS-based path finding implementation.

As an example, this is the generator based on the Kruskal minimum-spanning-tree algorithm:

```java
public class KruskalMST extends MazeAlgorithm {

	private Partition<Integer> forest;

	public KruskalMST(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(Integer start) {
		forest = new Partition<>(grid.vertexStream());
		grid.fullGridEdgesPermuted().forEach(this::addEdgeToMaze);
	}

	private void addEdgeToMaze(Edge<Integer> edge) {
		Integer u = edge.either(), v = edge.other(u);
		if (!forest.sameComponent(u, v)) {
			grid.addEdge(u, v);
			grid.set(u, COMPLETED);
			grid.set(v, COMPLETED);
			forest.union(u, v);
		}
	}
}
```
