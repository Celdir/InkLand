package ServerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//A basic server connection that joins to a client
public class ServerSide extends Connection{
	ServerSocket socket;
	Socket clientSocket;
	PrintWriter out;
	BufferedReader in;

	@Override
	public boolean isClosed(){
		return socket == null || socket.isClosed() || clientSocket == null || clientSocket.isClosed();
	}

	public ServerSide(MessageReceiver rec){
		super(rec);
		//Open server on port 44394 & wait for a client to join
		try{socket = new ServerSocket(44394);}
		catch(IOException e){
			e.printStackTrace();
			return;
		}
		System.out.println("Server opened, waiting for client...");

		try{
			clientSocket = socket.accept();
			out = new PrintWriter(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}
		catch(IOException e){e.printStackTrace();}
		System.out.println("Client connected");

		//Thread to read incoming data from client
		new Thread(){
			@Override public void run() {
				while(!isClosed()){
					try{
						synchronized(clientSocket){
							while(in.ready()){
								String line = in.readLine();
//								System.out.println("Received: "+line);
								receiver.receiveMessage(line);
							}
						}
					}
					catch(IOException e){e.printStackTrace();}
				}
			}
		}.start();
	}

	@Override
	public void close(){
		try{
			synchronized(clientSocket){
				if(socket != null) socket.close();
				if(clientSocket != null) clientSocket.close();
			}
		}
		catch(IOException e){}
	}

	@Override
	public void println(String message){
		if(isClosed()) return;
		out.println(message);
		out.flush();
//		System.out.println("Sent: "+message);
	}
}