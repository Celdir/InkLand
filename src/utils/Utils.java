package utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Utils{
	public static InputStream toInputStream(String str){
		return new ByteArrayInputStream(str.getBytes());
	}
}