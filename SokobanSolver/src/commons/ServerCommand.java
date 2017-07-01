package commons;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ServerCommand implements Serializable {
	String direction;

	public ServerCommand(String direction) {
		super();
		this.direction = direction;
	}
}
