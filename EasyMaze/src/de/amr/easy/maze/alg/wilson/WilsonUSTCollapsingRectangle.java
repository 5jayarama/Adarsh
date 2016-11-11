package de.amr.easy.maze.alg.wilson;

import static de.amr.easy.grid.api.GridPosition.CENTER;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.dir.Dir4;
import de.amr.easy.grid.iterators.shapes.Rectangle;

/**
 * Wilson's algorithm where the vertices are selected from a collapsing rectangle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTCollapsingRectangle extends WilsonUST {

	private int width, height;

	public WilsonUSTCollapsingRectangle(Grid2D<Dir4,TraversalState, Integer> grid) {
		super(grid);
		width = grid.numCols();
		height = grid.numRows();
	}

	@Override
	public void accept(Integer start) {
		start = grid.cell(CENTER);
		addToTree(start);
		int col = 0, row = 0;
		while (width > 0 && height > 0) {
			new Rectangle(grid, grid.cell(col, row), width, height).forEach(this::loopErasedRandomWalk);
			width -= 2;
			height -= 2;
			++col;
			++row;
		}
	}
}