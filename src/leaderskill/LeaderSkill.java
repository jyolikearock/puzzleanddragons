package leaderskill;

import game.Color;
import game.Match;

import java.util.Set;

public interface LeaderSkill {
	
	public double getMultiplier(Set<Match> matches, Color color);
	
	public Set<Color> getRequiredColors();

}
