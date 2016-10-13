package de.amr.easy.grid.rendering;

import java.awt.Color;
import java.awt.Font;

import de.amr.easy.grid.api.Direction;

/**
 * Grid rendering model providing sensible default values.
 * 
 * @author Armin Reichert
 */
public class DefaultGridRenderingModel implements GridRenderingModel {

	private Color gridBgColor = Color.BLACK;
	private Color cellTextColor = Color.BLACK;
	private int cellSize = 4;
	private Font textFont = new Font("Dialog", Font.PLAIN, 10);

	public void setGridBgColor(Color gridBgColor) {
		this.gridBgColor = gridBgColor;
	}

	public void setCellTextColor(Color cellTextColor) {
		this.cellTextColor = cellTextColor;
	}

	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}

	public void setTextFont(Font textFont) {
		this.textFont = textFont;
	}

	@Override
	public int getCellSize() {
		return cellSize;
	}

	@Override
	public Color getGridBgColor() {
		return gridBgColor;
	}

	@Override
	public Color getPassageColor(Integer cell, Direction dir) {
		return getCellBgColor(cell);
	}

	@Override
	public int getPassageThickness() {
		return getCellSize() / 2;
	}

	@Override
	public Color getCellBgColor(Integer cell) {
		return Color.WHITE;
	}

	@Override
	public String getCellText(Integer cell) {
		return "";
	}

	@Override
	public Color getCellTextColor() {
		return cellTextColor;
	}

	@Override
	public Font getCellTextFont() {
		return textFont;
	}
}