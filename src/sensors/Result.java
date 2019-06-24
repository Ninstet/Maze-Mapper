package sensors;

public abstract class Result {
	public static final Result GREEN = new Green();
	public static final Result POSSIBLE = new Possible();
	public static final Result WALL = new Wall();
	
	@SuppressWarnings("unused")
	private String state;

	private Result(String state) {
		this.state = state;
	}
	
	private static final class Green extends Result {
		private Green() {
			super("Green");
		}
	}
	
	private static final class Possible extends Result {
		private Possible() {
			super("Possible");
		}
	}
	
	private static final class Wall extends Result {
		private Wall() {
			super("Wall");
		}
	}
	
	@Override
	public boolean equals(Object o) {
		return this.getClass().equals(o.getClass());
	}

}
