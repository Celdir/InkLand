package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class BodyList implements Transferable {
	public final ArrayList<Blot> blots = new ArrayList<>();
	public final ArrayList<Stroke> strokes = new ArrayList<>();
	
	public void input(InputStream is) throws IOException {
		int szB = read(is);
		int szS = read(is);
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
		PrintStream out = new PrintStream(os);
		out.printf("%d %d ", blots.size(), strokes.size());
		for(Blot b : blots) b.print(os);
		for(Stroke s : strokes) s.print(os);
	}
	
	private static int read(InputStream is) throws IOException {
		String s = "";
		char c;
		while((c = (char)is.read()) != ' ') s += c;
		return Integer.parseInt(s);
	}
}