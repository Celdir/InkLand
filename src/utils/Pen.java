package utils;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

public class Pen implements Transferable {
	// Suggested implementation:
	// Fixed=immovable, Rigid=fixed shape, Loose=free-floating string, Spring=stretchable
	public enum PhysicsType {FIXED, RIGID, LOOSE, SPRING, }

	public String name = "UNDEFINED";
	public Color color = Color.BLACK;
	public double thickness = 1;
	public PhysicsType type = PhysicsType.RIGID;
	public HashMap<String, Double> inkUsage = new HashMap<String, Double>();

	public void input(InputStream is) throws IOException {
		name = PrintUtils.readToken(is);
		color = PrintUtils.readColor(is);
		thickness = PrintUtils.readDouble(is);
		type = PhysicsType.valueOf(PrintUtils.readToken(is));
		int numInks = PrintUtils.readInt(is);
		for(int i=0; i<numInks; ++i)
			inkUsage.put(PrintUtils.readToken(is), PrintUtils.readDouble(is));
	}

	public void print(OutputStream os) throws IOException {
		PrintUtils.writeToken(os, name);
		PrintUtils.writeColor(os, color);
		PrintUtils.writeDouble(os, thickness);
		PrintUtils.writeToken(os, type.name());
		PrintUtils.writeInt(os, inkUsage.size());
		for(Entry<String, Double> ink : inkUsage.entrySet()){
			PrintUtils.writeToken(os, ink.getKey());
			PrintUtils.writeDouble(os, ink.getValue());
		}
	}
}
