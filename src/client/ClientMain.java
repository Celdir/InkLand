package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JFrame;
import javax.swing.Timer;
import serverAPI.*;
import utils.*;

public class ClientMain implements MessageReceiver, ActionListener {
	public static void main(String[] args){ new ClientMain(); }

	Settings settings;
	Connection serverHook;
	KeyboardHook keyboardHook;
	MouseHook mouseHook;
	JFrame mainframe;
	InkComponent inkComp;
	final double MOVEMENT_SPEED;
	final double ROTATE_SPEED;
	Pen currentPen;

	ClientMain() {
		settings = new Settings();
		inkComp = new InkComponent(settings);
		serverHook = new ClientSide(this, settings);
		keyboardHook = new KeyboardHook(settings);
		mouseHook = new MouseHook(settings);
		mainframe = new ClientFrame(settings, inkComp);

		MOVEMENT_SPEED = settings.getDouble("movement-speed", 1);
		ROTATE_SPEED = settings.getDouble("rotate-speed", 1);
		new Timer(settings.getInt("client-timer-resolution", 10), this).start();
	}

	@Override
	public void receiveMessage(String message){
		InputStream is = PrintUtils.toInputStream(message);
		if(inkComp.inks == null){
			try{
				// Read inks from server
				int numInks = PrintUtils.readInt(is);
				inkComp.inks = new Ink[numInks];
				for(int i=0; i<numInks; ++i){
					inkComp.inks[i] = new Ink();
					inkComp.inks[i].input(is);
				}
				// Read pens from server
				int numPens = PrintUtils.readInt(is);
				inkComp.pens = new Pen[numPens];
				for(int i=0; i<numPens; ++i){
					inkComp.pens[i] = new Pen();
					inkComp.pens[i].input(is);
				}
				currentPen = inkComp.pens[0];
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else try{
			synchronized(inkComp.list){
				inkComp.list.input(is); 
			}
		}
		catch(IOException e){e.printStackTrace();}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mainframe.repaint();

		// Update player's position
		if(mainframe.hasFocus()){
			// Read keys
			if(keyboardHook.UP != keyboardHook.DOWN){
				inkComp.myPosition.translateBy(0, MOVEMENT_SPEED *
						(keyboardHook.UP ? 1 : -1), inkComp.rotLocked);
			}
			if(keyboardHook.LEFT != keyboardHook.RIGHT){
				inkComp.myPosition.translateBy(MOVEMENT_SPEED *
						(keyboardHook.RIGHT ? 1 : -1), 0, inkComp.rotLocked);
			}
			if(keyboardHook.CLOCK != keyboardHook.COUNTER){
				inkComp.myPosition.rotateBy(ROTATE_SPEED * (keyboardHook.CLOCK ? -1 : 1));
			}

			// Send position update
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try{inkComp.myPosition.print(os);}
			catch(IOException e2){e2.printStackTrace();}

			// Read & send mouse movement
			synchronized(mouseHook.backlog){
				try{
					PrintUtils.writeInt(os, mouseHook.backlog.size());
	
					// Send all backlogged strokes
					Stroke stroke = new Stroke();
					while(!mouseHook.backlog.isEmpty()){
						stroke.path = mouseHook.backlog.pollFirst();
						stroke.pen = currentPen;
						stroke.completed = true;
						stroke.print(os);
					}
					// Send the current WIP stroke
					if(!mouseHook.curLine.points.isEmpty()){
						stroke.path = mouseHook.curLine;
						stroke.pen = currentPen;
						stroke.completed = false;
						stroke.print(os);
					}
					serverHook.println(os.toString());
				}
				catch(IOException ex){
					ex.printStackTrace();
					mouseHook.backlog.clear();
				}
			}
		}
	}
}