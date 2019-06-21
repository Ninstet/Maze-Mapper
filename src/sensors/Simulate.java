package sensors;

import maze.Cell;
import maze.Maze;
import maze.Vector;

public class Simulate extends Sensor {
	static Vector tempVector;
	
	public static Result check(Maze maze, Cell current, int absoluteOrientation) {
		tempVector = new Vector(current.getX(), current.getY(), absoluteOrientation);
		if (tempVector.isOnMap(maze) && tempVector.isPossible(maze.getCells())) {
			if (tempVector.getTarget(maze.getCells()).isGreen()) {
				return Result.GREEN;
			} else {
				return Result.POSSIBLE;
			}
		} else {
			return Result.WALL;
		}
	}
	
}
