package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Comparator;

import de.amr.easy.data.Stack;
import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.TraversalState;

/**
 * A heuristic depth-first-search where the children of the current vertex are visited in the order
 * given by a vertex valuation, for example the Euclidian distance from a given target.
 * <p>
 * From: Patrick Henry Winston, Artificial Intelligence, p.93, Addison-Wesley, 1984
 * 
 * @author Armin Reichert
 */
public class HillClimbing extends AbstractGraphTraversal implements ObservableDFSPathFinder {

	private final Graph<?> graph;
	private final int source;
	private final int target;
	private final Stack<Integer> stack;

	public Comparator<Integer> vertexValuation = Integer::compare;

	public HillClimbing(Graph<?> graph, int source, int target) {
		this.graph = graph;
		this.source = source;
		this.target = target;
		this.stack = new Stack<>();
	}

	@Override
	public void clear() {
		super.clear();
		stack.clear();
	}

	@Override
	public int getSource() {
		return source;
	}

	@Override
	public int getTarget() {
		return target;
	}

	@Override
	public boolean isStacked(int cell) {
		return stack.contains(cell);
	}

	@Override
	public void traverseGraph() {
		visit(-1, source);
		while (!stack.isEmpty()) {
			if (stack.top().get() == target) {
				break;
			}
			int current = stack.pop();
			/*@formatter:off*/
				graph.adjVertices(current)
					.filter(child -> getState(child) == UNVISITED)
					.boxed()
					.sorted(vertexValuation.reversed())
					.forEach(child -> visit(current, child));
				/*@formatter:on*/
			setState(current, COMPLETED);
		}
		while (!stack.isEmpty()) {
			setState(stack.pop(), COMPLETED);
		}
	}

	private void visit(int parent, int child) {
		TraversalState oldState = getState(child);
		setState(child, VISITED);
		setParent(child, parent);
		stack.push(child);
		vertexTouched(child, oldState, getState(child));
		if (parent != -1) {
			edgeTouched(parent, child);
		}
	}
}