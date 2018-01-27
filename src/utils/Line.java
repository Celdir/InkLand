package utils;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Line implements Transferable {
	public final ArrayList<Point2D.Double> points = new ArrayList<>();

	boolean completed;
	
	public void input(InputStream is) throws IOException {
		points.clear();
		String coords = "";
		char c;
		while ((c = (char) is.read()) != '|') {
			coords += c;
		}

		Scanner s = new Scanner(coords);
		while (s.hasNextDouble()) {
			double x = s.nextDouble();
			double y = s.nextDouble();
			points.add(new Point2D.Double(x, y));
		}
	}

	public void print(OutputStream os) throws IOException {
		String out = "";
		for (Point2D.Double p : points) {
			out += p.x + " " + p.y + " ";
		}
		out += "| ";
		os.write(out.getBytes());
	}

}
