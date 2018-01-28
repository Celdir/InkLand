package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.Timer;
import serverAPI.*;
import utils.PrintUtils;
import utils.Settings;

public class ClientMain implements MessageReceiver, ActionListener {
	public static void main(String[] args){ new ClientMain(); }

	Settings settings;
	Connection serverHook;
	KeyboardState keyboardHook;
	JFrame mainframe;
	InkComponent inkComp;
	final double MOVEMENT_SPEED;
	final double ROTATE_SPEED;

	ClientMain() {
		settings = new Settings();
		serverHook = new ClientSide(this, settings);
		keyboardHook = new KeyboardState(settings);
		//TODO: Pause here to wait for special msgs from server?
		mainframe = new ClientFrame(settings);
		mainframe.add(inkComp = new InkComponent(settings));
		mainframe.setVisible(true);

		MOVEMENT_SPEED = settings.getDouble("movement-speed", 1);
		ROTATE_SPEED = settings.getDouble("rotate-speed", 1);
		new Timer(settings.getInt("client-timer-resolution", 10), this).start();
	}

	@Override
	public void receiveMessage(String message){
		if(inkComp == null){
			//read stuff 
		}
		else try{
			synchronized(inkComp.list){
				inkComp.list.input(PrintUtils.toInputStream(message)); 
			}
		}
		catch(IOException e){e.printStackTrace();}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mainframe.repaint();
		serverHook.println(PrintUtils.toString(inkComp.myPosition));

		// Update player's position
		if(mainframe.hasFocus()){
			if(keyboardHook.UP != keyboardHook.DOWN){
				inkComp.myPosition.translateBy(0, MOVEMENT_SPEED *
						(keyboardHook.UP ? 1 : -1), inkComp.rotLocked);
			}
			if(keyboardHook.LEFT != keyboardHook.RIGHT){
				inkComp.myPosition.translateBy(MOVEMENT_SPEED *
						(keyboardHook.RIGHT ? 1 : -1), 0, inkComp.rotLocked);
			}
			if(keyboardHook.CLOCK != keyboardHook.COUNTER){
				inkComp.myPosition.rotateBy(ROTATE_SPEED * (keyboardHook.CLOCK ? 1 : -1));
			}
		}
	}
}