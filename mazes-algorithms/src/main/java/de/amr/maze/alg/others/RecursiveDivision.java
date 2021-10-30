package de.amr.maze.alg.others;

import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static java.util.stream.IntStream.range;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Creates maze by recursive division.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/12/maze-generation-recursive-division-algorithm.html">Maze
 *      Generation: Recursive Division</a>
 */
public class RecursiveDivision extends MazeGenerator {

	public RecursiveDivision(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
		grid.fillOrthogonal();
		grid.setDefaultVertexLabel(cell -> COMPLETED);
	}

	@Override
	public void createMaze(int x, int y) {
		divide(0, 0, grid.numCols(), grid.numRows());
	}

	/**
	 * Divides the {@code (w x h)}-subgrid with top-left position {@code (x0, y0)}.
	 * 
	 * @param x0 x-position of subgrid
	 * @param y0 y-position subgrid
	 * @param w  width of subgrid
	 * @param h  height of subgrid
	 */
	private void divide(int x0, int y0, int w, int h) {
		if (w <= 1 && h <= 1) {
			return;
		}
		if (w < h || (w == h && rnd.nextBoolean())) {
			// Build "horizontal wall" at random y from [y0 + 1, y0 + h - 1], keep random door
			int y = y0 + 1 + rnd.nextInt(h - 1);
			int door = x0 + rnd.nextInt(w);
			range(x0, x0 + w).filter(x -> x != door).forEach(x -> {
				grid.edge(grid.cell(x, y - 1), grid.cell(x, y)).ifPresent(grid::removeEdge);
			});
			divide(x0, y0, w, y - y0);
			divide(x0, y, w, h - (y - y0));
		} else {
			// Build "vertical wall" at random x from [x0 + 1, x0 + w - 1], keep random door
			int x = x0 + 1 + rnd.nextInt(w - 1);
			int door = y0 + rnd.nextInt(h);
			range(y0, y0 + h).filter(y -> y != door).forEach(y -> {
				grid.edge(grid.cell(x - 1, y), grid.cell(x, y)).ifPresent(grid::removeEdge);
			});
			divide(x0, y0, x - x0, h);
			divide(x, y0, w - (x - x0), h);
		}
	}
}