package maze;

public class Simulate {
	static Vector tempVector;
	
	public static boolean check(Maze maze, Cell current, int absoluteDirection) {
		tempVector = new Vector(current.getX(), current.getY(), absoluteDirection);
		if (tempVector.isOnMap(maze)) {
			return tempVector.isPossible(maze.getCells());
		} else {
			return false;
		}
	}
	
}
