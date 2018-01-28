package client;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import javax.swing.event.MouseInputAdapter;
import utils.Line;
import utils.Settings;

public class MouseHook extends MouseInputAdapter{
//	public final LinkedList<Point2D.Double> points;
	final ArrayDeque<Line> backlog = new ArrayDeque<Line>();
	final Line curLine = new Line();
	private Point2D.Double lastPoint, curPoint;
	final double SAMPLE_DIST, SAMPLE_DIST_SQ;

	MouseHook(Settings settings){
		SAMPLE_DIST = settings.getDouble("mouse-sampling-distance", 5.0);
		SAMPLE_DIST_SQ = SAMPLE_DIST*SAMPLE_DIST;
	}

	@Override public void mousePressed(MouseEvent e){
		curLine.points.add(lastPoint = 
				new Point2D.Double(e.getPoint().getX(), e.getPoint().getY()));
	}

	@Override public void mouseDragged(MouseEvent e){
		if(curPoint.distanceSq(lastPoint) <= SAMPLE_DIST_SQ){
			curLine.points.add(curPoint);
			lastPoint = curPoint;
		}
	}

	@Override public void mouseReleased(MouseEvent e){
		curLine.points.add(new Point2D.Double(e.getPoint().getX(), e.getPoint().getY()));
		synchronized(backlog){
			backlog.add(curLine);
		}
		curLine.points.clear();
	}
}
