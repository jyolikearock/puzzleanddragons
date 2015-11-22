package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class RaLL implements LeaderSkill {

    @Override
    public double getMultiplier(Set<Match> matches, Color color) {
        if (color.equals(Color.H)) return 1.0;
        Set<Color> colors = new HashSet<Color>();
        for (Match match : matches) {
            Color c = match.getColor();
            if (!colors.contains(c)) colors.add(c);
        }
        int numColors = colors.size();
        if (numColors == 5) return 4.0;
        if (numColors == 6) return 7.0;
        return 1.0;
    }

    @Override
    public Set<Color> getRequiredColors() {
        return new HashSet<Color>();
    }

}
