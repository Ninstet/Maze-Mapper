package sensors;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;

public class Colour extends Sensor {
	EV3ColorSensor RGB_SENSOR;
	
	public Colour(Port port) {
		RGB_SENSOR = new EV3ColorSensor(port);
		RGB_SENSOR.setFloodlight(false);
		
		sampler = RGB_SENSOR.getRGBMode();
	}
	
	public float[] getRGB() {
		return average(sampler, 50);
	}
	
	public boolean isBlack() {
		float[] colour = this.getRGB();
		return colour[0] < 0.04 && colour[1] < 0.04 && colour[2] < 0.04;
	}
	
	public boolean isGreen() {
		float[] colour = this.getRGB();
		return Math.abs(0.03-colour[0]) < 0.025 && Math.abs(0.08-colour[1]) < 0.025 && Math.abs(0.07-colour[2]) < 0.025;
	}
	
	public boolean isRed() {
		float[] colour = this.getRGB();
		return colour[0] > 0.14 && Math.abs(0.03-colour[1]) < 0.02 && Math.abs(0.03-colour[2]) < 0.02;
	}
	
}
