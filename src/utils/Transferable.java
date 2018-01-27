package utils;

import java.io.InputStream;
import java.io.OutputStream;

public interface Transferable {
	public void input(InputStream is);
	public void print(OutputStream os);
}
