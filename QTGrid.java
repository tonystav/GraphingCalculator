
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

	// Method to check given grid area for any negative values, which are function solutions.
	// Area defined as square block of equal # rows & columns, with upper left starting point &
	// area size provided as parameters. Method treats entire area as single block, so any match
	// in search area counts for entire area.
	public Boolean isThisAreaOccupied(Integer startHere, Integer checkThisMany) {
		Boolean areaIsOccupied = false;

		for (int row=startHere; row<checkThisMany && !areaIsOccupied; row++) {
			for (int col=startHere; col<checkThisMany && !areaIsOccupied; col++) {
				if ((null != QTGrid[row][col].getResult()) && (0 > QTGrid[row][col].getResult())) {
					areaIsOccupied = true;
					break;
				}
			}
		}

		return areaIsOccupied;
	}
}
