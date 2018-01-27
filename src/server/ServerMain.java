package server;

import java.util.Scanner;
import serverAPI.*;
import serverAPI.Connection.MessageReceiver;

public class ServerMain{
	public static void main(String[] args){
		Connection server = new ServerSide(new MessageReceiver(){
			@Override
			public void receiveMessage(String message) {
				System.out.println("Received: "+message);
			}
		});

		Scanner scan = new Scanner(System.in);
		while(scan.hasNextLine()){
			server.println(scan.nextLine());
		}
		scan.close();
	}
}