package serverAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import utils.Settings;

public class ServerSide extends Connection{
//	final int PORT = 44394;
	final int MAX_CLIENTS;
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

	public ServerSide(MessageReceiver rec, Settings settings){
		super(rec);
		receiver = rec;
		MAX_CLIENTS = settings.getInt("max-players", 20);
		try{socket = new ServerSocket(settings.getInt("port", 44394));}
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
				if(client.socket == null || client.socket.isClosed()){
					it.remove();
					System.out.println("A client left the server");
					receiver.receiveMessage(client.id+" QUIT");
				}
				else{
					try{
						while(client.in.ready()){
							receiver.receiveMessage(client.id+" "+client.in.readLine());
						}
						if(outgoing != null){
							client.out.print(outgoing.toString());
							client.out.flush();
							if(client.out.checkError()) throw new IOException();
						}
					}
					catch(IOException e){
						try{client.socket.close();} catch(IOException e1){}
						client.socket = null;
					}
				}
			}
			outgoing = null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void close(){
		connectionWaitThread.stop();
		if(socket != null){
			try{socket.close();} catch(IOException e){}
			socket = null;
		}
	}

	public int numClients(){
		return clients.size();
	}

	public void println(String message){
		if(outgoing == null) outgoing = new StringBuilder(message).append('\n');
		else outgoing.append(message).append('\n');
	}
}