package main;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import maze.Gui;
import maze.Maze;

public class Client {
	private static Socket client;
	private static ObjectInputStream input;
	private static String host = "10.0.1.1";
	private static int port = 6000;
	
	private static Maze maze;
	private static Gui g = new Gui();

	public static void main(String[] args) throws IOException, ClassNotFoundException, EOFException {
		
		client = connectServer();
		
		maze = new Maze("Explorer", Data.X_SIZE, Data.Y_SIZE);
		
		System.out.print("Initializing sensors...");
		input.readObject();
		System.out.println(" Done!\n");
		
		while (true) {
			try {
				maze = (Maze) input.readObject();
			} catch (EOFException e) {
				System.out.println("\nServer disconnected.");
				break;
			}

			System.out.println("Maze updated: " + maze.getTitle());
			
			g.addMaze(maze.getTitle(), maze);
		}
		
	}
	
	private static Socket connectServer() throws IOException {
		System.out.println("Client started. Connecting to server...");
		
		while (true) {
			try {
				client = new Socket(host, port);
				break;
			} catch (SocketException e) {
				System.out.println("Connection refused. Retrying...");
			}
		}
		
		input = new ObjectInputStream(client.getInputStream());
		
		System.out.println("Server connected!");
		
		return client;
	}

}
