package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class BastetGL implements LeaderSkill {

	@Override
	public double getMultiplier(Set<Match> matches, Color color) {
		if (color.equals(Color.H)) return 1.0;
		int combos = matches.size();
		if (combos >= 4 && combos <= 7) return (combos + 1) / 2;
		else return 1.0;
	}

	@Override
	public Set<Color> getRequiredColors() {
		return new HashSet<Color>();
	}

}
