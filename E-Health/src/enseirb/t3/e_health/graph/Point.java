package enseirb.t3.e_health.graph;

import java.util.Date;

public class Point {
	private Date x;
	private int y;
	
	public Point(Date x, int y) {
		this.x = x;
		this.y = y;
	}

	public Date getX() {
		return x;
	}

	public void setX(Date x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
