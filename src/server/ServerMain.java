package server;

import java.io.IOException;
import java.util.Scanner;
import client.Orientation;
import serverAPI.*;
import utils.BodyList;
import utils.Utils;

public class ServerMain{
	BodyList bodyList;

	public static void main(String[] args){
		Connection server = new ServerSide(new MessageReceiver(){
			@Override
			public void receiveMessage(String message) {
				Orientation orientation = new Orientation();
				try{
					orientation.input(Utils.toInputStream(message));
//					bodyList.update(orientation);
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		});

		Scanner scan = new Scanner(System.in);
		while(scan.hasNextLine()){
			server.println(scan.nextLine());
		}
		scan.close();
	}
}