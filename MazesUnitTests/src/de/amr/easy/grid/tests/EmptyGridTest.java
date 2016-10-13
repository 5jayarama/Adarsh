package de.amr.easy.grid.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.Grid;

public class EmptyGridTest {

	private Grid2D<Integer, DefaultEdge<Integer>> grid;

	@Before
	public void setUp() {
		grid = new Grid(0, 0);
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testGridSize() {
		assertEquals(grid.edgeCount(), 0);
		assertEquals(grid.vertexCount(), 0);
		assertEquals(grid.numCols(), 0);
		assertEquals(grid.numRows(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGridAccessException() {
		grid.cell(0, 0);
	}

	@Test
	public void testGridEdgeStream() {
		assertTrue(grid.edgeStream().count() == 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGridEdgeAccess() {
		grid.edge(0, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGridEdgeAdd() {
		grid.addEdge(0, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGridVertexDegree() {
		grid.degree(0);
	}
}