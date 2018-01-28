package utils;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PrintUtils {
	public static String readToken(InputStream is) throws IOException {
		String s = "";
		char c;
		while((c = (char)is.read()) != ' ') s += c;
//		while((c = (char)is.read()) == ' ');
//		System.out.println("s="+s);
		return s;
	}

	public static int readInt(InputStream is) throws IOException {
		return Integer.parseInt(readToken(is));
	}

	public static double readDouble(InputStream is) throws IOException {
		return Double.parseDouble(readToken(is));
	}

	public static Color readColor(InputStream is) throws IOException {
		return new Color(readInt(is), readInt(is), readInt(is), readInt(is));
	}

	public static void writeToken(OutputStream os, String token) throws IOException {
		os.write(token.getBytes());
		os.write(' ');
	}
	
	public static void writeInt(OutputStream os, int i) throws IOException {
		writeToken(os, Integer.toString(i));
	}
	
	public static void writeDouble(OutputStream os, double d) throws IOException {
		writeToken(os, Double.toString(d));
	}
	
	public static void writeColor(OutputStream os, Color c) throws IOException {
		writeInt(os, c.getRed());
		writeInt(os, c.getGreen());
		writeInt(os, c.getBlue());
		writeInt(os, c.getAlpha());
	}

	public static InputStream toInputStream(String str){
		return new ByteArrayInputStream(str.getBytes());
	}

	public static String toString(Transferable obj){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{ obj.print(os); }
		catch(IOException e1){ e1.printStackTrace(); }
		return os.toString();
	}
}
