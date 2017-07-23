package model.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import commons.ServerCommand;
import commons.ServerPlan;

public class ServerPlanAdapter {
	private static HashMap<String,ServerCommand> letterToCommand;
	
	/**
	 * Initialize the string to command hashMap.
	 * if any new commands are added, add their representation here.
	 */
	private static void init()
	{
		letterToCommand=new HashMap<>();
		letterToCommand.put("r", new ServerCommand("right"));
		letterToCommand.put("l", new ServerCommand("left"));
		letterToCommand.put("d", new ServerCommand("down"));
		letterToCommand.put("u", new ServerCommand("up"));
	}
	/**
	 * Convert a list of strings to ServerPlan.
	 * @param strings List of strings that including the steps to complete the level 
	 * @param levelName The name of the level that the list was made for.
	 * @return ServerPlan object (supported by the client).
	 */
	public static ServerPlan convert(List<String> strings, String levelName) {
		if (letterToCommand==null)
			init();
		LinkedList<ServerCommand> commands = new LinkedList<>();
		for (String string : strings) {
			String sL = string.toLowerCase();
			ServerCommand command=letterToCommand.get(sL);
			if(command!=null)
				commands.add(command);
		}
		ServerPlan plan = new ServerPlan(commands);
		plan.setLevelName(levelName);
		return plan;
	}

}
