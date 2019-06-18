package maze;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MazePanel extends JPanel {
	private final Maze maze;
	
	public MazePanel(Maze maze) {
		this.maze = maze;
	}

	public void paintComponent(Graphics g) {
		int cell_size = Gui.HEIGHT / maze.getHeight();
		
		for (int i = 0; i < maze.getWidth(); i++) {
			for (int j = 0; j < maze.getHeight(); j++) {
				
				int[] topLeft = new int[] {i * cell_size, j * cell_size};
				int[] topRight = new int[] {(i + 1) * cell_size, j * cell_size};
				int[] bottomLeft = new int[] {i * cell_size, (j + 1) * cell_size};
				int[] bottomRight = new int[] {(i + 1) * cell_size, (j + 1) * cell_size};
				
				if (maze.getCells()[i][j].isWall(0)) g.drawLine(topLeft[0], topLeft[1], topRight[0], topRight[1]);
				if (maze.getCells()[i][j].isWall(1)) g.drawLine(topRight[0], topRight[1], bottomRight[0], bottomRight[1]);
				if (maze.getCells()[i][j].isWall(2)) g.drawLine(bottomRight[0], bottomRight[1], bottomLeft[0], bottomLeft[1]);
				if (maze.getCells()[i][j].isWall(3)) g.drawLine(bottomLeft[0], bottomLeft[1], topLeft[0], topLeft[1]);
				
				if (maze.getCells()[i][j].checkWalls()) g.fillRect(topLeft[0], topLeft[1], cell_size, cell_size);
				
			}
		}
	}

}
