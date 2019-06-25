package sensors;

import main.Memory;

public abstract class Direction {
	public static final Direction FORWARD = new Forward();
	public static final Direction BACKWARD = new Backward();
	public static final Direction LEFT = new Left();
	public static final Direction RIGHT = new Right();
	
	@SuppressWarnings("unused")
	private String state;

	private Direction(String state) {
		this.state = state;
	}
	
	private static final class Forward extends Direction {
		private Forward() {
			super("Forward");
		}
	}
	
	private static final class Backward extends Direction {
		private Backward() {
			super("Backward");
		}
	}
	
	private static final class Left extends Direction {
		private Left() {
			super("Left");
		}
	}
	
	private static final class Right extends Direction {
		private Right() {
			super("Right");
		}
	}
	
	@Override
	public boolean equals(Object o) {
		return this.getClass().equals(o.getClass());
	}
	
	@Override
	public String toString() {
		return this.state;
	}
	
	public static Direction getDirection(int absoluteDirection) {
		int relativeDirection = toRelativeDirection(absoluteDirection);
		if (relativeDirection == 0) {
			return Direction.FORWARD;
		} else if (relativeDirection == 1) {
			return Direction.RIGHT;
		} else if (relativeDirection == 3) {
			return Direction.LEFT;
		} else {
			return Direction.BACKWARD;
		}
	}
	
	public static int toRelativeDirection(int absoluteDirection) {
		return (absoluteDirection - Memory.orientation + 4) % 4;
	}
	
	public static int toAbsoluteDirection(int relativeDirection) {
		return (relativeDirection + Memory.orientation + 4) % 4;
	}

}
