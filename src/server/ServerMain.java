package server;

import java.io.IOException;
import java.util.HashMap;
import serverAPI.*;
import utils.BodyList;
import utils.Settings;
import utils.Utils;

public class ServerMain implements MessageReceiver{
	static void main(String[] args){ new ServerMain(); }

	BodyList bodyList;
	Settings settings;
	HashMap<Integer, Player> players;
	
	ServerMain(){
		settings = new Settings();
		players = new HashMap<Integer, Player>();
		new ServerSide(this, settings);
		
		
	}

	@Override
	public void receiveMessage(String message) {
		int i = message.indexOf(' '), id = Integer.parseInt(message.substring(0, i));
		message = message.substring(i+1);
		try{
			// Assume all messages from a client are just their orientation
			players.get(id).orientation.input(Utils.toInputStream(message));
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}