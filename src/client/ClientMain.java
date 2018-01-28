package client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.Timer;
import serverAPI.*;
import utils.Settings;
import utils.Utils;

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
		keyboardHook = new KeyboardState();
		mainframe = new ClientFrame(settings);
		mainframe.add(inkComp = new InkComponent());
		mainframe.setVisible(true);

		MOVEMENT_SPEED = settings.getDouble("movement-speed", 1);
		ROTATE_SPEED = settings.getDouble("rotate-speed", 1);
		new Timer(settings.getInt("timer-resolution", 10), this).start();
	}

	@Override
	public void receiveMessage(String message) {
		if(inkComp == null) return;
		try {
			synchronized(inkComp.list) {
				inkComp.list.input(Utils.toInputStream(message)); 
			}
		} catch(IOException e) { e.printStackTrace(); }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mainframe.repaint();
		serverHook.println(Utils.toString(inkComp.myPosition));

		// Update player's position
		if(keyboardHook.isPressed(KeyEvent.VK_W)){
			inkComp.myPosition.translateBy(0, MOVEMENT_SPEED, inkComp.rotLocked);
		}
		else if(keyboardHook.isPressed(KeyEvent.VK_S)){
			inkComp.myPosition.translateBy(0, -MOVEMENT_SPEED, inkComp.rotLocked);
		}
		if(keyboardHook.isPressed(KeyEvent.VK_A)){
			inkComp.myPosition.translateBy(-MOVEMENT_SPEED, 0, inkComp.rotLocked);
		}
		else if(keyboardHook.isPressed(KeyEvent.VK_D)){
			inkComp.myPosition.translateBy(MOVEMENT_SPEED, 0, inkComp.rotLocked);
		}
		if(keyboardHook.isPressed(KeyEvent.VK_Q)){
			inkComp.myPosition.rotateBy(ROTATE_SPEED);
		}
		else if(keyboardHook.isPressed(KeyEvent.VK_E)){
			inkComp.myPosition.rotateBy(-ROTATE_SPEED);
		}
	}
}
