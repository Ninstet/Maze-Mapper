package sensors;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import main.Controller;
import main.Data;

public class Gyro extends Sensor {
	EV3GyroSensor GYRO_SENSOR;

	public Gyro(Port port) {
		GYRO_SENSOR = new EV3GyroSensor(port);
		
		sampler = GYRO_SENSOR.getAngleMode();
	}
	
	public double getAngle() {
		return average(sampler, 50)[0];
	}
	
	public void rotateTo(double angle) {
		if (this.getAngle() < angle) {
			Controller.PILOT.arcForward(-Data.ARC_RADIUS);
			while (this.getAngle() < angle - 15) {}
		} else if (this.getAngle() > angle) {
			Controller.PILOT.arcForward(Data.ARC_RADIUS);
			while (this.getAngle() > angle + 15) {}
		}
	}

}
