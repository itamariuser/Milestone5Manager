package model.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A class for handling sokoban clients using thread pool.
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
	/**
	 * Removes the client that the address refers to from the awaiting clients list.
	 * @param addr - The client's address. 
	 */
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
		awaitingClients=new LinkedBlockingQueue<>();
		handledClients=new LinkedBlockingQueue<>();
	}
	/**
	 * A class for updating the client list and history.
	 */
	private class ServerTask implements Runnable {
		@Override
		public void run() {
			Socket aClient;
			try {
				aClient = awaitingClients.take();
				arr.clear();
				arr.add(aClient.getInetAddress());
				arr.add("remove awaitingClients");
				ServerModel.this.setChanged();
				ServerModel.this.notifyObservers(arr);
				handledClients.put(aClient);
				arr.clear();
				arr.add(aClient.getInetAddress());
				arr.add("add handledClients");
				ServerModel.this.setChanged();
				ServerModel.this.notifyObservers(arr);
				ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
				aClient.close();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	/**
	 * Run the server.
	 */
	@Override
	public void runServer() throws Exception
	{
		ExecutorService executor = Executors.newFixedThreadPool(this.nThreads);
		ServerSocket server = new ServerSocket(this.port);
		while(!stop)
		{ 
			Socket sock=server.accept();
			awaitingClients.put(sock);
			ArrayList<Object> arr=new ArrayList<>();
			arr.add(sock.getInetAddress());
			arr.add("add awaitingClients");
			ServerModel.this.setChanged();
			ServerModel.this.notifyObservers(arr);
			executor.execute(new ServerTask());
		}
		executor.shutdown();
		server.close();	
	}
	/**
	 * Shutdown the server.
	 */
	public void shutdownServer()
	{
		this.stop = true;
	}
}

