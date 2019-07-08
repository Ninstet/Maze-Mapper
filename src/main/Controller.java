package main;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.Sound;
import lejos.hardware.Sounds;
import lejos.hardware.ev3.EV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import main.Data;
import maze.Maze;
import maze.Vector;
import sensors.Colour;
import sensors.Direction;
import sensors.Gyro;
import sensors.IR;
import sensors.Result;

public class Controller {
	
	public static final Data DATA = new Data();
	
	public static final EV3LargeRegulatedMotor LEFT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.A);
	public static final EV3LargeRegulatedMotor RIGHT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.D);
	public static final Wheel WHEEL1 = WheeledChassis.modelWheel(LEFT_MOTOR, 5.697).offset(-Data.WHEEL_SEPARATION / 2);
	public static final Wheel WHEEL2 = WheeledChassis.modelWheel(RIGHT_MOTOR, 5.697).offset(Data.WHEEL_SEPARATION / 2);
	public static final Chassis CHASSIS = new WheeledChassis(new Wheel[] {WHEEL1, WHEEL2 }, WheeledChassis.TYPE_DIFFERENTIAL);
	
	public static final MovePilot PILOT = new MovePilot(CHASSIS);
	public static final EV3 EV3_BRICK = (EV3) BrickFinder.getLocal();
	public static final Keys KEYS = EV3_BRICK.getKeys();
	
	public static final Colour LEFT_COLOUR_SENSOR = new Colour(EV3_BRICK.getPort("S1"));
	public static final Colour RIGHT_COLOUR_SENSOR = new Colour(EV3_BRICK.getPort("S2"));
	public static final Gyro GYRO = new Gyro(EV3_BRICK.getPort("S3"));
	public static final IR IR_SENSOR = new IR(EV3_BRICK.getPort("S4"));

	public Controller() {
		// TODO Auto-generated constructor stub
	}
	
	public static Result check(Maze maze, int absoluteDirection) {
		Vector tempVector = new Vector(Memory.location.getX(), Memory.location.getY(), absoluteDirection);
		Direction direction = Direction.getDirection(absoluteDirection);
		
		if (LEFT_COLOUR_SENSOR.isGreen() || RIGHT_COLOUR_SENSOR.isGreen()) return Result.GREEN;
		
		if (tempVector.isOnMap(maze)) {
			IR_SENSOR.look(direction);
			
			if (IR_SENSOR.getDistance() < 20) {
				DATA.addLog(direction.toString() + ": Wall");
				return Result.WALL;
			} else {
				DATA.addLog(direction.toString() + ": Possible");
				return Result.POSSIBLE;
			}
			
		} else {
			return Result.WALL;
		}
	}
	
	public static void rotate(int relativeAngle) {
		double startAngle = GYRO.getAngle();
		
		if (relativeAngle > 0) {
			PILOT.arcForward(Data.ARC_RADIUS);
			while (GYRO.getAngle() > startAngle - relativeAngle) {}
			PILOT.stop();
			
			PILOT.setAngularSpeed(30);
			
			PILOT.arcForward(-Data.ARC_RADIUS);
			while (GYRO.getAngle() < startAngle - relativeAngle) {}
			PILOT.stop();
			
//			PILOT.rotate(GYRO.getAngle() + relativeAngle - startAngle);
		} else if (relativeAngle < 0) {
			PILOT.arcForward(-Data.ARC_RADIUS);
			while (GYRO.getAngle() < startAngle - relativeAngle) {}
			PILOT.stop();
			
			PILOT.setAngularSpeed(30);
			
			PILOT.arcForward(Data.ARC_RADIUS);
			while (GYRO.getAngle() > startAngle - relativeAngle) {}
			PILOT.stop();
			
//			PILOT.rotate(GYRO.getAngle() - relativeAngle + startAngle);
		}
		
		PILOT.setAngularSpeed(Data.ANGULAR_SPEED);
	}
	
	public static void turn(int absoluteDirection) {
		DATA.addLog("Looking " + Direction.getDirection(absoluteDirection).toString().toLowerCase() + ".");
		int relativeDirection = Direction.toRelativeDirection(absoluteDirection);
		if (relativeDirection == 1) rotate(90);
		if (relativeDirection == 2) rotate(180);
		if (relativeDirection == 3) rotate(-90);
	}
	
	public static void nextCell() {
		DATA.addLog("Next cell...");
		PILOT.setAngularSpeed(30);
		PILOT.travel(Data.CELL_SEPERATION, true);
		
		double distanceTravelled = 0.0;
		
		IR_SENSOR.look(Direction.LEFT);
		double leftDistance = IR_SENSOR.getDistance() + Data.IR_OFFSET;
		distanceTravelled = PILOT.getMovement().getDistanceTraveled();
		
		if ((Data.CELL_SIZE / 2) - leftDistance > Data.IR_OFFSET / 2) {
			PILOT.arcForward(Data.CELL_SEPERATION - distanceTravelled);
			while (PILOT.getMovement().getDistanceTraveled() < Data.CELL_SEPERATION - distanceTravelled - 10) {}
			PILOT.stop();
			PILOT.arcForward(-Data.ARC_RADIUS);
			while (IR_SENSOR.getDistance() > Data.CELL_SIZE / 2) {}
		} else {
			IR_SENSOR.look(Direction.RIGHT);
			double rightDistance = IR_SENSOR.getDistance() + Data.IR_OFFSET;
			distanceTravelled = PILOT.getMovement().getDistanceTraveled();
			
			if ((Data.CELL_SIZE / 2) - rightDistance > Data.IR_OFFSET / 2) {
				PILOT.arcForward(-(Data.CELL_SEPERATION - distanceTravelled));
				while (PILOT.getMovement().getDistanceTraveled() < Data.CELL_SEPERATION - distanceTravelled + 1) {}
				PILOT.stop();
				PILOT.arcForward(Data.ARC_RADIUS);
				while (IR_SENSOR.getDistance() > Data.CELL_SIZE / 2) {}
			} else {
				while (PILOT.isMoving()) {}
			}
		}
		
		PILOT.stop();
		IR_SENSOR.look(Direction.FORWARD);
		PILOT.setAngularSpeed(Data.ANGULAR_SPEED);
		
//		if (leftDistance < Data.CELL_SIZE && rightDistance < Data.CELL_SIZE) {
//			DATA.addLog("* Two walls *");
//			PILOT.rotate(2 * (int)Math.asin(Data.CELL_SIZE / (leftDistance + rightDistance)));
//		} else if (leftDistance < Data.CELL_SIZE) {
//			DATA.addLog("* Left wall *");
//			if (leftDistance < Data.CELL_SIZE / 2) PILOT.rotate(((Data.CELL_SIZE / 2) - leftDistance) / (Data.CELL_SIZE / 2) * 70);
//		} else if (rightDistance < Data.CELL_SIZE) {
//			DATA.addLog("* Right wall *");
//			if (rightDistance < Data.CELL_SIZE / 2) PILOT.rotate(-(((Data.CELL_SIZE / 2) - rightDistance) / (Data.CELL_SIZE / 2) * 70));
//		}
		
//		PILOT.travel(Data.CELL_SEPERATION - distanceTravelled);
	}
	
	public static void LED(String c) {
		if (c == "RED") { lejos.hardware.Button.LEDPattern(2); }
		if (c == "AMBER") { lejos.hardware.Button.LEDPattern(3); Sound.playNote(Sounds.FLUTE, 3000, 50); }
		if (c == "GREEN") { lejos.hardware.Button.LEDPattern(1); }
	}
	
	public static void init() {
		DATA.addLog("Initializing...");
		LED("RED");
		
		LEFT_COLOUR_SENSOR.getRGB();
		RIGHT_COLOUR_SENSOR.getRGB();
		
		GYRO.getAngle();
		
		IR_SENSOR.getDistance();
		IR_SENSOR.look(Direction.LEFT);
		IR_SENSOR.look(Direction.RIGHT);
		IR_SENSOR.look(Direction.FORWARD);
		
		PILOT.rotate(10.0);
		PILOT.rotate(-10.0);
		PILOT.setLinearSpeed(Data.LINEAR_SPEED);
		PILOT.setAngularSpeed(Data.ANGULAR_SPEED);
		PILOT.setLinearAcceleration(60.0);
		PILOT.setAngularAcceleration(1000.0);
		
		DATA.addLog("Done!");
		LED("AMBER");
		
		Sound.playNote(Sounds.FLUTE, 1500, 200);
		try {
			Thread.sleep(100);
		} catch (Exception e) {}
		Sound.playNote(Sounds.FLUTE, 1500, 200);

//		DATA.addLog("Press any key...");
//		KEYS.waitForAnyPress();
//		DATA.clearLogs();
		LED("GREEN");
	}

}
