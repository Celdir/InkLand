package utils;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InkType implements Transferable {
	Color color;
	double regenRate;

	public void input(InputStream is) throws IOException {
		color = PrintUtils.readColor(is);
		regenRate = PrintUtils.readDouble(is);
	}

	public void print(OutputStream os) throws IOException {
		PrintUtils.writeColor(os, color);
		PrintUtils.writeDouble(os, regenRate);
	}
}