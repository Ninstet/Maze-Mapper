package main;

import java.util.ArrayList;

import lejos.hardware.lcd.LCD;

public class Data {
	
	public static final int X_SIZE = 10;
	public static final int Y_SIZE = 10;
	
	public static final double LINEAR_SPEED = 15.0;
	public static final double ANGULAR_SPEED = 120.0;
	public static final double ARC_RADIUS = 0.0001;
	public static final double WHEEL_SEPARATION = 11.84;
	public static final double IR_OFFSET = 4.0;
	public static final double CELL_SIZE = 30.0;
	public static final double CELL_SEPERATION = 40.0;
	
	private ArrayList<String> logs = new ArrayList<>();
	
	public void addLog(String s) {
		if (logs.size() == 8) {
			logs.remove(0);
		}
		logs.add(s);
		this.updateLogs();
	}
	
	public void clearLogs() {
		logs.clear();
		this.updateLogs();
	}
	
	public String[] getLogs() {
		return logs.toArray(new String[logs.size()]);
	}
	
	public void updateLogs() {
		LCD.clear();
		for (int i = 0; i < logs.size(); i++) {
			LCD.drawString(logs.get(i), 1, i);
		}
	}

}
