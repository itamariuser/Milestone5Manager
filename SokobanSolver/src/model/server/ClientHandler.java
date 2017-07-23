package model.server;

import java.io.InputStream;
import java.io.OutputStream;

public interface ClientHandler {
	/**
	 * This method is used to handle a client that connects the server.
	 * @param inFromClient The InputStream that allows to send data to the client
	 * @param outToClient The OutputStream that allows to receive data from the client
	 * @throws Exception
	 */
	public void handleClient(InputStream inFromClient, OutputStream outToClient) throws Exception;
}
