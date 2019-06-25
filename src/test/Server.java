package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import lejos.hardware.lcd.LCD;
import sensors.Direction;
import sensors.Sensor;

public class Server {
	private static ServerSocket server;
	private static int port = 6000;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		LCD.drawString("Server running.", 1, 1);
		
		server = new ServerSocket(port);
		Socket client = server.accept();
		
		LCD.drawString("Client connected!.", 1, 2);
		
		ObjectInputStream input = new ObjectInputStream(client.getInputStream());
		ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
		
		String message = (String) input.readObject();
		
		LCD.drawString(message, 1, 3);
		
		output.writeObject("Helllooooo");
		
		Sensor.IR_SENSOR.look(Direction.LEFT);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		input.close();
		output.close();
		client.close();
		
	}

}
