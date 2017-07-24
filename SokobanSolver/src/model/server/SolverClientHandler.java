package model.server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import common.Level2D;
import commons.RESTClient;
import commons.ServerPlan;
import solver.SokobanSolver;
/**
 * A ClientHandler that responsible to handle a client that request a solution for
 * Sokoban level using algorithm and REST server.
 * @author Matan
 */
public class SolverClientHandler implements ClientHandler {
	
	private String uri;
	public SolverClientHandler(String uri) {
		this.uri = uri;
	}
	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) throws Exception {
		ObjectOutputStream output = null;
		ObjectInputStream input = null;
		try {
			output = new ObjectOutputStream(outToClient);
			input = new ObjectInputStream(inFromClient);
			Object o=input.readObject();
			Level2D levelToSolve = (Level2D) o; //reading the level from the sokoban client
			ServerPlan restPlan = getExistingPlan(this.uri, levelToSolve);
			if(restPlan.getCommands().isEmpty())
			{
				SokobanSolver solver = new SokobanSolver();
				ServerPlan algoPlan = ServerPlanAdapter.convert(solver.solveLevel(levelToSolve), levelToSolve.getName());
				output.writeObject(algoPlan);
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
	private ServerPlan getExistingPlan(String uri, Level2D levelToSolve) throws Exception
	{
		//RESTORE
//		RESTClient client = RESTClient.getInstance();
//		client.setServerURI(uri);
//		return client.getPlanForLevelName(levelToSolve.getName());
		//END
		//DELETE
		return new ServerPlan(new LinkedList<>());
		//END
	}

	
}

