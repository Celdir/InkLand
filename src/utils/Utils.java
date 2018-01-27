package utils;

import java.awt.Color;
import java.awt.geom.Point2D;
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

	public static Blot getPlayerShape(Settings settings){
		Blot shape = new Blot();
		shape.fill =  Color.BLACK;
		try{
			shape.bound.input(Utils.toInputStream(settings.getString("player-shape")));
			String[] rgb = settings.getString("player-color", "1 100 50 0").split(" ");
			shape.fill = new Color(
							Integer.parseInt(rgb[0]),//r
							Integer.parseInt(rgb[1]),//g
							Integer.parseInt(rgb[2]),//b
							Integer.parseInt(rgb[3]));//a
		}
		catch(IOException | NumberFormatException e){
			shape.bound.points.add(new Point2D.Double(0, 0));
			shape.bound.points.add(new Point2D.Double(0, 2));
			shape.bound.points.add(new Point2D.Double(1, 5));
		}
		return shape;
	}
}