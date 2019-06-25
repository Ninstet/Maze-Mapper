package main;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import maze.Cell;
import maze.Maze;
import sensors.Direction;
import sensors.Sensor;

public class Server {
	private static ServerSocket server;
	private static Socket client;
	private static ObjectOutputStream output;
	private static int port = 6000;
	
	private static Maze exploredMaze;
	
	public static void main(String[] args) throws IOException {
		
		client = connectClient();
		
		Controller.init();
		
		exploredMaze = new Maze("Explorer", Data.X_SIZE, Data.Y_SIZE);
		
		Cell bottomLeft = exploredMaze.getCells()[0][Data.Y_SIZE - 1];
		Cell topRight = exploredMaze.getCells()[Maze.rand(0, Data.X_SIZE - 1)][Maze.rand(0, Data.Y_SIZE - 1)];
		
		exploredMaze.explore(bottomLeft, topRight, false);
		exploredMaze.displayVectors(exploredMaze.shortestPath(bottomLeft, topRight));
		
		Sensor.IR_SENSOR.look(Direction.FORWARD);
	}
	
	private static Socket connectClient() throws IOException  {
		Controller.DATA.addLog("Server started.");
		
		server = new ServerSocket(port);
		client = server.accept();
		output = new ObjectOutputStream(client.getOutputStream());
		
		Controller.DATA.addLog("Client connected!");
		
		return client;
	}
	
	public static void uploadMaze(Maze maze) {
		try {
			output.reset();
			output.writeObject(maze);
			output.flush();
		} catch (IOException e) {
			System.exit(0);
		}
	}
	
}
