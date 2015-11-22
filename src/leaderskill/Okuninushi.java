package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class Okuninushi implements LeaderSkill {
    
    private static final double MULTIPLIER = 4.0;
    private static final int NUM_COMBOS_REQUIRED = 6;

    @Override
    public double getMultiplier(Set<Match> matches, Color color) {
        if (color.equals(Color.H)) return 1.0;
        int combos = matches.size();
        return (combos >= NUM_COMBOS_REQUIRED) ? MULTIPLIER : 1.0;
    }

    @Override
    public Set<Color> getRequiredColors() {
        return new HashSet<Color>();
    }

}
