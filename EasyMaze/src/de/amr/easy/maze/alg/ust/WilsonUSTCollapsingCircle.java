package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static java.lang.Math.max;

import de.amr.easy.grid.iterators.shapes.Circle;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Wilson's algorithm where the vertices are selected from a collapsing circle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTCollapsingCircle extends WilsonUST {

	public WilsonUSTCollapsingCircle(OrthogonalGrid grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		int center = grid.cell(CENTER);
		grid.set(center, COMPLETED);
		for (int r = max(grid.numRows(), grid.numCols()) - 1; r > 0; r--) {
			new Circle(grid, center, r).forEach(this::loopErasedRandomWalk);
		}
	}
}