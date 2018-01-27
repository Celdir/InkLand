package client;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;
import serverAPI.*;
import utils.Settings;

public class ClientMain{
	static Settings settings;
	public static void main(String[] args){
		settings = new Settings();
//		Scanner scan = new Scanner(System.in);
//		System.out.println("Enter a server address: ");
//		String host = scan.nextLine();
//		scan.close();
		
		new ClientSide(new MessageReceiver(){
			@Override
			public void receiveMessage(String message){
				System.out.println("Received: " + message);
			}
		}, settings);

		final JFrame mainframe = new JFrame("Ink Game");
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainframe.setSize((int)(screenSize.getWidth()/1.5), (int)(screenSize.getHeight()/1.5));
		mainframe.setLocationRelativeTo(null);
		mainframe.add(new InkComponent());
		mainframe.setVisible(true);
		
		new Timer(1, new ActionListener(){
			@Override public void actionPerformed(ActionEvent e){
				mainframe.repaint();
			}
		}).start(); 
	}
}
