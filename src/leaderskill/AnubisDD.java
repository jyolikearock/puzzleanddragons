package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class AnubisDD implements LeaderSkill {
    
    private static final double MULTIPLIER = 10.0;

    @Override
    public double getMultiplier(Set<Match> matches, Color color) {
        if (color.equals(Color.H)) return 1.0;
        int combos = matches.size();
        return (combos >= 10 && combos <= 15) ? MULTIPLIER + 1.0 * (combos - 10) : 1.0;
    }

    @Override
    public Set<Color> getRequiredColors() {
        return new HashSet<Color>();
    }

}
