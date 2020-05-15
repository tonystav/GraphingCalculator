
public class QTGrid {
	private QTContent[][] QTGrid;

	// Constructor for variable-sized grids
	public QTGrid(Integer gridSize) {
		this.QTGrid = new QTContent[gridSize][gridSize];
	}

	public QTContent getGridElement(int gridRow, int gridCol) {
		return QTGrid[gridRow][gridCol];
	}

	public void setGridElement(int gridRow, int gridCol, Integer xValue, Integer yValue, Double result) {
		QTGrid[gridRow][gridCol] = new QTContent(xValue, yValue, result);
	}

	public void initializeGridElement(int gridRow, int gridCol) {
		QTGrid[gridRow][gridCol] = new QTContent(0, 0, 0D);
	}

	public Boolean isElementNull(int gridRow, int gridCol) {
		if (null == QTGrid[gridRow][gridCol]) { return true; }
		else {return false; }
	}

	// Method to check given grid area for any negative values, which are function solutions.
	// Area defined as square block of equal # rows & columns, with upper left starting point &
	// area size provided as parameters. Method treats entire area as single block, so any match
	// in search area counts for entire area.
	public Boolean isThisAreaOccupied(Integer xStart, Integer yStart, Integer checkThisMany) {
		Boolean areaIsOccupied = false;

		for (int row=xStart; row<checkThisMany && !areaIsOccupied; row++) {
			for (int col=yStart; col<checkThisMany && !areaIsOccupied; col++) {
				if ((null != QTGrid[row][col].getResult()) && (0 > QTGrid[row][col].getResult())) {
					areaIsOccupied = true;
					break;
				}
			}
		}

		return areaIsOccupied;
	}
}
