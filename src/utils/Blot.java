package utils;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Blot implements Transferable {
	public final Line bound = new Line();
	public Color fill = Color.BLACK;
	
	public void input(InputStream is) throws IOException {
		fill = PrintUtils.readColor(is);
		bound.input(is);
	}

	public void print(OutputStream os) throws IOException {
		PrintUtils.writeColor(os, fill);
		bound.print(os);
	}
}
