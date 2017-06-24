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
	
	@Override
	public Boolean handleClient(InputStream inFromClient, OutputStream outToClient) throws Exception {
		ObjectInputStream input = new ObjectInputStream(inFromClient);
		ObjectOutputStream output = new ObjectOutputStream(outToClient);
		Level2D levelToSolve = (Level2D) input.readObject(); //reading the level from the sokoban client
		SokobanPlannable plannable = new SokobanPlannable(levelToSolve);
		Strips<Position2D> strips = new Strips<Position2D>();
		levelToSolve = (Level2D) input.readObject();
		Plan<Position2D> levelPlan = strips.plan(plannable);
		ServerPlan convertedPlan=PlanToServerPlanConverter.convertToServerPlan(strips.plan(plannable));
		output.writeObject(convertedPlan); //writing to the client as ServerPlan (CLIENT SHOULD KNOW IT THROUGH JAR)
		try {
			
		} finally {
			input.close();
			output.close();
		}
		
		if (levelPlan.getActions().isEmpty()) {
			return false;
		}
		return true;
		
	}

	
}
