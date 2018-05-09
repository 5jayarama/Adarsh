package de.amr.demos.grid.curves;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.util.GridUtils.log;

import java.util.stream.IntStream;

import de.amr.easy.grid.curves.PeanoCurve;
import de.amr.easy.grid.impl.Topologies;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;

public class PeanoCurveApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new PeanoCurveApp());
	}

	public PeanoCurveApp() {
		super(243 * 4, 243 * 4, 4, Topologies.TOP4);
		setAppName("Peano Curve");
	}

	@Override
	public void run() {
		IntStream.of(3, 9, 81, 243).forEach(n -> {
			resizeGrid(canvasSize.width / n);
			int startCell = grid.cell(BOTTOM_LEFT);
			new PeanoCurve(log(3, n)).traverse(grid, startCell, this::addEdge);
			SwingBFSAnimation bfs = new SwingBFSAnimation(canvas, grid);
			bfs.setDistancesVisible(false);
			bfs.run(startCell);
			sleep(1000);
		});
	}
}