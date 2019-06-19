package maze;

public class Main {
	
	public static void main(String[] args) {
		Maze maze = new Maze(10, 10);
		Gui g = new Gui(maze);
		maze.randomise();
		g.refresh();
	}
	
}
