package server;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import javax.swing.Timer;
import serverAPI.*;
import serverAPI.ServerSide.*;
import utils.*;

public class ServerMain implements MessageReceiver, ActionListener, ConnectionListener{
	public static void main(String[] args){ new ServerMain(); }

	BodyList bodyList;
	Settings settings;
	HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	Connection serverHook;
	Blot playerShape;
	Ink[] inks;
	HashMap<String, Pen> pens;

	ServerMain(){
		settings = new Settings();
		bodyList = new BodyList();
		inks = Utils.loadInks(settings.getObject("ink-types"));
		pens = Utils.loadPens(settings.getObject("pen-types"));
		serverHook = new ServerSide(this, this, settings);
		playerShape = Utils.getPlayerShape(settings);
		new Timer(settings.getInt("server-timer-resolution", 10), this).start();
	}

	@Override public void connectionOpened(Client client){
		players.put(client.id, new Player(inks.clone()));

		// Send inks & pens to the client
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{
			PrintUtils.writeInt(os, inks.length);
			for(Ink ink : inks) ink.print(os);
			PrintUtils.writeInt(os, pens.size());
			for(Pen pen : pens.values()) pen.print(os);
			client.out.println(os.toString());
			client.out.flush();
		}
		catch(IOException e){e.printStackTrace();}
	}

	@Override
	public void receiveMessage(String message) {
		try{
			InputStream is = PrintUtils.toInputStream(message);
			int playerId = PrintUtils.readInt(is);

			Player player = players.get(playerId);
			player.orientation.input(is);

			int newStrokes = PrintUtils.readInt(is);
			for(int i=0; i<newStrokes; ++i){
				Stroke stroke = new Stroke();
				stroke.input(is);
				bodyList.strokes.add(stroke);
				//TODO: Validate stroke!
			}
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
		//TODO: temporary Stroke to test display
		Stroke s = new Stroke();
		s.pen.color = Color.BLACK;
		s.pen.thickness = 0.1;
		s.path.points.add(new Point2D.Double(-4, 10));
		s.path.points.add(new Point2D.Double(4, 10));
		bodyList.strokes.add(s);

		serverHook.println(PrintUtils.toString(bodyList));
	}
}