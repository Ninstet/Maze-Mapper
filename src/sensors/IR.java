package sensors;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3IRSensor;

public class IR extends Sensor {
	EV3IRSensor IR_SENSOR;
	EV3MediumRegulatedMotor SERVO;
	
	public IR(Port port) {
		IR_SENSOR = new EV3IRSensor(port);
		SERVO = new EV3MediumRegulatedMotor(MotorPort.B);
		
		sampler = IR_SENSOR.getDistanceMode();
	}
	
	public double getDistance() {
		return average(sampler, 50)[0];
	}
	
	public void setAngle(int angle) {
		SERVO.rotateTo(angle);
	}

}
