package de.amr.maze.alg.ust;

import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.grid.impl.GridFactory.emptyGrid;
import static de.amr.graph.util.GraphUtils.log;
import static de.amr.graph.util.GraphUtils.nextPow;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

import java.util.stream.IntStream;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.curves.MooreLCurve;
import de.amr.graph.grid.impl.Grid4Topology;

/**
 * Wilson's algorithm where the random walks start cells are defined by a Moore curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTMooreCurve extends WilsonUST {

	public WilsonUSTMooreCurve(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected IntStream randomWalkStartCells() {
		int[] cells = new int[grid.numVertices()];
		int n = nextPow(2, max(grid.numCols(), grid.numRows()));
		var squareGrid = emptyGrid(n, n, Grid4Topology.get(), UNVISITED, 0);
		int cell = squareGrid.cell(n / 2, n - 1);
		int i = 0;
		if (grid.isValidCol(n / 2) && grid.isValidRow(n - 1)) {
			cells[i++] = grid.cell(n / 2, n - 1);
		}
		for (byte dir : new MooreLCurve(log(2, n))) {
			var neighbor = squareGrid.neighbor(cell, dir);
			if (neighbor.isPresent()) {
				cell = neighbor.get();
				int col = squareGrid.col(cell);
				int row = squareGrid.row(cell);
				if (grid.isValidCol(col) && grid.isValidRow(row)) {
					cells[i++] = grid.cell(col, row);
				}
			}
		}
		return stream(cells);
	}
}