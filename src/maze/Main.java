package maze;

public class Main {
	public static Gui g;
	
	private static final int X_SIZE = 10;
	private static final int Y_SIZE = 10;
	
	public static void main(String[] args) {
//		while (true) {
			Maze exploredMaze = new Maze(X_SIZE, Y_SIZE);
			g = new Gui(exploredMaze);
			
			Cell bottomLeft = exploredMaze.getCells()[0][Y_SIZE - 1];
			Cell topRight = exploredMaze.getCells()[X_SIZE - 1][0];
			
			
			exploredMaze.explore(bottomLeft, topRight, true);
			exploredMaze.displayVectors(exploredMaze.shortestPath(bottomLeft, topRight));
			g.refresh();
//			exploredMaze.displayCellValues();
//		}
	}
	
//	public static void pathFromOriginTo(int x, int y) {
//		maze.shortestPath(maze.getCells()[0][0], maze.getCells()[Math.round(x / (g.getHeight() / maze.getHeight()))][Math.round(y / (g.getHeight() / maze.getHeight()))]);
//		g.refresh();
//	}
	
}
