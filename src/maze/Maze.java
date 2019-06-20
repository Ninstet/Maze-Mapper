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
	
	// ------------------------
	// ---- GETTER METHODS ----
	// ------------------------
	
	
	
	// Get the width of the maze in units of cells.
	
	
	public int getWidth() {
		return width;
	}
	
	
	
	// Get the height of the maze in units of cells.
	
	public int getHeight() {
		return height;
	}
	
	
	
	// Get a list of list of cells in the maze. In the first element of the first list is a list of cells in the first row.
	
	public Cell[][] getCells() {
		return cells;
	}
	
	
	
	
	
	
	
	// ----------------------------
	// ---- SIMULATION METHODS ----
	// ----------------------------
	
	
	
	// Randomize the maze such that a path exists between any two cells.
	
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
			
			if (neighbourVectors.size() > 0) {
				
				tempVector = neighbourVectors.get(rand(0, neighbourVectors.size() - 1));
				cells = tempVector.breakWalls(cells);
				
				stack.push(current);
				current = cells[tempVector.getTargetX()][tempVector.getTargetY()];
				
				visitedCells++;
				
			} else if (stack.size() > 0) current = stack.pop();
			
		}
		
	}
	
	
	
	// Randomly break walls in the maze to add additional alternate routes between cells. Probability is of a wall being broken.
	
	public void randomBreak(double probability) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (rand(0, getHeight() * getWidth()) > (getHeight() * getWidth()) * (1 - probability)) {
					Vector tempVector = new Vector(i, j, rand(0,3));
					if (tempVector.isOnMap(this)) tempVector.breakWalls(cells);
				}
			}
		}
	}
	
	
	
	// Begin exploring the maze using depth first search. When required to traverse to cell in stack, use A*.
	
	public void explore(Cell cellStart, Cell cellEnd, boolean simulate) {
		
		Maze simulatedMaze = null;
		if (simulate) {
			simulatedMaze = new Maze(getWidth(), getHeight());
			simulatedMaze.randomize();
			simulatedMaze.randomBreak(0);
			ArrayList<Vector> path = simulatedMaze.shortestPath(cellStart, cellEnd);
			simulatedMaze.displayVectors(path);
			simulatedMaze.colorPath(path, Color.CYAN);
			new Gui(simulatedMaze);
		}
		
		cellEnd.setColor(Color.RED);
		
		Stack<Cell> open = new Stack<Cell>();
		ArrayList<Cell> closed = new ArrayList<Cell>();
		
		open.add(cellStart);
		
		Memory.location = cellStart;
		Memory.orientation = 0;
		
		while (Memory.location != cellEnd) {
			
			Cell target = open.pop();
			traverse(Memory.location, target);
			closed.add(Memory.location);
			
			if (Memory.location != cellEnd) {
				
				for (int i = 3; i <= 5; i++) {
					
					int absoluteOrientation = (i + Memory.orientation) % 4;
					
					if (simulate) {
						if (Simulate.check(simulatedMaze, Memory.location, absoluteOrientation)) {
							tempVector = new Vector(Memory.location.getX(), Memory.location.getY(), absoluteOrientation);
							Cell neighbour = tempVector.getTarget(cells);
							
							tempVector.breakWalls(getCells());
							if (!open.contains(neighbour) && !closed.contains(neighbour)) {
								open.push(neighbour);
								neighbour.setColor(Color.BLUE);
							}
						}
					} else {
						if (Controller.check(absoluteOrientation)) {
							tempVector = new Vector(Memory.location.getX(), Memory.location.getY(), absoluteOrientation);
							Cell neighbour = tempVector.getTarget(cells);
							
							tempVector.breakWalls(getCells());
							if (!open.contains(neighbour) && !closed.contains(neighbour)) {
								open.push(neighbour);
								neighbour.setColor(Color.BLUE);
							}
						}
						
					}
					
				}
				
			} else {
				Maze tempMaze = new Maze(getWidth(), getHeight());
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						Cell cell = tempMaze.getCells()[i][j];
						if (!closed.contains(getCells()[i][j])) {
							cell.setWall(0, false);
							cell.setWall(1, false);
							cell.setWall(2, false);
							cell.setWall(3, false);
						} else {
							tempMaze.getCells()[i][j] = getCells()[i][j];
						}
					}
				}
				tempMaze.colorPath(tempMaze.shortestPath(cellStart, cellEnd), Color.RED);
				tempMaze.displayVectors(tempMaze.shortestPath(cellStart, cellEnd));
				
				if (getPathCoords(shortestPath(cellStart, cellEnd)).equals(tempMaze.getPathCoords(tempMaze.shortestPath(cellStart, cellEnd)))) System.out.println("Shortest path found!");
				
				new Gui(tempMaze);
			}
			
		}
		
		colorPath(shortestPath(cellStart, cellEnd), Color.CYAN);
		Main.g.refresh();
		
	}
	
	
	
	// Get a path (list of vectors) between two cells in the maze. Uses A*.
	
	public ArrayList<Vector> shortestPath(Cell cellStart, Cell cellEnd) {
		ArrayList<Vector> heritage = new ArrayList<Vector>();
		
		ArrayList<Cell> open = new ArrayList<Cell>();
		ArrayList<Cell> closed = new ArrayList<Cell>();
		
		Cell current = cellStart;
		open.add(current);
		
		while (current != cellEnd) {
			
			if (open.size() == 0) break;
			
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
		Collections.reverse(path);
		
//		for (int i = 0; i < closed.size(); i++) closed.get(i).setColor(Color.RED);
//		for (int i = 0; i < open.size(); i++) open.get(i).setColor(Color.GREEN);
//		for (int i = 0; i < path.size(); i++) path.get(i).setColor(Color.CYAN);
		
//		cellStart.setColor(Color.BLUE);
//		cellEnd.setColor(Color.BLUE);
		
		return path;
		
	}
	
	
	
	
	
	
	
	
	
	
	// ---------------------
	// ---- GUI METHODS ----
	// ---------------------
	
	
	
	// Print all the cells in the maze to the console.
	
	public void printCells() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				System.out.println("(" + i + ", " + j + ")");
				cells[i][j].printCell();
				System.out.println("");
			}
		}
	}
	
	
	
	// Set all the cells in the maze to display their cell values on GUI refresh.
	
	public void displayCellValues() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j].displayValues(true);
			}
		}
		Main.g.refresh();
	}
	
	
	
	// Set all the cells in a path (list of vectors) to display their directions on GUI refresh.
	
	public void displayVectors(ArrayList<Vector> path) {
		for (int i = 0; i < path.size(); i++) path.get(i).get(cells).setDirection(path.get(i).getDirection());
		Main.g.refresh();
	}
	
	
	
	
	
	
	
	// -------------------------
	// ---- PRIVATE METHODS ----
	// -------------------------
	
	
	
	// Create a blank 'Cell' object for each cell in the maze.
	
	private void initiateCells() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
	}
	
	
	
	// Determine the path followed from a 'heritage tree' that is a list of parent and child cells.
	
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
	
	
	
	// Get the smallest F valued cell from a list of cells. Gives an index out of bounds error if path not possible.
	
	private static Cell getSmallestFCell(ArrayList<Cell> list, Cell cellStart, Cell cellEnd) {
		int index = 0;
		int min = list.get(index).getF();
		
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i).getF() < min) {
				min = list.get(i).getF();
				index = i;
			}
		}
		
		return list.get(index);
	}
	
	
	
	// Set the color of all the cells in the maze.
	
	private void setAllColors(Color color) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j].setColor(color);
			}
		}
	}
	
	
	
	// Set the color of all the cells in the path.
	
	private void colorPath(ArrayList<Vector> path, Color color) {
		for (int i = 0; i < path.size(); i++) path.get(i).get(getCells()).setColor(color);
		path.get(path.size() - 1).getTarget(cells).setColor(color);
	}
	
	
	
	// Get a list of the coordinates followed by a path (a list of vectors).
	
	private ArrayList<String> getPathCoords(ArrayList<Vector> path) {
		ArrayList<String> coords = new ArrayList<String>();
		for (int i = 0; i < path.size(); i++) {
			coords.add(path.get(i).get(cells).getCoords());
		}
		coords.add(path.get(path.size() - 1).getTarget(cells).getCoords());
		return coords;
	}
	
	
	
	// Animate the motion to a target cell from the current cell in the memory class.
	
	private void traverse(Cell current, Cell target) {
		ArrayList<Vector> path = shortestPath(Memory.location, target);

		System.out.println("--------------------------");
		System.out.println("Location: " + Memory.location.getCoords() + "\nTarget:   " + target.getCoords());
		System.out.println("Path: ");
		for (int i = 0; i < path.size(); i++) System.out.println("    (" + path.get(i).getX() + ", " + path.get(i).getY() + ") --> " + path.get(i).getTarget(cells).getCoords());
		
		for (int i = 0; i < path.size(); i++) {
			Memory.location.setColor(Color.WHITE);
			Memory.location = path.get(i).getTarget(cells);
			Memory.orientation = path.get(i).getDirection();
			Memory.location.setColor(Color.CYAN);
			sleep(150);
			Main.g.refresh();
		}
	}
	
	
	
	
	
	
	
	
	// ----------------------
	// ---- MISC METHODS ----
	// ----------------------
	
	
	
	// Return a random number from a to b (inclusive).
	
	public static int rand(int a, int b) {
		return (int)(Math.random() * (b + 1) + a);
	}
	
	
	
	// Sleep a specified time interval (with error handling).
	
	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
