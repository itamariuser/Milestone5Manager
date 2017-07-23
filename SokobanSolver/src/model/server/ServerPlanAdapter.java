package model.server;

import java.util.LinkedList;
import java.util.List;

import commons.ServerCommand;
import commons.ServerPlan;

public class ServerPlanAdapter {
	/**
	 * This method is used to convert list of strings to ServerPlan object.
	 * In the list the steps are: u - up, l - left, d - down, r - right.
	 * @param strings List of strings that including the steps to complete the level 
	 * @param levelName The name of the level that the list was made for.
	 * @return ServerPlan object (supported by the client)
	 */
	public static ServerPlan convert(List<String> strings, String levelName) {
		LinkedList<ServerCommand> commands = new LinkedList<>();
		for (String string : strings) {
			String sL = string.toLowerCase();
			switch (sL) {
			case "r":
				commands.add(new ServerCommand("right"));
				break;
			case "l":
				commands.add(new ServerCommand("left"));
				break;
			case "d":
				commands.add(new ServerCommand("down"));
				break;
			case "u":
				commands.add(new ServerCommand("up"));
				break;
			default:
				commands.add(new ServerCommand("right"));
				break;
			}
		}
		ServerPlan plan = new ServerPlan(commands);
		plan.setLevelName(levelName);
		return plan;
	}

}
