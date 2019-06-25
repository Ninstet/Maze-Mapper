package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		Socket client = new Socket("10.0.1.1", 6000);
		
		ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
		
		output.writeObject("Hello Mr. Robot");
		output.writeObject("How are you today?");
		output.writeObject("Penis");
		
		ObjectInputStream input = new ObjectInputStream(client.getInputStream());
		String message = (String) input.readObject();
		System.out.println(message);
		
		int distance = (int) input.readObject();
		System.out.println(distance);
		
		input.close();
		output.close();
		client.close();
		
	}

}
