package server;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.Timer;
import serverAPI.*;
import serverAPI.ServerSide.*;
import utils.*;

public class ServerMain implements MessageReceiver, ActionListener, ConnectionListener{
	public static void main(String[] args){ new ServerMain(); }

	BodyList bodyList;
	Settings settings;
	HashMap<Integer, Player> players;
	Connection serverHook;
	Blot playerShape;
	Ink[] inks;

	ServerMain(){
		settings = new Settings();
		players = new HashMap<Integer, Player>();
		bodyList = new BodyList();
		inks = Utils.loadInks(settings.getObject("ink-types"));
		serverHook = new ServerSide(this, this, settings);
		playerShape = Utils.getPlayerShape(settings);
		new Timer(settings.getInt("server-timer-resolution", 10), this).start();
	}

	@Override public void connectionOpened(Client client){
		players.put(client.id, new Player(inks.clone()));
		// Send inks to the client
		StringBuilder builder = new StringBuilder("").append(inks.length).append(' ');
		for(Ink ink : inks) builder.append(PrintUtils.toString(ink));
		client.out.print(builder.toString());
		client.out.flush();
	}

	@Override
	public void receiveMessage(String message) {
		int i = message.indexOf(' '), id = Integer.parseInt(message.substring(0, i));
		message = message.substring(i+1);
		try{//Radians
			// Assume all messages from a client are just about orientation
			Player player = players.get(id);
			player.orientation.input(PrintUtils.toInputStream(message));
			// TODO: send paint strokes (ink usage)
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
		Stroke s = new Stroke();
		s.pen.color = Color.BLACK;
		s.pen.thickness = 0.1;
		s.path.points.add(new Point2D.Double(-4, 10));
		s.path.points.add(new Point2D.Double(4, 10));
		bodyList.strokes.add(s);
		serverHook.println(PrintUtils.toString(bodyList));
	}
}