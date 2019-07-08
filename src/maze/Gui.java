package maze;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	
	public void addMaze(String title, Maze maze) {
		MazePanel panel = new MazePanel(maze);

		int selected = tabbedPane.getSelectedIndex();
		removeTabWithTitle(title);
		tabbedPane.add(panel, title);
		tabbedPane.setSelectedIndex(selected);
		
		this.add(tabbedPane);
		this.revalidate();
		this.repaint();
	}
	
	private void removeTabWithTitle(String title) {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			String tabTitle = tabbedPane.getTitleAt(i);
			if (tabTitle.equals(title)) {
				tabbedPane.remove(i); 
				break;
			}
		}
	}
	
}
