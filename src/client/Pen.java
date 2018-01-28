package client;

import java.awt.Point;
import javax.swing.event.MouseInputAdapter;

public class Pen extends MouseInputAdapter {
	public final ArrayList<Point> points = new ArrayList<>();
	public final double SAMPLE_DIST = 5.0;

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
