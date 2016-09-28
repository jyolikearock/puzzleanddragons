package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class Tsubaki implements LeaderSkill {

    private static final double MULTIPLIER1 = 3.5;
    private static final double MULTIPLIER2 = 2.5;
    private static final Set<Color> COLORS_TO_MATCH = new HashSet<Color>();
    private static final int NUM_COLORS_TO_MATCH = 2;

    static {
        COLORS_TO_MATCH.add(Color.R);
        COLORS_TO_MATCH.add(Color.L);
    }

    @Override
    public double getMultiplier(Set<Match> matches, Color color) {
        if (color.equals(Color.H)) return 1.0;

        boolean procMultiplier1 = false;
        boolean procMultiplier2 = false;
        Set<Color> colorsMatched = new HashSet<Color>();
        for (Match match : matches) {
            Color c = match.getColor();
            if (COLORS_TO_MATCH.contains(c))
                colorsMatched.add(c);
            if (c.equals(Color.R) && match.size() == 5)
                procMultiplier2 = true;
        }

        if (colorsMatched.size() >= NUM_COLORS_TO_MATCH)
            procMultiplier1 = true;

        if (procMultiplier1 && procMultiplier2)
            return MULTIPLIER1 * MULTIPLIER2;
        else if (procMultiplier1)
            return MULTIPLIER1;
        else if (procMultiplier2)
            return MULTIPLIER2;
        else
            return 1.0;
    }

    @Override
    public Set<Color> getRequiredColors() {
        return COLORS_TO_MATCH;
    }

}
