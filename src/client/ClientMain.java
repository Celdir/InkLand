package client;

import java.util.Scanner;
import serverAPI.*;
import serverAPI.Connection.MessageReceiver;

public class ClientMain{

	public static void main(String[] args){
		// Connect to server
		Connection connection = new ClientSide(new MessageReceiver(){
			@Override
			public void receiveMessage(String message){
				System.out.println("Received: " + message);
			}
		}, "127.0.0.1");

		Scanner scan = new Scanner(System.in);
		while(scan.hasNextLine()){
			String message = scan.nextLine();
			connection.println(message);
			System.out.println("Sent: " + message);
		}
		scan.close();
	}
}
