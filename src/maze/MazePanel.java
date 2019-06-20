package maze;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import org.opencv.core.Point;

public class MazePanel extends JPanel {
	private final Maze maze;
	
	public MazePanel(Maze maze) {
		this.maze = maze;
	}

	public void paintComponent(Graphics g) {
		int cell_size = Gui.HEIGHT / maze.getHeight();
		
		for (int i = 0; i < maze.getWidth(); i++) {
			for (int j = 0; j < maze.getHeight(); j++) {
				Cell cell = maze.getCells()[i][j];
				
				g.setColor(cell.getColor());
				g.fillRect(i * cell_size, j * cell_size, cell_size, cell_size);
				g.setColor(Color.BLACK);
				
			}
		}
		
		for (int i = 0; i < maze.getWidth(); i++) {
			for (int j = 0; j < maze.getHeight(); j++) {
				
				Cell cell = maze.getCells()[i][j];
				
				int[] topLeft = new int[] {i * cell_size, j * cell_size};
				int[] topRight = new int[] {(i + 1) * cell_size, j * cell_size};
				int[] bottomLeft = new int[] {i * cell_size, (j + 1) * cell_size};
				int[] bottomRight = new int[] {(i + 1) * cell_size, (j + 1) * cell_size};
				
				if (cell.checkWalls() && cell.getColor() == Color.WHITE) g.fillRect(topLeft[0], topLeft[1], cell_size, cell_size);
				
				if (cell.willDisplayValues()) {
					g.drawString(cell.getCoords(), topLeft[0] + (cell_size / 2) - (cell_size / 5), topLeft[1] + 15);
					if (cell.getF() != 0) g.drawString(cell.getG() + "  " + cell.getF() + "  " + cell.getH(), topLeft[0] + (cell_size / 2) - 35, topLeft[1] + (cell_size / 2));
				}
				
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(2));
				
				if (cell.getDirection() != -1) {
					Point p1 = null, p2 = null;
					int offset = cell_size / 10;
					
					if (cell.getDirection() == 0) {
						p2 = new Point(topLeft[0] + (cell_size / 2), topLeft[1] + (cell_size / 2) - offset);
						p1 = new Point(topLeft[0] + (cell_size / 2), topLeft[1] - (cell_size / 2) + offset);
					} else if (cell.getDirection() == 1) {
						p2 = new Point(topLeft[0] + (cell_size / 2) + offset, topLeft[1] + (cell_size / 2));
						p1 = new Point(topLeft[0] + cell_size + (cell_size / 2) - offset, topLeft[1] + (cell_size / 2));
					} else if (cell.getDirection() == 2) {
						p2 = new Point(topLeft[0] + (cell_size / 2), topLeft[1] + (cell_size / 2) + offset);
						p1 = new Point(topLeft[0] + (cell_size / 2), topLeft[1] + cell_size + (cell_size / 2) - offset);
					} else if (cell.getDirection() == 3) {
						p2 = new Point(topLeft[0] + (cell_size / 2) - offset, topLeft[1] + (cell_size / 2));
						p1 = new Point(topLeft[0] - (cell_size / 2) + offset, topLeft[1] + (cell_size / 2));
					}
					
					g2.drawLine((int)(p1.x), (int)(p1.y), (int)(p2.x), (int)(p2.y));
					drawArrowHead(g2, p1, p2, Color.BLACK, offset);
				}
				
				if (cell.hasWall(0)) g2.drawLine(topLeft[0], topLeft[1], topRight[0], topRight[1]);
				if (cell.hasWall(1)) g2.drawLine(topRight[0], topRight[1], bottomRight[0], bottomRight[1]);
				if (cell.hasWall(2)) g2.drawLine(bottomRight[0], bottomRight[1], bottomLeft[0], bottomLeft[1]);
				if (cell.hasWall(3)) g2.drawLine(bottomLeft[0], bottomLeft[1], topLeft[0], topLeft[1]);
				
			}
			
		}
		
	}
	
	private void drawArrowHead(Graphics2D g2, Point tip, Point tail, Color color, int offset) {
		double phi = Math.toRadians(40);
        int barb = offset;
		
		g2.setPaint(color);
		double dy = tip.y - tail.y;
		double dx = tip.x - tail.x;
		double theta = Math.atan2(dy, dx);
		double x, y, rho = theta + phi;
		
		for (int i = 0; i < 2; i++) {
			x = tip.x - barb * Math.cos(rho);
			y = tip.y - barb * Math.sin(rho);
			g2.draw(new Line2D.Double(tip.x, tip.y, x, y));
			rho = theta - phi;
		}
	}

}
