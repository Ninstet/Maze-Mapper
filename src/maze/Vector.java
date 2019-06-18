package maze;

public class Vector {
	private int x1, y1;
	private int x2, y2;
	private int wall1, wall2;
	
	public void setStart(int x1, int y1) {
		this.x1 = x1;
		this.y1 = y1;
	}
	
	public void setEnd(int x2, int y2) {
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void setWalls(int wall1, int wall2) {
		this.wall1 = wall1;
		this.wall2 = wall2;
	}
	
	public int[] getStart() {
		return new int[] {x1, y1};
	}
	
	public int[] getEnd() {
		return new int[] {x2, y2};
	}
	
	public int[] getWalls() {
		return new int[] {wall1, wall2};
	}
	
}
