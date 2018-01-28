package utils;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class Settings {
//	private Map<String, String> map;
	private Map<String, Object> map;
	private String filename = "settings.yml";

	@SuppressWarnings("unchecked")
	public Settings(String filename){
		this.filename = filename;
		
		//Initialize this Settings object with a map (setting -> value)
		Yaml yaml = new Yaml();
		map = (Map<String, Object>) yaml.load(
			FileIO.loadFile(filename, getClass().getResourceAsStream("/settings.yml")));
//		map = FileIO.loadYaml(filename, getClass().getResourceAsStream("/settings.yml"));
	}
	
	public Settings(){
		this("settings.yml");
	}

	public boolean getBoolean(String key){
		Object val = map.get(key.toLowerCase());
		return val != null && Boolean.parseBoolean(val.toString());
	}
	public boolean getBoolean(String key, boolean def){
		Object val = map.get(key.toLowerCase());
		return val == null ? def : Boolean.parseBoolean(val.toString());
	}

	public int getInt(String key){
		Object val = map.get(key.toLowerCase());
		return val != null && val.toString().matches("\\d+") ? Integer.parseInt(val.toString()) : 0;
	}
	public int getInt(String key, int def){
		Object val = map.get(key.toLowerCase());
		return val != null && val.toString().matches("\\d+") ? Integer.parseInt(val.toString()) : def;
	}

	public double getDouble(String key){
		Object val = map.get(key.toLowerCase());
		if(val == null) return 0;
		try{return Double.parseDouble(val.toString());}
		catch(NumberFormatException e){return 0;}
	}
	public double getDouble(String key, double def){
		Object val = map.get(key.toLowerCase());
		if(val == null) return def;
		try{return Double.parseDouble(val.toString());}
		catch(NumberFormatException e){return def;}
	}

	public String getString(String key){
		Object val = map.get(key.toLowerCase());
		return val == null ? null : val.toString();
	}
	public String getString(String key, String def){
		Object val = map.get(key.toLowerCase());
		return val == null ? def : val.toString();
	}

	public Object getObject(String key){
		return map.get(key);
	}

	public void set(String key, Object value){
		map.put(key.toLowerCase(), value);
	}

	public void updateFile(){
		//Save current settings back to the file they were loaded from
		FileIO.saveYaml(filename, map);
	}
}
