package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import utils.Blot;
import utils.BodyList;
import utils.Ink;
import utils.Orientation;
import utils.Pen;
import utils.Settings;
import utils.Stroke;

public class InkComponent extends JComponent {
	private static final long serialVersionUID = -8844473018708075151L;

	final BodyList list = new BodyList();
	final Orientation myPosition = new Orientation();
	Ink[] inks;
	Pen[] pens;
	final int SCALE;
	boolean rotLocked = true;

	InkComponent(Settings settings){
		SCALE = settings.getInt("scale", 30);
	}

	@Override protected void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		synchronized (list) {
			for(Blot b : list.blots) {
				g.setColor(b.fill);
				int npts = b.bound.points.size();
				int[] xs = new int[npts];
				int[] ys = new int[npts];
				for(int i = 0; i < npts; ++i) {
					Point2D.Double pt = myPosition.world2local(b.bound.points.get(i), rotLocked);
					xs[i] = local2pixel(pt.x, false);
					ys[i] = local2pixel(pt.y, true);
				}
				g.fillPolygon(xs, ys, npts);
			}
			for(Stroke s : list.strokes) {
				g.setColor(s.pen.color);
				int npts = s.path.points.size();
				int[] xs = new int[npts];
				int[] ys = new int[npts];
				for(int i = 0; i < npts; ++i) {
					Point2D.Double pt = myPosition.world2local(s.path.points.get(i), rotLocked);
					xs[i] = local2pixel(pt.x, false);
					ys[i] = local2pixel(pt.y, true);
				}
				((Graphics2D)g).setStroke(new java.awt.BasicStroke((float)(getScale()*s.pen.thickness)));
				g.drawPolyline(xs, ys, npts);
			}
		}
	}

	private int local2pixel(double pos, boolean vert) {
		if(vert) pos *= -1;
		pos *= getScale();
		return (int)pos + (vert ? getHeight() : getWidth())/2;
	}

	private double getScale() {
		return SCALE;
	}
}
