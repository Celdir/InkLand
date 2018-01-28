package utils;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Blot implements Transferable {
	public final Line bound = new Line();
	public Color fill = Color.BLACK;
	
	public void input(InputStream is) throws IOException {
		String rgba = "";
		char c;
		for (int i = 0; i < 4; ++i) {
			while ((c = (char) is.read()) != ' ') {
				rgba += c;
			}
		}

		String[] s = rgba.split(" ");
		int r = Integer.parseInt(s[0]);
		int g = Integer.parseInt(s[1]);
		int b = Integer.parseInt(s[2]);
		int a = Integer.parseInt(s[3]);

		fill = new Color(r, g, b, a);
		bound.input(is);
	}

	public void print(OutputStream os) throws IOException {
		String out = "";
		out += fill.getRed() + " " + fill.getGreen() + " " + fill.getBlue() + " " + fill.getTransparency() + " ";
		os.write(out.getBytes());
		bound.print(os);
	}
}
