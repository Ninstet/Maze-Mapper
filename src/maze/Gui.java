package maze;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class Gui extends JFrame {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	
	public Gui(Maze maze) {
		super();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT + 24);
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setTitle("Maze");
		
		this.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//
			}
		});
		
		MazePanel panel = new MazePanel(maze);
		this.add(panel);
		
		this.setVisible(true);
	}
	
	public void refresh() {
		this.repaint();
	}
	
}
