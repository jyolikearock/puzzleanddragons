package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class Sakuya implements LeaderSkill {
    
    private static final double MULTIPLIER = 5;
    private static final Set<Color> COLORS_TO_MATCH = new HashSet<Color>();
    private static final int NUM_COLORS_TO_MATCH = 4;
    
    static {
        COLORS_TO_MATCH.add(Color.R);
        COLORS_TO_MATCH.add(Color.G);
        COLORS_TO_MATCH.add(Color.B);
        COLORS_TO_MATCH.add(Color.L);
    }
    
    @Override
    public double getMultiplier(Set<Match> matches, Color color) {
        if (color.equals(Color.H)) return 1.0;
        Set<Color> colorsMatched = new HashSet<Color>();
        for (Match match : matches) {
            Color c = match.getColor();
            if (COLORS_TO_MATCH.contains(c)) {
                colorsMatched.add(c);
            }
        }
        return (colorsMatched.size() >= NUM_COLORS_TO_MATCH) ? MULTIPLIER : 1.0;
    }

    @Override
    public Set<Color> getRequiredColors() {
        return COLORS_TO_MATCH;
    }

}
