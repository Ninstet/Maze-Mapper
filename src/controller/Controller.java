package controller;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import controller.Data;

public class Controller {
	
	public static final EV3LargeRegulatedMotor LEFT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.A);
	public static final EV3LargeRegulatedMotor RIGHT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.D);
	public static final Wheel WHEEL1 = WheeledChassis.modelWheel(LEFT_MOTOR, 5.697).offset(-Data.WHEEL_SEPARATION / 2);
	public static final Wheel WHEEL2 = WheeledChassis.modelWheel(RIGHT_MOTOR, 5.697).offset(Data.WHEEL_SEPARATION / 2);
	public static final Chassis CHASSIS = new WheeledChassis(new Wheel[] {WHEEL1, WHEEL2 }, WheeledChassis.TYPE_DIFFERENTIAL);
	
	public static final MovePilot PILOT = new MovePilot(CHASSIS);
	public static final EV3 EV3_BRICK = (EV3) BrickFinder.getLocal();
	public static final Keys KEYS = EV3_BRICK.getKeys();

	public Controller() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean check(int direction) {
		return false;
	}
	
	public void rotateTo(int absoluteDirection) {
		
	}
	
	public void forward() {
		
	}

}
