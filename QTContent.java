import org.mariuszgromada.math.mxparser.Expression;

public class QTContent {		// Values stored in each quadtree node
	private Integer x, y;		// Horizontal & vertical coordinates of current node
	private Double result;		// Value of equation evaluated at current coordinates
	private Boolean visited;	// Shows whether node was used already in graphing borders

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	public Boolean getVisited() {
		return visited;
	}

	public void setVisited(Boolean visited) {
		this.visited = visited;
	}

	public QTContent() {	// Create new node with no values (check: may/not need this)
		super();
		this.x = (Integer) null;
		this.y = (Integer) null;
		this.result = (Double) null;
		this.visited = false;
	}

	public QTContent(Integer x, Integer y, Double result) {
		super();
		this.x = x;
		this.y = y;
		this.result = result;
		this.visited = false;	// Set separately, so default false at creation
	}

	public QTContent(Integer x, Integer y, String function) {
		super();
		String frmlRplc = function.replaceAll("x", String.valueOf(x)).replaceAll("y", String.valueOf(y));
		Expression expression = new Expression(frmlRplc);
		Double result = expression.calculate();

		this.x = x;
		this.y = y;
		this.result = result;
		this.visited = false;	// Set separately, so default false at creation
	}

	public QTContent copyQTC(QTContent fromQTC) {
		QTContent toQTC = new QTContent();
		toQTC.x = fromQTC.getX();
		toQTC.y = fromQTC.getY();
		toQTC.result = fromQTC.getResult();
		toQTC.visited = fromQTC.visited;

		return toQTC;
	}
}
