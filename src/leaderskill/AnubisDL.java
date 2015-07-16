package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class AnubisDL implements LeaderSkill {
	
	private static final double MULTIPLIER = 4.0;

	@Override
	public double getMultiplier(Set<Match> matches, Color color) {
		if (color.equals(Color.H)) return 1.0;
		int combos = matches.size();
		return (combos >= 8 && combos <= 10) ? MULTIPLIER + 3.0 * (combos - 8) : 1.0;
	}

	@Override
	public Set<Color> getRequiredColors() {
		return new HashSet<Color>();
	}
	
}
