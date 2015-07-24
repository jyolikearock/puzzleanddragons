package util;

import game.Color;

import java.util.LinkedHashSet;
import java.util.Set;

public class Constants {
	
	public static final Set<Color> COLORS = new LinkedHashSet<Color>();
	
	static {
		COLORS.add(Color.R);
		COLORS.add(Color.G);
		COLORS.add(Color.B);
		COLORS.add(Color.L);
		COLORS.add(Color.D);
	}

}
