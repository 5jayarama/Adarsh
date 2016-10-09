package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Maze generator using a randomized breadth-first-traversal.
 * 
 * @author Armin Reichert
 */
public class RandomBFS implements Consumer<Integer> {

	private final ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid;
	private final Random rnd = new Random();
	private final Set<Integer> mazeCells = new HashSet<>();
	private final List<Integer> frontier = new LinkedList<>();

	public RandomBFS(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		this.grid = grid;
	}

	@Override
	public void accept(Integer start) {
		mazeCells.add(start);
		frontier.add(start);
		grid.set(start, VISITED);
		while (!frontier.isEmpty()) {
			int index = frontier.size() == 1 ? 0 : rnd.nextInt(frontier.size());
			Integer cell = frontier.remove(index);
			/*@formatter:off*/
			grid.neighbors(cell, Direction.randomOrder())
				.filter(neighbor -> !mazeCells.contains(neighbor))
				.forEach(newMazeCell -> {
					mazeCells.add(newMazeCell);
					frontier.add(newMazeCell);
					grid.set(newMazeCell, VISITED);
					grid.addEdge(new DefaultEdge<>(cell, newMazeCell));
				});
			/*@formatter:on*/
			grid.set(cell, COMPLETED);
		}
	}
}