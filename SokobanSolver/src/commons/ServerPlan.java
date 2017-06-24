package commons;


import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServerPlan {
	private LinkedList<ServerCommand> commands;

	
	public ServerPlan(LinkedList<ServerCommand> commands) {
		super();
		this.commands = commands;
	}

	public LinkedList<ServerCommand> getCommands() {
		return commands;
	}

	public void setCommands(LinkedList<ServerCommand> commands) {
		this.commands = commands;
	}

	@Override
	public boolean equals(Object obj) {
		boolean eq = true;
		if (obj instanceof ServerPlan) {
			for (ServerCommand c : ((ServerPlan) obj).commands) {
				if (this.commands.contains(c))
					eq = false;
			}
		}
		return eq;
	}

}
