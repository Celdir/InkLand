package client;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import serverAPI.*;

public class ClientMain{
	public static void main(String[] args){
//		Scanner scan = new Scanner(System.in);
//		System.out.println("Enter a server address: ");
//		String host = scan.nextLine();
//		scan.close();
		
		new ClientSide(new MessageReceiver(){
			@Override
			public void receiveMessage(String message){
				System.out.println("Received: " + message);
			}
		}, "127.0.0.1");

		JFrame mainframe = new JFrame("Ink Game");
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainframe.setSize((int)(screenSize.getWidth()/1.5), (int)(screenSize.getHeight()/1.5));
		mainframe.setLocationRelativeTo(null);
		mainframe.add(new InkComponent());
		mainframe.setVisible(true);
	}
}
