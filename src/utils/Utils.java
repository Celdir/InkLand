package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils{
	public static InputStream toInputStream(String str){
		return new ByteArrayInputStream(str.getBytes());
	}

	public static int read(InputStream is) throws IOException {
		String s = "";
		char c;
		while((c = (char)is.read()) != ' ') s += c;
		return Integer.parseInt(s);
	}

	public static String toString(Transferable obj){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{ obj.print(os); }
		catch(IOException e1){ e1.printStackTrace(); }
		return os.toString();
	}
}