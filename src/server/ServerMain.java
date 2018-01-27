package server;

import java.io.IOException;
import client.Orientation;
import serverAPI.*;
import utils.BodyList;
import utils.Settings;
import utils.Utils;

public class ServerMain{
	BodyList bodyList;
	static Settings settings;
	public static void main(String[] args){
		settings = new Settings();
		new ServerSide(new MessageReceiver(){
			@Override
			public void receiveMessage(String message) {
				Orientation orientation = new Orientation();
				try{
					orientation.input(Utils.toInputStream(message));
					//TODO: bodyList.update(orientation);
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}, settings);
	}
}