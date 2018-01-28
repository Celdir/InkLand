package utils;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Blot implements Transferable {
	public final Line bound = new Line();
	public Color fill = Color.BLACK;
	
	public void input(InputStream is) throws IOException {
		fill = new Color(Utils.read(is), Utils.read(is), Utils.read(is), Utils.read(is));
		bound.input(is);
	}

	public void print(OutputStream os) throws IOException {
		String out = "";
		out += fill.getRed() + " " + fill.getGreen() + " " + fill.getBlue() + " " + fill.getTransparency() + " ";
		os.write(out.getBytes());
		bound.print(os);
	}
}
