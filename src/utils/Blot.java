package utils;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Blot implements Transferable {
	public final Line bound = new Line();
	public Color fill = Color.BLACK;
	
	public void input(InputStream is) throws IOException {
		String rgb = "";
		char c;
		while ((c = (char) is.read()) != ' ') {
			rgb += c;
		}
		
		fill = new Color(Integer.parseInt(rgb));
		bound.input(is);
	}

	public void print(OutputStream os) throws IOException {
		String out = "";
		out += fill.getRGB() + " ";
		os.write(out.getBytes());
		bound.print(os);
	}
}
