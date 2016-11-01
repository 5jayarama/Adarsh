package de.amr.demos.maze.swing;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.rendering.swing.BFSAnimation;
import de.amr.easy.maze.alg.EllerInsideOut;

public class EllerInsideOutApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new EllerInsideOutApp());
	}

	public EllerInsideOutApp() {
		super("Eller's Algorithm", 64);
		setFullscreen(true);
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			changeCellSize(cellSize);
			new EllerInsideOut(grid).accept(null);
			new BFSAnimation(canvas, grid).runAnimation(grid.cell(GridPosition.TOP_LEFT));
			sleep(1000);
			clear();
		});
	}
}