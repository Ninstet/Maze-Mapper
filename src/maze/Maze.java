package maze;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import lejos.hardware.Keys;
import main.Controller;
import main.Data;
import main.Server;
import main.Memory;
import sensors.Direction;
import sensors.Result;
import sensors.Sensor;
import sensors.Simulate;

public class Maze implements Serializable {
	private static final long serialVersionUID = 4348019366607099374L;
	
	private Cell[][] cells;
	private String title;
	private int width, height;
	
	Vector tempVector;

	public Maze(String title, int width, int height) {
		cells = new Cell[width][height];
		
		this.title = title;
		
		this.width = width;
		this.height = height;
		
		initiateCells();
	}
	
	
	
	
	// ------------------------
	// ---- GETTER METHODS ----
	// ------------------------
	
	
	
	/**
	 * Get the width of the maze in units of cells.
	 * @return Width of the maze.
	 */
	public String getTitle() {
		return title;
	}
	
	
	
	/**
	 * Get the width of the maze in units of cells.
	 * @return Width of the maze.
	 */
	public int getWidth() {
		return width;
	}
	
	
	
	/**
	 * Get the height of the maze in units of cells.
	 * @return Height of the maze.
	 */
	public int getHeight() {
		return height;
	}
	
	
	
	/**
	 * Get a list of list of cells in the maze. In the first element of the first list is a list of cells in the first row.
	 * @return Array of array of cells representing the maze.
	 */
	public Cell[][] getCells() {
		return cells;
	}
	
	
	
	
	
	
	
	// ----------------------------
	// ---- SIMULATION METHODS ----
	// ----------------------------
	
	
	
	/**
	 * Randomize the maze such that a path exists between any two cells.
	 */
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
	
	
	
	/**
	 * Randomly break walls in the maze to add additional alternate routes between cells.
	 * @param probability - Probability of a wall being broken.
	 */
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
	
	
	
