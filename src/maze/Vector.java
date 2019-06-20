package maze;

import java.util.ArrayList;

public class Vector {
	private int x1, y1;
	private int x2, y2;
	private int direction;
	
	public Vector(int x1, int y1, int direction) {
		this.x1 = x1;
		this.y1 = y1;
		this.direction = direction;
		
		if (direction == 0) {
			this.x2 = x1;
			this.y2 = y1 - 1;
		} else if (direction == 1) {
			this.x2 = x1 + 1;
			this.y2 = y1;
		} else if (direction == 2) {
			this.x2 = x1;
			this.y2 = y1 + 1;
		} else if (direction == 3) {
			this.x2 = x1 - 1;
			this.y2 = y1;
		}
	}
	
	public Vector(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		
		if (y1 > y2) {
			direction = 0;
		} else if (x1 < x2) {
			direction = 1;
		} else if (y1 < y2) {
			direction = 2;
		} else if (x1 > x2) {
			direction = 3;
		}
	}
	
	public Cell get(Cell[][] cells) {
		return cells[x1][y1];
	}
	
	public int getX() {
		return x1;
	}
	
	public int getY() {
		return y1;
	}
	
	public Cell getTarget(Cell[][] cells) {
		return cells[x2][y2];
	}
	
	public int getTargetX() {
		return x2;
	}
	
	public int getTargetY() {
		return y2;
	}
	
	public void printVector() {
		System.out.println("(" + getX() + ", " + getY() + ") --> (" + getTargetX() + ", " + getTargetY() + ")");
	}
	
	public int getDirection() {
		return direction;
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
	
	public boolean isOnMap(Maze maze) {
		return x2 >= 0 && x2 < maze.getWidth() && y2 >= 0 && y2 < maze.getHeight();
	}
	
	public boolean isPossible(Cell[][] cells) {
		if (y1 > y2) {
			return !cells[x1][y1].getWalls()[0] && !cells[x2][y2].getWalls()[2];
		} else if (x1 < x2) {
			return !cells[x1][y1].getWalls()[1] && !cells[x2][y2].getWalls()[3];
		} else if (y1 < y2) {
			return !cells[x1][y1].getWalls()[2] && !cells[x2][y2].getWalls()[0];
		} else if (x1 > x2) {
			return !cells[x1][y1].getWalls()[3] && !cells[x2][y2].getWalls()[1];
		} else {
			return false;
		}
	}
	
}
