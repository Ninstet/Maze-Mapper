package maze;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Gui extends JFrame {
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	
	public static JTabbedPane tabbedPane = new JTabbedPane();
	
	public Gui() {
		super();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH + 25, HEIGHT + 70);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("Maze");
		
		this.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//
			}
		});
		
		//getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		this.setVisible(true);
	}
	
	public void refresh() {
		this.repaint();
	}
	
	public void addMaze(String title, Maze maze) {
		MazePanel panel = new MazePanel(maze);
		tabbedPane.add(title, panel);
		
		this.add(tabbedPane);
		this.refresh();
	}
	
}
