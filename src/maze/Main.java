package maze;

public class Main {
	public static Gui g;
	
	public static void main(String[] args) {
		while (true) {
			Maze exploredMaze = new Maze(10, 10);
			g = new Gui(exploredMaze);
			exploredMaze.explore(exploredMaze.getCells()[0][9], exploredMaze.getCells()[9][0], true);
		}
	}
	
//	public static void pathFromOriginTo(int x, int y) {
//		maze.shortestPath(maze.getCells()[0][0], maze.getCells()[Math.round(x / (g.getHeight() / maze.getHeight()))][Math.round(y / (g.getHeight() / maze.getHeight()))]);
//		g.refresh();
//	}
	
}
