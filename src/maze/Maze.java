package maze;

import java.util.ArrayList;
import java.util.Stack;

public class Maze {
	private final Cell[][] cells;
	private int width, height;

	public Maze(int width, int height) {
		cells = new Cell[width][height];
		this.width = width;
		this.height = height;
		
		for (int i = 0; i < 100; i++) {
			System.out.println(rand(0,10));
		}
		
		initiateCells();
	}
	
	private void initiateCells() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
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
	
	private int rand(int a, int b) {
		return (int)(Math.random() * (b + 1) + a);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void random() {
		
		int visitedCells = 0;
		int totalCells = width * height;
		int x, y;
		
		Stack<Cell> stack = new Stack<Cell>();
		Cell current = new Cell(rand(0, width - 1), rand(0, height - 1));
		
		ArrayList<Vector> neighbours = new ArrayList<Vector>();
		Vector tempVector;
		
		while (visitedCells < totalCells) {
			
			Main.g.repaint();
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
			
			neighbours.clear();
			
			x = current.getCoords()[0];
			y = current.getCoords()[1];
			
			
			// North neighbour
			tempVector = new Vector();
			tempVector.setStart(x, y);
			if (y - 1 >= 0 && cells[x][y - 1].checkWalls()) {
				tempVector.setEnd(x, y - 1);
				tempVector.setWalls(0, 2);
				neighbours.add(tempVector);
			}
			
			// East neighbour
			tempVector = new Vector();
			tempVector.setStart(x, y);
			if (x + 1 < width && cells[x + 1][y].checkWalls()) {
				tempVector.setEnd(x + 1, y);
				tempVector.setWalls(1, 3);
				neighbours.add(tempVector);
			}
			
			// South neighbour
			tempVector = new Vector();
			tempVector.setStart(x, y);
			if (y + 1 < height && cells[x][y + 1].checkWalls()) {
				tempVector.setEnd(x, y + 1);
				tempVector.setWalls(2, 0);
				neighbours.add(tempVector);
			}
			
			// West neighbour
			tempVector = new Vector();
			tempVector.setStart(x, y);
			if (x - 1 >= 0 && cells[x - 1][y].checkWalls()) {
				tempVector.setEnd(x - 1, y);
				tempVector.setWalls(3, 1);
				neighbours.add(tempVector);
			}
			
			
			// MAIN
			if (neighbours.size() > 0) {
				
				int i = rand(0, neighbours.size() - 1);
				System.out.println(i);
				tempVector = neighbours.get(i);
				
				cells[tempVector.getStart()[0]][tempVector.getStart()[1]].setWall(tempVector.getWalls()[0], false);
				cells[tempVector.getEnd()[0]][tempVector.getEnd()[1]].setWall(tempVector.getWalls()[1], false);
				
				stack.push(current);
				
				current = cells[tempVector.getEnd()[0]][tempVector.getEnd()[1]];
				
				x = current.getCoords()[0];
				y = current.getCoords()[1];
				
				visitedCells++;
				
			} else if (stack.size() > 0) {
				
				current = stack.pop();
				x = current.getCoords()[0];
				y = current.getCoords()[1];
				
			}
			
		}
		
	}
	
}
