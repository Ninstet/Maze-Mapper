package sensors;

public abstract class Result {
	public static final Result GREEN = new Green();
	public static final Result POSSIBLE = new Possible();
	public static final Result WALL = new Wall();
	
	private String state;

	private Result(String state) {
		this.state = state;
	}
	
	private static final class Green extends Result {
		private Green() {
			super("green");
		}
	}
	
	private static final class Possible extends Result {
		private Possible() {
			super("possible");
		}
	}
	
	private static final class Wall extends Result {
		private Wall() {
			super("wall");
		}
	}
	
	@Override
	public boolean equals(Object o) {
		return this.getClass().equals(o.getClass());
	}

}
