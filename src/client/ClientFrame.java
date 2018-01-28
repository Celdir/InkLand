package client;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import utils.Settings;

public class ClientFrame extends JFrame{
	private static final long serialVersionUID = 2824773598973951192L;

	public ClientFrame(Settings settings){
		super("Ink Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(
				(int) (screenSize.getWidth() * (settings.getDouble("screen-width", 75)/100)),
				(int) (screenSize.getHeight() * (settings.getDouble("screen-height", 75)/100))
		);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}