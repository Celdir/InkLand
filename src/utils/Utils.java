package utils;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	public static Ink[] loadInks(Object yaml){
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> inksData = (List<HashMap<String, Object>>)yaml;
		List<Ink> inks = new ArrayList<Ink>();

		for(HashMap<String, Object> inkData : inksData){
			//{name: blue, starting: 100, capacity: 100, refill-rate: 2.0, color: 10 10 255 255}
			Ink ink = new Ink();
			ink.CAPACITY = Double.parseDouble(inkData.get("capacity").toString());
			ink.REGEN_RATE = Double.parseDouble(inkData.get("refill-rate").toString());
			ink.amount = Double.parseDouble(inkData.get("starting").toString());
			ink.name = inkData.get("name").toString();
			ink.color = Utils.colorFromString(inkData.get("color").toString());
			inks.add(ink);
		}
		return inks.toArray(new Ink[inks.size()]);
	}
}