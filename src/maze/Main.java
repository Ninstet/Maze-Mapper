package maze;

public class Main {
	public static Gui g;
	
	public static void main(String[] args) {
		Maze maze = new Maze(100, 100);
		g = new Gui(maze);
		maze.randomize();
		maze.shortestPath(maze.getCells()[Maze.rand(0, 99)][Maze.rand(0, 99)], maze.getCells()[Maze.rand(0, 99)][Maze.rand(0, 99)]);
		g.refresh();
	}
	
}
