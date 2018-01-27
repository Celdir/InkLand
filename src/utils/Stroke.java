package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Stroke implements Transferable {
	final Line path = new Line();
	final PenType pen = new PenType();
	
	boolean completed;
	
	@Override public void input(InputStream is) throws IOException {
		completed = (char)is.read() == 'C';
		path.input(is);
		pen.input(is);
	}

	@Override public void print(OutputStream os) throws IOException {
		os.write(completed ? 'C' : 'I');
		path.print(os);
		pen.print(os);
	}
}