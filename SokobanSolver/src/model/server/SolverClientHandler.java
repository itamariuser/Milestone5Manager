package model.server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import algorithm.Plan;
import algorithm.Strips;
import commons.Level2D;
import commons.ServerPlan;
import model.data.Position2D;
import solver.PlanToServerPlanConverter;
import solver.SokobanPlannable;

public class SolverClientHandler implements ClientHandler {

	// TODO: Ask jersey server if level is already planned, yes -> return it,
	//no -> plan it and send to both the server (PUT) and the client 
	private Plan<Position2D> planning;
	
	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) throws Exception {
		ObjectOutputStream output = null;
		ObjectInputStream input = null;
		try {
			output = new ObjectOutputStream(outToClient);
			input = new ObjectInputStream(inFromClient);
			Level2D levelToSolve = (Level2D) input.readObject(); //reading the level from the sokoban client
			SokobanPlannable plannable = new SokobanPlannable(levelToSolve);
			Strips<Position2D> strips = new Strips<Position2D>();
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					planning = strips.plan(plannable);					
				}
			});
			t.start();
			t.join();
			ServerPlan convertedPlan=PlanToServerPlanConverter.convertToServerPlan(planning);
			output.writeObject(convertedPlan); //writing to the client as ServerPlan (CLIENT SHOULD KNOW IT THROUGH JAR)
			output.close();
		} catch (Exception e) {
			// TODO: handle exception
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

