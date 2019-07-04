package main;

import java.awt.Color;

import maze.Cell;
import sensors.Sensor;

public class Memory {
	public static Cell location;
	public static int orientation;
	
	public static Color getColor() {
		if (Sensor.LEFT_COLOUR_SENSOR.isGreen() || Sensor.RIGHT_COLOUR_SENSOR.isGreen()) {
			return Color.GREEN;
		} else if (Sensor.LEFT_COLOUR_SENSOR.isRed() || Sensor.RIGHT_COLOUR_SENSOR.isRed()) {
			return Color.RED;
		} else {
			return Color.WHITE;
		}
	}

}
