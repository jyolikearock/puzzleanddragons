package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class Perseus implements LeaderSkill {

    private static final double MULTIPLIER = 3.0;
    
    @Override
    public double getMultiplier(Set<Match> matches, Color color) {
        if (color.equals(Color.H)) return 1.0;
        int biggestMatch = 0;
        for (Match match : matches) {
            Color c = match.getColor();
            if (c.equals(Color.G) && match.size() > biggestMatch)
                biggestMatch = match.size();
        }
        if (biggestMatch >= 6 && biggestMatch <= 8) return MULTIPLIER + 0.5 * (biggestMatch - 6);
        else return 1.0;
    }

    @Override
    public Set<Color> getRequiredColors() {
        Set<Color> requiredColors = new HashSet<Color>();
        requiredColors.add(Color.G);
        return requiredColors;
    }

}
