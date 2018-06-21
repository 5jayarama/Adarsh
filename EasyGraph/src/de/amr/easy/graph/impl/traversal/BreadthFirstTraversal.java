package de.amr.easy.graph.impl.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Queue;

import de.amr.easy.graph.api.Graph;

/**
 * Breadth-first traversal of an undirected graph from a given source vertex. After being executed,
 * the distance of each vertex from the source can be queried, as well as the maximal distance of a
 * reachable vertex from the source.
 * <p>
 * During the traversal, events are fired which can be processed by observers, for example an
 * animation.
 * 
 * @author Armin Reichert
 */
public class BreadthFirstTraversal extends AbstractGraphTraversal {

	protected final Queue<Integer> q;
	protected final int[] distance;
	private int maxDistance;

	protected BreadthFirstTraversal(Graph<?, ?> graph, Queue<Integer> queue) {
		super(graph);
		this.q = queue;
		this.distance = new int[graph.numVertices()];
	}

	public BreadthFirstTraversal(Graph<?, ?> graph) {
		this(graph, new ArrayDeque<>());
	}

	@Override
	protected void clear() {
		super.clear();
		q.clear();
		Arrays.fill(distance, -1);
		maxDistance = -1;
	}

	@Override
	public boolean inQ(int vertex) {
		return q.contains(vertex);
	}

	@Override
	public void traverseGraph(int source, int target) {
		clear();
		q.add(source);
		setState(source, VISITED);
		maxDistance = distance[source] = 0;
		while (!q.isEmpty()) {
			int v = q.poll();
			if (v == target) {
				return;
			}
			setState(v, COMPLETED);
			children(v).forEach(w -> {
				q.add(w);
				setState(w, VISITED);
				setParent(w, v);
				distance[w] = distance[v] + 1;
				maxDistance = Math.max(maxDistance, distance[w]);
			});
		}
	}

	/**
	 * The distance of the given vertex from the source.
	 * 
	 * @param v
	 *          some vertex
	 * @return the distance from the source
	 */
	public int getDistance(int v) {
		return distance[v];
	}

	/**
	 * Returns the maximum distance encountered in this traversal.
	 * 
	 * @return the maximum distance
	 */
	public int getMaxDistance() {
		return maxDistance;
	}

	/**
	 * Returns a vertex with maximum distance encountered in this traversal.
	 * 
	 * @return a vertex with maximum distance or empty
	 */
	public Optional<Integer> getMaxDistanceVertex() {
		return graph.vertices().boxed().max(Comparator.comparing(this::getDistance));
	}
}