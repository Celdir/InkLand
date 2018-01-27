package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Transferable {
	public void input(InputStream is) throws IOException;
	public void print(OutputStream os) throws IOException;
}
