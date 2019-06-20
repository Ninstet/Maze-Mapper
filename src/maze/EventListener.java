package maze;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EventListener extends MouseAdapter {

	@Override
	public void mouseMoved(MouseEvent e) {
		Main.pathFromOriginTo(e.getX(), e.getY());
	}
	
	
}
