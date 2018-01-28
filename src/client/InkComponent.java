package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

import utils.Blot;
import utils.BodyList;

public class InkComponent extends JComponent {
	private static final long serialVersionUID = -8844473018708075151L;

	final BodyList list = new BodyList();
	final Orientation myPosition = new Orientation();
	boolean rotLocked = true;

	@Override protected void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		//System.out.println(list.blots.size() + list.strokes.size());
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
	}

	private int local2pixel(double pos, boolean vert) {
		if(vert) pos *= -1;
		pos *= getScale();
		return (int)pos + (vert ? getHeight() : getWidth())/2;
	}

	private double getScale() {
		return 30;
	}
}