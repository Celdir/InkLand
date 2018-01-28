package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Stroke implements Transferable {
	public final Line path = new Line();
	public final PenType pen = new PenType();
	
	boolean completed;
	
	public void input(InputStream is) throws IOException {
		completed = (char)is.read() == 'C';
		path.input(is);
		pen.input(is);
	}

	public void print(OutputStream os) throws IOException {
		os.write(completed ? 'C' : 'I');
		path.print(os);
		pen.print(os);
	}
}