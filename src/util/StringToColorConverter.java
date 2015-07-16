package util;

import game.Color;

import java.util.HashMap;
import java.util.Map;

public class StringToColorConverter {
	
	private static final Map<String, Color> STRING_TO_COLOR = new HashMap<String, Color>();
	
	static {
		STRING_TO_COLOR.put("R", Color.R);
		STRING_TO_COLOR.put("G", Color.G);
		STRING_TO_COLOR.put("B", Color.B);
		STRING_TO_COLOR.put("L", Color.L);
		STRING_TO_COLOR.put("D", Color.D);
		STRING_TO_COLOR.put("H", Color.H);
		STRING_TO_COLOR.put("P", Color.P);
		STRING_TO_COLOR.put("J", Color.J);
	}
	
	public static Color getColor(String string) {
		Color color = STRING_TO_COLOR.get(string);
		return color;
	}

}
