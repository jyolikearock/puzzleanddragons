package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class Vishnu implements LeaderSkill {

	private static final double MULTIPLIER = 2.5;
	
	@Override
	public double getMultiplier(Set<Match> matches, Color color) {
		if (color.equals(Color.H)) return 1.0;
		int numGreenMatches = 0;
		for (Match match : matches) {
			Color c = match.getColor();
			if (c.equals(Color.G)) numGreenMatches++;
		}
		if (numGreenMatches == 1) return MULTIPLIER;
		if (numGreenMatches >= 2) return MULTIPLIER + 1.0;
		else return 1.0;
	}

	@Override
	public Set<Color> getRequiredColors() {
		Set<Color> requiredColors = new HashSet<Color>();
		requiredColors.add(Color.G);
		return requiredColors;
	}

}
