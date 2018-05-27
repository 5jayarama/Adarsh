package de.amr.easy.grid.ui.swing;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static java.awt.EventQueue.invokeLater;
import static java.lang.String.format;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Topology;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.util.GridUtils;

/**
 * Base class for grid sample applications.
 * 
 * @author Armin Reichert
 */
public abstract class SwingGridSampleApp extends JFrame implements Runnable {

	public static void launch(SwingGridSampleApp app) {
		try {
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		invokeLater(app::createAndShowUI);
	}

	protected final Dimension canvasSize;
	protected String appName;
	protected boolean fullscreen;
	protected ObservableGrid<TraversalState, Integer> grid;
	protected ObservingGridCanvas canvas;
	protected ConfigurableGridRenderer renderer = new ConfigurableGridRenderer();

	private void configureRenderer() {
		renderer.fnCellBgColor = cell -> {
			switch (grid.get(cell)) {
			case VISITED:
				return Color.BLUE;
			case COMPLETED:
				return Color.WHITE;
			case UNVISITED:
				return renderer.getGridBgColor();
			default:
				return Color.BLACK;
			}
		};
		renderer.fnPassageWidth = () -> Math.max(1, renderer.getCellSize() / 4);
	}

	/**
	 * Creates a sample application using a window of specified width and height. The grid is
	 * dimensioned such that it fits into the window with the given grid cell size.
	 * 
	 * @param canvasWidth
	 *          the canvas width
	 * @param canvasHeight
	 *          the canvas height
	 * @param cellSize
	 *          the grid cell size
	 */
	public SwingGridSampleApp(int canvasWidth, int canvasHeight, int cellSize, Topology top) {
		fullscreen = false;
		canvasSize = new Dimension(canvasWidth, canvasHeight);
		grid = new ObservableGrid<>(canvasWidth / cellSize, canvasHeight / cellSize, top, UNVISITED, false);
		configureRenderer();
	}

	/**
	 * Creates a sample application using the full screen. The grid is dimensioned such that it fits
	 * into the screen with the given grid cell size.
	 * 
	 * @param cellSize
	 *          the grid cell size
	 */
	public SwingGridSampleApp(int cellSize, Topology top) {
		fullscreen = true;
		canvasSize = GridUtils.getScreenResolution();
		grid = new ObservableGrid<>(canvasSize.width / cellSize, canvasSize.height / cellSize, top, UNVISITED, false);
		configureRenderer();
	}

	private void createAndShowUI() {
		canvas = new ObservingGridCanvas(grid, renderer);
		canvas.setBackground(Color.BLACK);
		canvas.setDelay(0);
		canvas.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
		canvas.getActionMap().put("exit", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		add(canvas, BorderLayout.CENTER);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(createTitle());
		setBackground(Color.BLACK);
		if (fullscreen) {
			setPreferredSize(canvasSize);
			setSize(canvasSize);
			setUndecorated(true);
			setAlwaysOnTop(true);
		} else {
			canvas.setPreferredSize(canvasSize);
			pack();
		}
		setLocationRelativeTo(null);
		setVisible(true);

		// start animation
		new Thread(this).start();
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
		setTitle(createTitle());
	}

	private String createTitle() {
		return format("%s [%d cols %d rows %d cells @%d px]", appName, grid.numCols(), grid.numRows(), grid.numCells(),
				renderer.getCellSize());
	}

	/**
	 * Sets the cell size which resizes the grid to fit the window size. Can only be called after the
	 * window has been created.
	 * 
	 * @param cellSize
	 *          new grid cell size
	 */
	public void resizeGrid(int cellSize) {
		if (renderer.getCellSize() == cellSize) {
			return;
		}
		grid = new ObservableGrid<>(canvasSize.width / cellSize, canvasSize.height / cellSize, grid.getTopology(),
				grid.getDefaultContent(), false);
		canvas.setGrid(grid);
		renderer.fnCellSize = () -> cellSize;
		canvas.adaptSize();
		setTitle(createTitle());
	}

	public void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void addEdge(int u, int v) {
		grid.set(u, COMPLETED);
		grid.addEdge(u, v);
		grid.set(v, COMPLETED);
	}
}