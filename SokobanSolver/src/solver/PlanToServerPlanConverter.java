package solver;



import java.util.LinkedList;

import algorithm.Action;
import algorithm.Plan;
import commons.ServerCommand;
import commons.ServerPlan;
import model.data.Position2D;

public class PlanToServerPlanConverter {

	/**TODO: convert from list of actions to commands(or maybe plan output commands straight out, so this is useless, and we need to change each instance of ServerPlan to a List<String>
	 * 
	 * @param plan
	 * @return
	 */
	public static ServerPlan convertToServerPlan(Plan<Position2D> plan)
	{
		LinkedList<ServerCommand> commands=new LinkedList<>();
		for (Action<Position2D> act : plan.getActions()) {
			if(act.getName().startsWith("Move_MainCharacter_In_Direction"))//many strings "Move x"
			{
				for (String string : act.getSub()) {
					if(string.endsWith("up"))
					{
						commands.add(new ServerCommand("up"));
					}
					else if(string.endsWith("down"))
					{
						commands.add(new ServerCommand("down"));
					}
					else if(string.endsWith("left"))
					{
						commands.add(new ServerCommand("left"));
					}
					else if(string.endsWith("right"))
					{
						commands.add(new ServerCommand("right"));
					}
				}
			}
			if(act.getName().equals("Move_Crate_To"))
			{
				Position2D cratePos=act.getEffects().getComponents().get(0).getData();
				Position2D playerPos=act.getEffects().getComponents().get(1).getData();
				//differentiate
				if(playerPos.getX() == cratePos.getX()+1)
				{
					commands.add(new ServerCommand("left"));
				}
				else if(playerPos.getX() == cratePos.getX()-1)
				{
					commands.add(new ServerCommand("right"));
				}
				else if(playerPos.getY() == cratePos.getY()+1)
				{
					commands.add(new ServerCommand("up"));
				}
				else if(playerPos.getY() == cratePos.getY()-1)
				{
					commands.add(new ServerCommand("down"));
				}
			}
			if(act.getName().equals("Move_MainCharacter_To"))
			{
				Position2D from=act.getPreconditions().getComponents().get(0).getData();
				Position2D to=act.getEffects().getComponents().get(0).getData();
				if(from.getX() == to.getX()+1)
				{
					commands.add(new ServerCommand("left"));
				}
				else if(from.getX() == to.getX()-1)
				{
					commands.add(new ServerCommand("right"));
				}
				else if(from.getY() == to.getY()+1)
				{
					commands.add(new ServerCommand("up"));
				}
				else if(from.getY() == to.getY()-1)
				{
					commands.add(new ServerCommand("down"));
				}
			}
		}
		return new ServerPlan(commands);
	}
	
}
