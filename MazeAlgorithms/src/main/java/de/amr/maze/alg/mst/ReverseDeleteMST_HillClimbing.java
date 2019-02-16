package de.amr.maze.alg.mst;

import de.amr.graph.pathfinder.api.TraversalState;
import de.amr.graph.pathfinder.impl.HillClimbingSearch;

/**
 * Reverse-Delete-MST algorithm using "hill climbing" for connectivity test.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteMST_HillClimbing extends ReverseDeleteMST {

	public ReverseDeleteMST_HillClimbing(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected boolean connected(int u, int v) {
		HillClimbingSearch<TraversalState, Integer> search = new HillClimbingSearch<>(grid,
				x -> grid.manhattan(x, v));
		search.exploreGraph(u, v);
		return search.getParent(v) != -1;
	}
}