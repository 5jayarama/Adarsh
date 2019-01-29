package de.amr.easy.maze.alg.mst;

import de.amr.graph.pathfinder.impl.DepthFirstSearch2;

/**
 * Reverse-Delete-MST algorithm using depth-first search for connectivity test.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteMST_DFS extends ReverseDeleteMST {

	public ReverseDeleteMST_DFS(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected boolean connected(int u, int v) {
		DepthFirstSearch2 dfs = new DepthFirstSearch2(grid);
		dfs.traverseGraph(u, v);
		return dfs.getParent(v) != -1;
	}
}