
public class Point extends Object {
	public double x;
	public double y;
	
	Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/*
	 * Point() { this.x = 0; this.y = 0; }
	 */
	public Point clone() {
		Point point = new Point(this.x, this.y);
		return point;
	}
}
