package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class BodyList implements Transferable {
	public final ArrayList<Blot> blots = new ArrayList<>();
	public final ArrayList<Stroke> strokes = new ArrayList<>();
	
	public void clear() {
		blots.clear();
		strokes.clear();
	}
	
	public void input(InputStream is) throws IOException {
		int szB = PrintUtils.readInt(is);
		int szS = PrintUtils.readInt(is);
		blots.clear();
		strokes.clear();
		while(szB --> 0) {
			Blot b = new Blot();
			b.input(is);
			blots.add(b);
		}
		while(szS --> 0) {
			Stroke s = new Stroke();
			s.input(is);
			strokes.add(s);
		}
	}
	public void print(OutputStream os) throws IOException {
		PrintUtils.writeInt(os, blots.size());
		PrintUtils.writeInt(os, strokes.size());
		for(Blot b : blots) b.print(os);
		for(Stroke s : strokes) s.print(os);
	}
}