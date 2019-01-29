package de.amr.easy.maze.alg.traversal;

import static de.amr.datastruct.StreamUtils.randomElement;
import static de.amr.graph.grid.impl.OrthogonalGrid.emptyGrid;
import static de.amr.graph.pathfinder.api.TraversalState.COMPLETED;
import static de.amr.graph.pathfinder.api.TraversalState.UNVISITED;
import static de.amr.graph.pathfinder.api.TraversalState.VISITED;

import java.util.OptionalInt;

import de.amr.datastruct.Stack;
import de.amr.easy.maze.alg.core.MazeGenerator;
import de.amr.graph.grid.impl.OrthogonalGrid;

/**
 * Generates a maze by iterative random depth-first traversal of a grid.
 * 
 * @author Armin Reichert
 */
public class IterativeDFS implements MazeGenerator<OrthogonalGrid> {

	private OrthogonalGrid grid;

	public IterativeDFS(int numCols, int numRows) {
		grid = emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public OrthogonalGrid getGrid() {
		return grid;
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		Stack<Integer> stack = new Stack<>();
		int current = grid.cell(x, y);
		grid.set(current, VISITED);
		stack.push(current);
		while (!stack.isEmpty()) {
			OptionalInt unvisitedNeighbor = randomUnvisitedNeighbor(current);
			if (unvisitedNeighbor.isPresent()) {
				int neighbor = unvisitedNeighbor.getAsInt();
				grid.addEdge(current, neighbor);
				grid.set(neighbor, VISITED);
				if (randomUnvisitedNeighbor(neighbor).isPresent()) {
					stack.push(neighbor);
				}
				current = neighbor;
			} else {
				grid.set(current, COMPLETED);
				if (!stack.isEmpty()) {
					current = stack.pop();
				}
				if (!grid.isCompleted(current)) {
					stack.push(current);
				}
			}
		}
		return grid;
	}

	private OptionalInt randomUnvisitedNeighbor(int cell) {
		return randomElement(grid.neighbors(cell).filter(grid::isUnvisited));
	}
}