package main;

import java.awt.Color;

import maze.Cell;

public class Memory {
	public static Cell location;
	public static int orientation;
	
	public static Color getColor() {
		if (Controller.LEFT_COLOUR_SENSOR.isGreen() || Controller.RIGHT_COLOUR_SENSOR.isGreen()) {
			return Color.GREEN;
		} else if (Controller.LEFT_COLOUR_SENSOR.isRed() || Controller.RIGHT_COLOUR_SENSOR.isRed()) {
			return Color.RED;
		} else {
			return Color.WHITE;
		}
	}

}
