package maze;

import java.awt.Color;
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
	
	
	// ---- GETTER METHODS ----
	
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
	
	public void displayCellValues(Gui g) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j].displayValues(true);
			}
		}
		
		g.refresh();
	}
	
	
	// ---- SIMULATION METHODS ----
	
	public void randomize() {
		
		int visitedCells = 1;
		int totalCells = width * height;
		
		Stack<Cell> stack = new Stack<Cell>();
		Cell current = new Cell(rand(0, width - 1), rand(0, height - 1));
		
		ArrayList<Vector> neighbourVectors = new ArrayList<Vector>();
		Vector tempVector;
		
		while (visitedCells < totalCells) {
			
			neighbourVectors.clear();
			
			for (int i = 0; i < 4; i++) {
				tempVector = new Vector(current.getX(), current.getY(), i);
				if (tempVector.isOnMap(this) && tempVector.getTarget(cells).checkWalls()) neighbourVectors.add(tempVector);
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
	
	public void shortestPath(Cell cellStart, Cell cellEnd) {
		
		ArrayList<Vector> heritage = new ArrayList<Vector>();
		Vector tempVector;
		
		ArrayList<Cell> open = new ArrayList<Cell>();
		ArrayList<Cell> closed = new ArrayList<Cell>();
		
		Cell current = cellStart;
		open.add(current);
		
		while (current != cellEnd) {
			
			current = getSmallestFCell(open, cellStart, cellEnd);
			
			open.remove(current);
			closed.add(current);
			
			if (current != cellEnd) {
				
				for (int i = 0; i < 4; i++) {
					tempVector = new Vector(current.getX(), current.getY(), i);
					
					if (tempVector.isOnMap(this)) {
						Cell neighbour = tempVector.getTarget(cells);
						
						if (tempVector.isPossible(cells) && !closed.contains(neighbour)) {
							if (neighbour.getNewG(current) < neighbour.getG() || !open.contains(neighbour)) {
								neighbour.update(current, cellEnd);
								heritage.add(tempVector);
								
								if (!open.contains(neighbour)) open.add(neighbour);
							}
						}
						
					}
					
				}
				
			}
			
		}
		
		
		ArrayList<Cell> path = getPath(cellStart, cellEnd, heritage);
		
		for (int i = 0; i < closed.size(); i++) closed.get(i).setColor(Color.RED);
		for (int i = 0; i < open.size(); i++) open.get(i).setColor(Color.GREEN);
		for (int i = 0; i < path.size(); i++) path.get(i).setColor(Color.CYAN);
		
		cellStart.setColor(Color.BLUE);
		cellEnd.setColor(Color.BLUE);
		
	}
	
	private ArrayList<Cell> getPath(Cell cellStart, Cell cellEnd, ArrayList<Vector> heritage) {
		Vector tempVector;
		
		ArrayList<Cell> path = new ArrayList<Cell>();
		
		if (heritage.size() > 0) {
			int lastX = heritage.get(heritage.size() - 1).getX();
			int lastY = heritage.get(heritage.size() - 1).getY();
			for (int i = heritage.size() - 1; i >= 0; i--) {
				tempVector = heritage.get(i);
				if (tempVector.getTargetX() == lastX && tempVector.getTargetY() == lastY) {
					path.add(tempVector.getTarget(cells));
					lastX = tempVector.getX();
					lastY = tempVector.getY();
				}
			}
		}
		
		return path;
	}
	
	private void initiateCells() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
	}
	
	private static Cell getSmallestFCell(ArrayList<Cell> array, Cell cellStart, Cell cellEnd) {
		int index = 0;
		int min = array.get(index).getF();
		
		for (int i = 1; i < array.size(); i++) {
			if (array.get(i).getF() < min) {
				min = array.get(i).getF();
				index = i;
			}
		}
		
		return array.get(index);
	}
	
	public static int rand(int a, int b) {
		return (int)(Math.random() * (b + 1) + a);
	}
	
}
