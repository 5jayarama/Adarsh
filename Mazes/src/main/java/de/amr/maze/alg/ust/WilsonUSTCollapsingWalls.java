package de.amr.maze.alg.ust;

import java.util.stream.IntStream;

import de.amr.graph.grid.traversals.CollapsingWalls;

/**
 * Wilson's algorithm where the vertices are selected alternating left-to-right and right-to-left
 * column-wise.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTCollapsingWalls extends WilsonUST {

	public WilsonUSTCollapsingWalls(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return new CollapsingWalls(grid).stream();
	}
}