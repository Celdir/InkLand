package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.Timer;
import serverAPI.*;
import utils.*;

public class ServerMain implements MessageReceiver, ActionListener{
	public static void main(String[] args){ new ServerMain(); }

	BodyList bodyList;
	Settings settings;
	HashMap<Integer, Player> players;
	Connection serverHook;
	Blot playerShape;

	ServerMain(){
		settings = new Settings();
		players = new HashMap<Integer, Player>();
		bodyList = new BodyList();
		serverHook = new ServerSide(this, settings);
		playerShape = Utils.getPlayerShape(settings);
		new Timer(settings.getInt("timer-resolution", 10), this).start();
	}

	@Override
	public void receiveMessage(String message) {
		int i = message.indexOf(' '), id = Integer.parseInt(message.substring(0, i));
		message = message.substring(i+1);
		try{//Radians
			// Assume all messages from a client are just about orientation
			Player player = players.get(id);
			if(player == null) players.put(id, player = new Player());
			player.orientation.input(PrintUtils.toInputStream(message));
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override public void actionPerformed(ActionEvent e){
		// Timer has been triggered! Send data to clients
		bodyList.clear();
		for(Player player : players.values()) {
			Blot shape = new Blot();
			shape.fill = playerShape.fill;
			shape.bound.points.clear();
			for(Point2D.Double pt : playerShape.bound.points)
				shape.bound.points.add(player.orientation.local2world(pt, true));
			bodyList.blots.add(shape);
		}
		serverHook.println(PrintUtils.toString(bodyList));
	}
}