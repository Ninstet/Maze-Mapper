package maze;

public class Vector {
	private int x1, y1;
	private int x2, y2;
	
	public Vector(int x1, int y1) {
		this.x1 = x1;
		this.y1 = y1;
	}
	
	public void setTarget(int x2, int y2) {
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public int getTargetX() {
		return x2;
	}
	
	public int getTargetY() {
		return y2;
	}
	
	public Cell[][] breakWalls(Cell[][] cells) {
		if (y1 > y2) {
			cells[x1][y1].setWall(0, false);
			cells[x2][y2].setWall(2, false);
		} else if (x1 < x2) {
			cells[x1][y1].setWall(1, false);
			cells[x2][y2].setWall(3, false);
		} else if (y1 < y2) {
			cells[x1][y1].setWall(2, false);
			cells[x2][y2].setWall(0, false);
		} else if (x1 > x2) {
			cells[x1][y1].setWall(3, false);
			cells[x2][y2].setWall(1, false);
		}
		
		return cells;
	}
	
}
