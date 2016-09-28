package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class Myr implements LeaderSkill {

    private static final double MULTIPLIER = 7.0;

    @Override
    public double getMultiplier(Set<Match> matches, Color color) {
        if (color.equals(Color.H)) {
            return 1.0;
        }

        for (Match match : matches) {
            if (match.isCross() && match.getColor().equals(Color.H)) {
                return MULTIPLIER;
            }
        }
        return 1.0;
    }

    @Override
    public Set<Color> getRequiredColors() {
        return new HashSet<Color>();
    }

}
