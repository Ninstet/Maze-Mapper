package maze;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import controller.Controller;
import controller.Memory;

public class Maze {
	private Cell[][] cells;
	private int width, height;
	
	Vector tempVector;

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
	
	public void explore(Cell cellStart, Cell cellEnd, boolean simulate) {
		
		Maze simulatedMaze = null;
		if (simulate) {
			simulatedMaze = new Maze(10, 10);
			simulatedMaze.randomize();
//			ArrayList<Vector> path = simulatedMaze.shortestPath(simulatedMaze.getCells()[0][0], simulatedMaze.getCells()[1][1]);
//			for (int i = 0; i < path.size(); i++) System.out.println("(" + path.get(i).getX() + ", " + path.get(i).getY() + ") --> " + path.get(i).getTarget(cells).getCoords());
//			Gui g = new Gui(simulatedMaze);
		}
		
		Stack<Cell> open = new Stack<Cell>();
		ArrayList<Cell> closed = new ArrayList<Cell>();
		
		open.add(cellStart);
		Cell target;
		
		Memory.location = cellStart;
		Memory.orientation = 0;
		
		while (Memory.location != cellEnd) {
			
			for (int i = 0; i < open.size(); i++) open.get(i).setColor(Color.GREEN);
			
			target = open.pop();
			
			ArrayList<Vector> path = shortestPath(Memory.location, target);
			
			System.out.println("Location: " + Memory.location.getCoords() + "\nTarget:   " + target.getCoords());
			System.out.println("Path: ");
			for (int i = 0; i < path.size(); i++) System.out.println("    (" + path.get(i).getX() + ", " + path.get(i).getY() + ") --> " + path.get(i).getTarget(cells).getCoords());
			System.out.println("----------------------");
			
			for (int i = 0; i < path.size(); i++) {
				Memory.location.setColor(Color.WHITE);
				Memory.location = path.get(i).getTarget(cells);
				Memory.orientation = path.get(i).getDirection();
				Memory.location.setColor(Color.CYAN);
				sleep(100);
				Main.g.refresh();
			}
			
			closed.add(Memory.location);
			
			if (Memory.location != cellEnd) {
				
				for (int i = 1; i >= -1; i--) {
					int d = i;
					if (i == -1) d = 3;
					
					int absoluteOrientation = (d + Memory.orientation) % 4;
					
					if (simulate) {
						if (Simulate.check(simulatedMaze, Memory.location, absoluteOrientation)) {
							tempVector = new Vector(Memory.location.getX(), Memory.location.getY(), absoluteOrientation);
							Cell neighbour = tempVector.getTarget(cells);
							
							tempVector.breakWalls(getCells());
							if (!open.contains(neighbour) && !closed.contains(neighbour)) {
								open.push(neighbour);
								neighbour.setColor(Color.GREEN);
							}
						}
					} else {
						if (Controller.check(absoluteOrientation)) {
							tempVector = new Vector(Memory.location.getX(), Memory.location.getY(), absoluteOrientation);
							Cell neighbour = tempVector.getTarget(cells);
							
							tempVector.breakWalls(getCells());
							if (!open.contains(neighbour) && !closed.contains(neighbour)) {
								open.push(neighbour);
								neighbour.setColor(Color.GREEN);
							}
						}
						
					}
					
				}
				
			}
			
		}
		
	}
	
	public ArrayList<Vector> shortestPath(Cell cellStart, Cell cellEnd) {
		ArrayList<Vector> heritage = new ArrayList<Vector>();
		
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
		ArrayList<Vector> path = getPath(cellStart, cellEnd, heritage);
		
//		for (int i = 0; i < closed.size(); i++) closed.get(i).setColor(Color.RED);
//		for (int i = 0; i < open.size(); i++) open.get(i).setColor(Color.GREEN);
//		for (int i = 0; i < path.size(); i++) path.get(i).setColor(Color.CYAN);
		
//		cellStart.setColor(Color.BLUE);
//		cellEnd.setColor(Color.BLUE);
		
		Collections.reverse(path);
		
		return path;
		
	}
	
	
	// ---- PRIVATE METHODS ----
	
	private ArrayList<Vector> getPath(Cell cellStart, Cell cellEnd, ArrayList<Vector> heritage) {
		ArrayList<Vector> path = new ArrayList<Vector>();
		
		if (heritage.size() > 1) {
			int lastX = cellEnd.getX();
			int lastY = cellEnd.getY();
			
			for (int i = heritage.size() - 1; i >= 0; i--) {
				tempVector = heritage.get(i);
				if (tempVector.getTargetX() == lastX && tempVector.getTargetY() == lastY) {
					path.add(tempVector);
					lastX = tempVector.getX();
					lastY = tempVector.getY();
				}
			}
		} else {
			path.add(new Vector(cellStart.getX(), cellStart.getY(), cellEnd.getX(), cellEnd.getY()));
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
	
	private void setAllColors(Color color) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j].setColor(color);
			}
		}
	}
	
	
	// ---- MISC METHODS ----
	
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
	
	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
