package utils;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Ink implements Transferable {
	public String name;
	public Color color;
	public double REGEN_RATE, CAPACITY, amount;

	public void input(InputStream is) throws IOException {
		name = PrintUtils.readToken(is);
		color = PrintUtils.readColor(is);
		REGEN_RATE = PrintUtils.readDouble(is);
		CAPACITY = PrintUtils.readDouble(is);
		amount = PrintUtils.readDouble(is);
	}

	public void print(OutputStream os) throws IOException {
		PrintUtils.writeToken(os, name);
		PrintUtils.writeColor(os, color);
		PrintUtils.writeDouble(os, REGEN_RATE);
		PrintUtils.writeDouble(os, CAPACITY);
		PrintUtils.writeDouble(os, amount);
	}
}