	/**
	 * Randomly set cells in a maze to green, meaning you cannot traverse over them.
	 * @param probability - Probability of a cell being made green.
	 */
	public void addGreenCells(double probability) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (rand(0, getHeight() * getWidth()) > (getHeight() * getWidth()) * (1 - probability)) cells[i][j].setGreen(true);
			}
		}
	}
	
	
	
	/**
	 * Begin exploring the maze using depth first search. When required to traverse to cell in stack, use A*.
	 * @param cellStart - The starting cell to explore from.
	 * @param cellEnd - The goal cell you wish to optimise a shortest path to.
	 * @param simulate - True to simulate sensor inputs and maze, false to use robot sensors in real maze.
	 */
	public void explore(Cell cellStart, boolean simulate) {
		Cell cellEnd = null;
		Simulate simulation = null;
		
		if (simulate) {
			cellEnd = this.getCells()[rand(0, getWidth() - 1)][rand(0, getHeight() - 1)];
			simulation = new Simulate(getWidth(), getHeight(), cellStart, cellEnd, 0.7, 0.05);
		}
		
		Stack<Cell> open = new Stack<Cell>();
		ArrayList<Cell> closed = new ArrayList<Cell>();
		
		open.add(cellStart);
		
		Memory.location = cellStart;
		Memory.orientation = 0;
		
		while (!isSmallestPossiblePath(cellStart, cellEnd, closed) && Controller.KEYS.getButtons() != Keys.ID_ESCAPE) {
			
			Cell target = open.pop();
			traverse(target, cellEnd);
			closed.add(Memory.location);
			
			if (Memory.getColor() == Color.RED) {
				Controller.DATA.addLog("End found!"); 
				cellEnd = Memory.location;
			}
			if (cellEnd != null) cellEnd.setColor(Color.RED);
			
			for (int i = 3; i <= 5; i++) {
				int absoluteOrientation = Direction.toAbsoluteDirection(i);
				tempVector = new Vector(Memory.location.getX(), Memory.location.getY(), absoluteOrientation);
				
				if ((simulate ? simulation.check(absoluteOrientation) : Controller.check(this, absoluteOrientation)) == Result.POSSIBLE) {
					Cell neighbour = tempVector.getTarget(cells);
					
					tempVector.breakWalls(cells);
					if (!open.contains(neighbour) && !closed.contains(neighbour)) {
						open.push(neighbour);
						neighbour.setColor(Color.BLUE);
					}
				} else if ((simulate ? simulation.check(absoluteOrientation) : Controller.check(this, absoluteOrientation)) == Result.GREEN) {
					tempVector.get(cells).setGreen(true);
					Memory.orientation = (Memory.orientation + 2) % 4;
					tempVector = new Vector(Memory.location.getX(), Memory.location.getY(), Memory.orientation);
					traverse(tempVector.getTarget(cells), cellEnd);
					break;
				}
				
			}
			
		}
		
		colorPath(shortestPath(cellStart, cellEnd), Color.CYAN);
		displayVectors(shortestPath(cellStart, cellEnd));
		Server.uploadMaze(this);
	}
	
	
	
	/**
	 * Get a path (list of vectors) between two cells in the maze. Uses A*.
	 * @param cellStart - Cell to start the path from.
	 * @param cellEnd - Cell to end the path at.
	 * @return The shortest path between the cells.
	 */
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
						
						if (tempVector.isPossible(cells) && !closed.contains(neighbour) && !neighbour.isGreen()) {
							
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
	
	
	
	/**
	 * Print all the cells in the maze to the console.
	 */
	public void printCells() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				System.out.println("(" + i + ", " + j + ")");
				cells[i][j].printCell();
				System.out.println("");
			}
		}
	}
	
	
	
	/**
	 * Set all the cells in the maze to display their cell values on GUI refresh.
	 */
	public void displayCellValues() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j].displayValues(true);
			}
		}
		Server.uploadMaze(this);
	}
	
	
	
	/**
	 * Set all the cells in a path (list of vectors) to display their directions on GUI refresh.
	 * @param path - Path (list of vectors) to display the vectors over.
	 */
	public void displayVectors(ArrayList<Vector> path) {
		for (int i = 0; i < path.size(); i++) path.get(i).get(cells).setDirection(path.get(i).getDirection());
		Server.uploadMaze(this);
	}
	
	
	
	/**
	 * Set the color of all the cells in the path.
	 * @param path - The path (list of vectors) you wish to set the color of cells for.
	 * @param color - The color you wish to set the cells to.
	 */
	public void colorPath(ArrayList<Vector> path, Color color) {
		for (int i = 0; i < path.size(); i++) path.get(i).get(getCells()).setColor(color);
		path.get(path.size() - 1).getTarget(cells).setColor(color);
	}
	
	
	
	
	
	
	
	// -------------------------
	// ---- PRIVATE METHODS ----
	// -------------------------
	
	
	
	/**
	 * Create a blank 'Cell' object for each cell in the maze.
	 */
	private void initiateCells() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
	}
	
	
	
	/**
	 * Determine the path followed from a 'heritage tree' that is a list of parent and child cells.
	 * @param cellStart - Cell the path starts from.
	 * @param cellEnd - Cell the path ends at.
	 * @param heritage - Heritage tree (list of vectors) determined by the A* algorithm.
	 * @return The path as a list of vectors.
	 */
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
	
	
	
	/**
	 * Determine if a path between 2 cells is the shortest possible path in a maze not fully explored. This is done by assuming all unexplored cells are empty and hence traversable.
	 * @param cellStart - Cell the path starts from.
	 * @param cellEnd - Cell the path ends at.
	 * @param closed - List of unexplored cells to assume are empty.
	 * @return True if the path between 2 cells is the shortest. False if there could be a shorter path, and hence more data is required to be sure.
	 */
	private boolean isSmallestPossiblePath(Cell cellStart, Cell cellEnd, ArrayList<Cell> closed) {
		if (cellEnd == null) return false;
		Maze tempMaze = new Maze("Simulation", this.width, this.height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tempMaze.getCells()[i][j] = this.getCells()[i][j].clone();
				if (!closed.contains(getCells()[i][j])) {
					tempMaze.getCells()[i][j].setWall(0, false);
					tempMaze.getCells()[i][j].setWall(1, false);
					tempMaze.getCells()[i][j].setWall(2, false);
					tempMaze.getCells()[i][j].setWall(3, false);
				}
			}
		}
		//this.colorPath(tempMaze.shortestPath(cellStart, cellEnd), Color.RED);
		//this.displayVectors(tempMaze.shortestPath(cellStart, cellEnd));
		
		return getPathCoords(shortestPath(cellStart, cellEnd)).equals(tempMaze.getPathCoords(tempMaze.shortestPath(cellStart, cellEnd)));
	}
	
	
	
	/**
	 * Get the smallest F valued cell from a list of cells. Gives an index out of bounds error if path not possible.
	 * @param list - List of cells to find the smallest F value in.
	 * @param cellStart - Cell the A* search would start from.
	 * @param cellEnd - Cell the A* search would end at.
	 * @return The cell with the smallest F value.
	 */
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
	
	
	
	/**
	 * Set the color of all the cells in the maze.
	 * @param color - The color you wish to set all cells to.
	 */
	private void setAllColors(Color color) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j].setColor(color);
			}
		}
	}
	
	
	
	/**
	 * Get a list of the coordinates followed by a path (a list of vectors).
	 * @param path - The path (list of vectors) over which you wish to find the coordinates of the cells for.
	 * @return A list of coordinates representing the path followed.
	 */
	private ArrayList<String> getPathCoords(ArrayList<Vector> path) {
		ArrayList<String> coords = new ArrayList<String>();
		for (int i = 0; i < path.size(); i++) {
			coords.add(path.get(i).get(cells).getCoords());
		}
		if (path.size() > 0) coords.add(path.get(path.size() - 1).getTarget(cells).getCoords());
		return coords;
	}
	
	
	
	/**
	 * Animate the motion to a target cell from the current cell in the memory class.
	 * @param target - The cell you wish to traverse to.
	 * @param cellEnd - The end cell in the exploration (to ensure it remains red).
	 */
	private void traverse(Cell target, Cell cellEnd) {
		ArrayList<Vector> path = shortestPath(Memory.location, target);

//		System.out.println("--------------------------");
//		System.out.println("Location: " + Memory.location.getCoords() + "\nTarget:   " + target.getCoords());
//		System.out.println("Path: ");
//		for (int i = 0; i < path.size(); i++) System.out.println("    (" + path.get(i).getX() + ", " + path.get(i).getY() + ") --> " + path.get(i).getTarget(cells).getCoords());
		
		for (int i = 0; i < path.size(); i++) {
			Memory.location.setColor(Color.WHITE);
			if (Memory.location.isGreen()) Memory.location.setColor(Color.GREEN);
			if (Memory.location == cellEnd) Memory.location.setColor(Color.RED);
			
			Controller.IR_SENSOR.look(Direction.FORWARD);
			Controller.turn(path.get(i).getDirection());
			Controller.nextCell();
			
			Memory.location.setDirection(-1);
			Memory.location = path.get(i).getTarget(cells);
			Memory.orientation = path.get(i).getDirection();
			Memory.location.setDirection(Memory.orientation);
			Memory.location.setColor(Color.ORANGE);
			
//			sleep(200);
			Server.uploadMaze(this);
		}
	}
	
	
	
	
	
	
	
	
	// ----------------------
	// ---- MISC METHODS ----
	// ----------------------
	
	
	
	/**
	 * Return a random number from a to b (inclusive).
	 * @param a - The lower bound for the random number.
	 * @param b - The upper bound for the random number.
	 * @return The random integer.
	 */
	public static int rand(int a, int b) {
		return (int)(Math.random() * (b + 1) + a);
	}
	
	
	
	/**
	 * Sleep a specified time interval (with error handling).
	 * @param ms - The time you wish the program to idle for (in milliseconds).
	 */
	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
