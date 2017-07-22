package model.server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import commons.Level2D;
import commons.RestClient;
import commons.ServerCommand;
import commons.ServerPlan;

public class SolverClientHandler implements ClientHandler {
	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) throws Exception {
		ObjectOutputStream output = null;
		ObjectInputStream input = null;
		try {
			output = new ObjectOutputStream(outToClient);
			input = new ObjectInputStream(inFromClient);
			Level2D levelToSolve = (Level2D) input.readObject(); //reading the level from the sokoban client
			RestClient client = RestClient.getInstance();
			client.setServerURI("http://localhost:8080/Milestone5SolutionRestServer/");
			ServerPlan restPlan = client.getPlanForLevelName(levelToSolve.getName());
			if(restPlan.getCommands().isEmpty())
			{
				ServerCommand comm = new ServerCommand("Move right");
				LinkedList<ServerCommand> arr = new LinkedList<ServerCommand>();
				arr.add(comm);
				ServerPlan convertedPlan = new ServerPlan(arr);
				output.writeObject(convertedPlan); //writing to the client as ServerPlan (CLIENT SHOULD KNOW IT THROUGH JAR)
			}
			else
			{
				output.writeObject(restPlan);
			}
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(input != null)
			{
				input.close();
			}
			if(output != null)
			{
				output.close();
			}
		}

	
	}

	
}

