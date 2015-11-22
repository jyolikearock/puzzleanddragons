package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class Unconditional10 implements LeaderSkill {

    private static final double MULTIPLIER = 1.0;
    
    @Override
    public double getMultiplier(Set<Match> matches, Color color) {
        if (color.equals(Color.H)) return 1.0;
        return MULTIPLIER;
    }

    @Override
    public Set<Color> getRequiredColors() {
        return new HashSet<Color>();
    }

}
