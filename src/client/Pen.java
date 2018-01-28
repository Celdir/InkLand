package client;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.event.MouseInputAdapter;
import utils.Settings;

public class Pen extends MouseInputAdapter {
	public final ArrayList<Point> points = new ArrayList<Point>();
	final double SAMPLE_DIST;
	
	Pen(Settings settings){
		SAMPLE_DIST = settings.getDouble("mouse-sampling-distance", 5.0);
	}

	public void mousePressed(MouseEvent e) {
		points.clear();
		points.add(e.getPoint());
	}

	public void mouseDragged(MouseEvent e) {
		points.add(e.getPoint());
	}

	public void mouseReleased(MouseEvent e) {
		Point last = points.get(0);
		for (int i = 1; i < points.size(); ++i) {
			Point cur = points.get(i);
		}
	}
}
