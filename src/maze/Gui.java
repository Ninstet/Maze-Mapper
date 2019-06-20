package maze;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class Gui extends JFrame {
	public static final int WIDTH = 500;
	public static final int HEIGHT = 500;
	
	public Gui(Maze maze) {
		super();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT + 25);
		setResizable(true);
		setLocationRelativeTo(null);
		setTitle("Maze");
		
		getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//
			}
		});
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		MazePanel panel = new MazePanel(maze);
		add(panel);
		
		setVisible(true);
	}
	
	public void refresh() {
		repaint();
	}
	
}
