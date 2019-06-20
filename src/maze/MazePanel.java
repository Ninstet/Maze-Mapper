package maze;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class MazePanel extends JPanel {
	private final Maze maze;
	
	public MazePanel(Maze maze) {
		this.maze = maze;
	}

	public void paintComponent(Graphics g) {
		int cell_size = Gui.HEIGHT / maze.getHeight();
		
		g.drawLine(100, 100, 300, 300);
		
		for (int i = 0; i < maze.getWidth(); i++) {
			
			for (int j = 0; j < maze.getHeight(); j++) {
				
				Cell cell = maze.getCells()[i][j];
				
				int[] topLeft = new int[] {i * cell_size, j * cell_size};
				int[] topRight = new int[] {(i + 1) * cell_size, j * cell_size};
				int[] bottomLeft = new int[] {i * cell_size, (j + 1) * cell_size};
				int[] bottomRight = new int[] {(i + 1) * cell_size, (j + 1) * cell_size};
				
				g.setColor(cell.getColor());
				g.fillRect(topLeft[0], topLeft[1], cell_size, cell_size);
				g.setColor(Color.BLACK);
				
				if (cell.checkWalls()) g.fillRect(topLeft[0], topLeft[1], cell_size, cell_size);
				
				if (cell.willDisplayValues()) {
					g.drawString("(" + cell.getX() + ", " + cell.getY() + ")", topLeft[0] + (cell_size / 2) - (cell_size / 5), topLeft[1] + 15);
					if (cell.getF() != 0) g.drawString(cell.getG() + "  " + cell.getF() + "  " + cell.getH(), topLeft[0] + (cell_size / 2) - 25, topLeft[1] + (cell_size / 2));
				}
				
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(2));
				
				if (cell.hasWall(0)) g2.drawLine(topLeft[0], topLeft[1], topRight[0], topRight[1]);
				if (cell.hasWall(1)) g2.drawLine(topRight[0], topRight[1], bottomRight[0], bottomRight[1]);
				if (cell.hasWall(2)) g2.drawLine(bottomRight[0], bottomRight[1], bottomLeft[0], bottomLeft[1]);
				if (cell.hasWall(3)) g2.drawLine(bottomLeft[0], bottomLeft[1], topLeft[0], topLeft[1]);

			}
		}
		
	}

}
