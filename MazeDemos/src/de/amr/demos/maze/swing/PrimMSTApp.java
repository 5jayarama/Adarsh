package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.rendering.swing.BFSAnimation;
import de.amr.easy.maze.alg.PrimMST;

public class PrimMSTApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new PrimMSTApp());
	}

	public PrimMSTApp() {
		super("Prim-MST Maze");
		setFullscreen(true);
	}

	@Override
	public void run() {
		Integer startCell = grid.cell(TOP_LEFT);
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			changeCellSize(cellSize);
			new PrimMST(grid).accept(startCell);
			new BFSAnimation(canvas, grid).runAnimation(startCell);
			sleep(3000);
			clear();
		});
		System.exit(0);
	}
}