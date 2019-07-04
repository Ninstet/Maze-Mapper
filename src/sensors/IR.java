package sensors;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3IRSensor;

public class IR extends Sensor {
	private EV3IRSensor IR_SENSOR;
	private EV3MediumRegulatedMotor SERVO;
	
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
	
	public void look(Direction direction) {
		if (direction == Direction.LEFT) setAngle(-130);
		if (direction == Direction.FORWARD) setAngle(0);
		if (direction == Direction.RIGHT) setAngle(130);
	}

}
