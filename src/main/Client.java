package main;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import maze.Gui;
import maze.Maze;

public class Client {
	private static Socket client;
	private static ObjectInputStream input;
	private static String host = "10.0.1.1";
	private static int port = 6000;
	
	private static Maze exploredMaze;
	private static Gui g = new Gui();

	public static void main(String[] args) throws IOException, ClassNotFoundException, EOFException {
		
		client = connectServer();
		
		exploredMaze = new Maze(Data.X_SIZE, Data.Y_SIZE);
		
		while (true) {
			System.out.println("Updating GUI...");
			
			exploredMaze = (Maze) input.readObject();
			g.addMaze("Explorer", exploredMaze);
		}

	}
	
	private static Socket connectServer() throws IOException {
		System.out.println("Client started.");
		
		Socket client = new Socket(host, port);
		input = new ObjectInputStream(client.getInputStream());
		
		System.out.println("Server connected!");
		
		return client;
	}

}
