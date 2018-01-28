package utils;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JOptionPane;

public class Utils{
	public static Blot getPlayerShape(Settings settings){
		Blot shape = new Blot();
		shape.fill =  Color.BLACK;
		try{
			shape.bound.input(PrintUtils.toInputStream(settings.getString("player-shape") + " |"));
			shape.fill = colorFromString(settings.getString("player-color", "1 100 50 0"));
		}
		catch(IOException | NumberFormatException e){
			shape.bound.points.add(new Point2D.Double(0, 0));
			shape.bound.points.add(new Point2D.Double(0, 2));
			shape.bound.points.add(new Point2D.Double(1, 5));
		}
		return shape;
	}

	public static Color colorFromString(String str){
		String[] rgba = str.split(" ");
		return new Color(
				Integer.parseInt(rgba[0]),//r
				Integer.parseInt(rgba[1]),//g
				Integer.parseInt(rgba[2]),//b
				Integer.parseInt(rgba[3]));//a
	}

	//{name: blue, starting: 100, capacity: 100, refill-rate: 2.0, color: 10 10 255 255}
	public static Ink[]/*HashMap<String, Ink>*/ loadInks(Object yaml){
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> inksData = (List<HashMap<String, Object>>)yaml;
		HashMap<String, Ink> inks = new HashMap<String, Ink>();

		for(HashMap<String, Object> inkData : inksData){
			Ink ink = new Ink();
			ink.name = inkData.get("name").toString();
			ink.color = Utils.colorFromString(inkData.get("color").toString());
			ink.REGEN_RATE = Double.parseDouble(inkData.get("refill-rate").toString());
			ink.CAPACITY = Double.parseDouble(inkData.get("capacity").toString());
			ink.amount = Double.parseDouble(inkData.get("starting").toString());
			inks.put(ink.name, ink);
		}
//		return inks;
		return inks.values().toArray(new Ink[inks.size()]);
	}
	
	//{name: 'Basic Pen', color: '0 0 0 255', inks: {black: 1}, physics: {type: rigid, density: 1}}
	@SuppressWarnings("unchecked")
	public static HashMap<String, Pen> loadPens(Object yaml){
		HashMap<String, Pen> pens = new HashMap<String, Pen>();
		for(HashMap<String, Object> penData : (List<HashMap<String, Object>>)yaml){
			try{
				Pen pen = new Pen();
				pen.name = penData.get("name").toString();
				pen.color = Utils.colorFromString(penData.get("color").toString());
				pen.thickness = Double.parseDouble(penData.get("width").toString());
				// Read ink-usage
				for(Entry<String, Object> e : ((HashMap<String, Object>)penData.get("inks")).entrySet()){
					pen.inkUsage.put(e.getKey(), Double.parseDouble(e.getValue().toString()));
				}
				HashMap<String, Object> physics = (HashMap<String, Object>)penData.get("physics");
				try{
					//TODO: density
//					pen.density = Double.parseDouble(physics.get("density").toString());
					pen.type = Pen.PhysicsType.valueOf(physics.get("type").toString().toUpperCase());
				}
				catch(IllegalArgumentException ex){
					pen.type = Pen.PhysicsType.RIGID;
				}
				pens.put(pen.name, pen);
			}
			catch(NullPointerException ex){
				JOptionPane.showMessageDialog(null, "Invalid pen configuration!",
						"Error loading settings.yml", JOptionPane.ERROR_MESSAGE);
			}
		}
		return pens;
	}
}