package maze;

public class Main {
	public static Gui g;
	
	public static void main(String[] args) {
		Maze maze = new Maze(10, 10);

		g = new Gui(maze);
		maze.random();
		
//		maze.printCells();
	}
	
}
