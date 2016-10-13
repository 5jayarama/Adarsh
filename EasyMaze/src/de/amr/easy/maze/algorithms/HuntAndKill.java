package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import java.util.BitSet;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Generates a maze similar to the "hunt-and-kill" algorithm.
 *
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/24/maze-generation-hunt-and-kill-algorithm.html">
 *      Hunt-And-Kill algorithm on Jamis Buck's blog</a>
 */
public class HuntAndKill implements Consumer<Integer> {

	protected final ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid;
	protected final BitSet targets;

	public HuntAndKill(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		this.grid = grid;
		targets = new BitSet(grid.numCells());
	}

	@Override
	public void accept(Integer animal) {
		final Predicate<Integer> isAlive = a -> grid.get(a) == UNVISITED;
		final Predicate<Integer> isDead = isAlive.negate();
		do {
			kill(animal);
			grid.neighbors(animal).filter(isAlive).forEach(targets::set);
			Optional<Integer> livingNeighbor = grid.randomNeighbor(animal, isAlive);
			if (livingNeighbor.isPresent()) {
				grid.addEdge(new DefaultEdge<>(animal, livingNeighbor.get()));
				animal = livingNeighbor.get();
			} else if (!targets.isEmpty()) {
				animal = hunt();
				grid.addEdge(new DefaultEdge<>(animal, grid.randomNeighbor(animal, isDead).get()));
			}
		} while (!targets.isEmpty());
	}

	protected Integer hunt() {
		return targets.nextSetBit(0);
	}

	protected void kill(Integer animal) {
		grid.set(animal, COMPLETED);
		targets.clear(animal);
	}
}