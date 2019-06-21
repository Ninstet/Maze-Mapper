package main;

import maze.Cell;
import maze.Gui;
import maze.Maze;

public class Main {
	public static Gui g = new Gui();
	
	private static final int X_SIZE = 10;
	private static final int Y_SIZE = 10;
	
	public static void main(String[] args) {
		Maze exploredMaze = new Maze(X_SIZE, Y_SIZE);
		
		g.addMaze("Explorer", exploredMaze);
		
		Cell bottomLeft = exploredMaze.getCells()[0][Y_SIZE - 1];
		Cell topRight = exploredMaze.getCells()[Maze.rand(0, X_SIZE - 1)][Maze.rand(0, Y_SIZE - 1)];
		
		exploredMaze.explore(bottomLeft, topRight, true);
		exploredMaze.displayVectors(exploredMaze.shortestPath(bottomLeft, topRight));
			
	}
	
}
