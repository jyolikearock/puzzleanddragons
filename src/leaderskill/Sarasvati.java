package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class Sarasvati implements LeaderSkill {

    private static final double MULTIPLIER = 5.0;
    
    @Override
    public double getMultiplier(Set<Match> matches, Color color) {
        if (color.equals(Color.H)) return 1.0;
        int numBlueMatches = 0;
        for (Match match : matches) {
            Color c = match.getColor();
            if (c.equals(Color.B)) numBlueMatches++;
        }
        return (numBlueMatches >= 3) ? MULTIPLIER : 1.0;
    }

    @Override
    public Set<Color> getRequiredColors() {
        Set<Color> requiredColors = new HashSet<Color>();
        requiredColors.add(Color.B);
        return requiredColors;
    }

}
