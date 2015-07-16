package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class NoLeaderSkill implements LeaderSkill {

	double multiplier;
	
	public NoLeaderSkill(double multiplier) {
		this.multiplier = multiplier;
	}
	
	public NoLeaderSkill() {
		this(1.0);
	}
	
	@Override
	public double getMultiplier(Set<Match> matches, Color color) {
		return multiplier;
	}

	@Override
	public Set<Color> getRequiredColors() {
		return new HashSet<Color>();
	}

}
