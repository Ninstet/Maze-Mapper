package maze;

public class Main {
	private static Maze maze;
	private static Gui g;
	
	public static void main(String[] args) {
		maze = new Maze(10, 10);
		g = new Gui(maze);
		maze.randomize();
		g.refresh();
	}
	
	public static void pathFromOriginTo(int x, int y) {
		maze.shortestPath(maze.getCells()[0][0], maze.getCells()[Math.round(x / (g.getHeight() / maze.getHeight()))][Math.round(y / (g.getHeight() / maze.getHeight()))]);
		g.refresh();
	}
	
}
