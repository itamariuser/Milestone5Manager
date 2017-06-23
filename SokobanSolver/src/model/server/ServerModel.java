package model.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handle sokoban clients using thread pool (ExecutorService).
 */
public class ServerModel extends Observable implements Server {
	
	private int port;
	private volatile boolean stop;
	private BlockingQueue<Socket> awaitingClients;
	private BlockingQueue<Socket> handledClients; 
	private ClientHandler ch;
	private int nThreads;
	
	
	
	
	public BlockingQueue<Socket> getAwaitingClients() {
		return awaitingClients;
	}

	public BlockingQueue<Socket> getHandledClients() {
		return handledClients;
	}

	ArrayList<Object> arr;
	
	public void RemoveFromAwaitingClients(InetAddress addr)
	{
		if (awaitingClients.contains(addr)) 
		{
			awaitingClients.remove(addr);
			arr.clear();
			arr.add(addr);
			arr.add("remove awaitingClients");
			ServerModel.this.setChanged();
			ServerModel.this.notifyObservers(arr);
		}
	}

	public ServerModel(int port, ClientHandler ch, int nThreads) {
		this.port = port;
		this.ch = ch;
		this.nThreads = nThreads;
		arr=new ArrayList<>();
	}
	
	public class ServerTask implements Callable<Boolean> {

		@Override
		public Boolean call() throws Exception {
			Socket aClient = awaitingClients.take();
			arr.clear();
			arr.add(aClient.getInetAddress());
			arr.add("remove awaitingClients");
			ServerModel.this.setChanged();
			ServerModel.this.notifyObservers(arr);
			handledClients.put(aClient);
			arr.clear();
			arr.add(aClient.getInetAddress());
			arr.add("add handledClients");
			ServerModel.this.notifyObservers(arr);
			boolean isSolvable = ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
			aClient.getInputStream().close();
			aClient.getOutputStream().close();
			return isSolvable;
		}
	}
	
	@Override
	public void runServer() throws Exception
	{
		ExecutorService executor = Executors.newFixedThreadPool(this.nThreads);
		ServerSocket server = new ServerSocket(this.port);
		server.setSoTimeout(10000);
		while(!stop)
		{ 
			Socket sock=server.accept();
			awaitingClients.put(sock);
			ArrayList<Object> arr=new ArrayList<>();
			arr.add(sock);
			arr.add("add awaitingClients");
			ServerModel.this.setChanged();
			ServerModel.this.notifyObservers(arr);
			executor.submit(new ServerTask());
		}
		server.close();	
	}
}

