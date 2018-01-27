package client;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import utils.Transferable;

public class Orientation implements Transferable {
	private Point2D.Double position;
	private double rotRadians;
	private transient double s, c;
	
	Point2D.Double local2world(Point2D.Double in, boolean useRot) {
		double x, y;
		if(useRot) {
			x = in.x*c - in.y*s;
			y = in.x*s + in.y*c;
		} else {
			x = in.x;
			y = in.y;
		}
		x += position.x;
		y += position.y;
		return new Point2D.Double(x, y);
	}
	
	Point2D.Double world2local(Point2D.Double in, boolean useRot) {
		Point2D.Double pt = new Point2D.Double(in.x, in.y);
		pt.x -= position.x;
		pt.y -= position.y;
		if(useRot) {
			double x = pt.x;
			pt.x = pt.y*s + x*c;
			pt.y = pt.y*c - x*s;
		}
		return pt;
	}

	public double getRotation() {
		return rotRadians;
	}

	public void rotateBy(double rotRadians) {
		this.rotRadians += rotRadians;
		c = Math.cos(rotRadians);
		s = Math.sin(rotRadians);
	}
	
	public void translateBy(Point2D.Double displacement, boolean useRot) {
		translateBy(displacement.x, displacement.y, useRot);
	}
	
	public void translateBy(double dx, double dy, boolean useRot) {
		if(useRot) {
			double x = dx;
			dx = x*c - dy*s;
			dy = x*s + dy*c;
		}
		position.x += dx;
		position.y += dy;
	}

	public void input(InputStream is) throws IOException {
		String st = "";
		int ct = 0;
		while(ct < 3) {
			char c = (char)is.read();
			st += c;
			if(c == ' ') ++ct;
		}
		Scanner sc = new Scanner(st);
		position.x = sc.nextDouble();
		position.y = sc.nextDouble();
		rotRadians = sc.nextDouble();
		c = Math.cos(rotRadians);
		s = Math.sin(rotRadians);
		sc.close();
	}

	public void print(OutputStream os) throws IOException {
		PrintStream out = new PrintStream(os);
		out.printf("%0.5f %0.5f %0.5f ", position.x, position.y, rotRadians);
	}
}
