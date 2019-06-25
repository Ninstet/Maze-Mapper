package sensors;

import java.awt.Color;
import java.util.ArrayList;

import main.Memory;
import maze.Cell;
import maze.Maze;
import maze.Vector;

public class Simulate extends Sensor {
	private Maze simulatedMaze;
	
	public Simulate(int width, int height, Cell cellStart, Cell cellEnd, double breakProb, double greenProb) {
		simulatedMaze = new Maze(width, height);
//		Server.g.addMaze("Solution", simulatedMaze);
		
		simulatedMaze.randomize();
		simulatedMaze.randomBreak(breakProb);
		simulatedMaze.addGreenCells(greenProb);
		
		ArrayList<Vector> path = simulatedMaze.shortestPath(cellStart, cellEnd);
		simulatedMaze.displayVectors(path);
		simulatedMaze.colorPath(path, Color.CYAN);
	}
	
	public Result check(int absoluteDirection) {
		Vector tempVector = new Vector(Memory.location.getX(), Memory.location.getY(), absoluteDirection);
		
		if (tempVector.get(simulatedMaze.getCells()).isGreen()) return Result.GREEN;
		
		if (tempVector.isOnMap(simulatedMaze) && tempVector.isPossible(simulatedMaze.getCells())) {
			return Result.POSSIBLE;
		} else {
			return Result.WALL;
		}
	}
	
}
