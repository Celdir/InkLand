package client;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.Timer;
import com.sun.glass.events.KeyEvent;
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

	ClientMain() {
		settings = new Settings();
		serverHook = new ClientSide(this, settings);
		keyboardHook = new KeyboardState();
		inkComp = new InkComponent();

		mainframe = new JFrame("Ink Game");
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainframe.setSize((int)(screenSize.getWidth()/1.5), (int)(screenSize.getHeight()/1.5));
		mainframe.setLocationRelativeTo(null);
		mainframe.add(inkComp);
		mainframe.setVisible(true);

		new Timer(1, this).start();
	}

	@Override
	public void receiveMessage(String message) {
		try{ inkComp.list.input(Utils.toInputStream(message)); }
		catch(IOException e) { e.printStackTrace(); }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mainframe.repaint();
		serverHook.println(Utils.toString(inkComp.myPosition));
		if(keyboardHook.isPressed(KeyEvent.VK_W)){
			
		}
		else if(keyboardHook.isPressed(KeyEvent.VK_S)){
			
		}
		if(keyboardHook.isPressed(KeyEvent.VK_A));//move
		if(keyboardHook.isPressed(KeyEvent.VK_S));//move
		if(keyboardHook.isPressed(KeyEvent.VK_D));//move
	}
}
