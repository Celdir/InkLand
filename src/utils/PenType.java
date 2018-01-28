package utils;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PenType implements Transferable {
	public Color color;
	public double thickness;

	public void input(InputStream is) throws IOException {
		color = PrintUtils.readColor(is);
		thickness = PrintUtils.readDouble(is);
	}

	public void print(OutputStream os) throws IOException {
		PrintUtils.writeColor(os, color);
		PrintUtils.writeDouble(os, thickness);
	}
}
