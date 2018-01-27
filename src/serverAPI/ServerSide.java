package serverAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerSide extends Connection{
	final int PORT = 44394, MAX_CLIENTS = 20;
	ServerSocket socket;
	ArrayList<Client> clients;
	Thread connectionWaitThread, ioThread;
	StringBuilder outgoing;
	MessageReceiver receiver;
	
	public interface ServerMessageReceiver extends MessageReceiver{
		void receiveMessage(Client client, String message);
	}
	
	@Override
	public boolean isClosed(){
		return socket == null || socket.isClosed() || clients == null;
	}

	static int nextId = 0;
	class Client{
		final int id;
		Socket socket;
		PrintWriter out;
		BufferedReader in;
		Client(Socket connection){
			id = ++nextId;
			socket = connection;
			try{
				out = new PrintWriter(connection.getOutputStream());
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}
			catch(IOException e){e.printStackTrace();}
		}
	}

	public ServerSide(MessageReceiver rec){
		super(rec);
		receiver = rec;
		try{socket = new ServerSocket(PORT );}
		catch(IOException e){e.printStackTrace();return;}

		connectionWaitThread = new Thread(){
			@Override public void run(){
				try{
					clients = new ArrayList<Client>();
					
					while(true){
						Socket connection = socket.accept();
						if(clients.size() == MAX_CLIENTS){
							PrintWriter temp = new PrintWriter(connection.getOutputStream());
							temp.println("Server is full!");
							temp.flush();
							connection.close();
							continue;
						}
						synchronized(clients){
							clients.add(new Client(connection));
						}
						System.out.println("Got a connection to a client");
					}
				}
				catch(IOException e){e.printStackTrace();}
			}
		};
		connectionWaitThread.start();
		
		ioThread = new Thread(){
			@Override public void run() {
				while(!socket.isClosed()){
					loop();
				}
			}
		};
		ioThread.start();

		System.out.println("Server opened");
	}

	public void loop(){
		synchronized(clients){
			Iterator<Client> it = clients.iterator();
			while(it.hasNext()){
				Client client = it.next();
				try{
					if(client.socket.isClosed()){
						it.remove();
						System.out.println("A client left the server");
					}
					else{
						while(client.in.ready()){
							receiver.receiveMessage(client.id+" "+client.in.readLine());
						}
						if(outgoing != null){
							client.out.print(outgoing.toString());
							client.out.flush();
						}
					}
				}
				catch(IOException e){e.printStackTrace();}
			}
			outgoing = null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void close(){
		connectionWaitThread.stop();
		if(socket != null) try{socket.close();} catch(IOException e){}
	}

	public int numClients(){
		return clients.size();
	}

	public void println(String message){
		if(outgoing == null) outgoing = new StringBuilder(message).append('\n');
		else outgoing.append(message).append('\n');
	}
}