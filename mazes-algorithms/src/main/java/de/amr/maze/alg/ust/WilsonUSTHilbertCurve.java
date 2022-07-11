package de.amr.maze.alg.ust;

import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.grid.api.GridPosition.TOP_LEFT;
import static de.amr.graph.grid.impl.Grid4Topology.E;
import static de.amr.graph.grid.impl.Grid4Topology.N;
import static de.amr.graph.grid.impl.Grid4Topology.S;
import static de.amr.graph.grid.impl.Grid4Topology.W;
import static de.amr.graph.grid.impl.GridFactory.emptyGrid;
import static de.amr.graph.util.GraphUtils.log;
import static de.amr.graph.util.GraphUtils.nextPow;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

import java.util.stream.IntStream;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.curves.HilbertCurve;
import de.amr.graph.grid.impl.Grid4Topology;
import de.amr.graph.grid.impl.GridGraph;

/**
 * Wilson's algorithm where the random walk start cells are defined by a Hilbert curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTHilbertCurve extends WilsonUST {

	public WilsonUSTHilbertCurve(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected IntStream randomWalkStartCells() {
		int[] cells = new int[grid.numVertices()];
		int n = nextPow(2, max(grid.numCols(), grid.numRows()));
		GridGraph<?, ?> squareGrid = emptyGrid(n, n, Grid4Topology.get(), UNVISITED, 0);
		int cell = squareGrid.cell(TOP_LEFT);
		int i = 0;
		cells[i++] = cell;
		for (byte dir : new HilbertCurve(log(2, n), W, N, E, S)) {
			cell = squareGrid.neighbor(cell, dir).get();
			int col = squareGrid.col(cell), row = squareGrid.row(cell);
			if (grid.isValidCol(col) && grid.isValidRow(row)) {
				cells[i++] = grid.cell(col, row);
			}
		}
		return stream(cells);
	}
}