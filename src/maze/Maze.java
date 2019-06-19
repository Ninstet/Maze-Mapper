package maze;

import java.util.ArrayList;
import java.util.Stack;

public class Maze {
	private Cell[][] cells;
	private int width, height;

	public Maze(int width, int height) {
		cells = new Cell[width][height];
		
		this.width = width;
		this.height = height;
		
		initiateCells();
	}
	
	
	// ---- PUBLIC METHODS ----
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Cell[][] getCells() {
		return cells;
	}
	
	public void printCells() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				System.out.println("(" + i + ", " + j + ")");
				cells[i][j].printCell();
				System.out.println("");
			}
		}
	}
	
	public void randomise() {
		
		int visitedCells = 1;
		int totalCells = width * height;
		
		Stack<Cell> stack = new Stack<Cell>();
		Cell current = new Cell(rand(0, width - 1), rand(0, height - 1));
		
		ArrayList<Vector> neighbourVectors = new ArrayList<Vector>();
		Vector tempVector;
		
		while (visitedCells < totalCells) {
			
			neighbourVectors.clear();
			
			int x = current.getX();
			int y = current.getY();
			
			// North neighbour
			tempVector = new Vector(x, y);
			if (y - 1 >= 0 && cells[x][y - 1].checkWalls()) {
				tempVector.setTarget(x, y - 1);
				neighbourVectors.add(tempVector);
			}
			
			// East neighbour
			tempVector = new Vector(x, y);
			if (x + 1 < width && cells[x + 1][y].checkWalls()) {
				tempVector.setTarget(x + 1, y);
				neighbourVectors.add(tempVector);
			}
			
			// South neighbour
			tempVector = new Vector(x, y);
			if (y + 1 < height && cells[x][y + 1].checkWalls()) {
				tempVector.setTarget(x, y + 1);
				neighbourVectors.add(tempVector);
			}
			
			// West neighbour
			tempVector = new Vector(x, y);
			if (x - 1 >= 0 && cells[x - 1][y].checkWalls()) {
				tempVector.setTarget(x - 1, y);
				neighbourVectors.add(tempVector);
			}
			
			// MAIN
			if (neighbourVectors.size() > 0) {
				
				tempVector = neighbourVectors.get(rand(0, neighbourVectors.size() - 1));
				cells = tempVector.breakWalls(cells);
				
				stack.push(current);
				current = cells[tempVector.getTargetX()][tempVector.getTargetY()];
				
				visitedCells++;
				
			} else if (stack.size() > 0) current = stack.pop();
			
		}
		
	}
	
	
	// ---- PRIVATE METHODS ----
	
	private int rand(int a, int b) {
		return (int)(Math.random() * (b + 1) + a);
	}
	
	private void initiateCells() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
	}
	
}
