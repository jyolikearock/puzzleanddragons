package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class HorusRR implements LeaderSkill {

    @Override
    public double getMultiplier(Set<Match> matches, Color color) {
        if (color.equals(Color.H)) return 1.0;
        Set<Color> colors = new HashSet<Color>();
        for (Match match : matches) {
            Color c = match.getColor();
            if (c.equals(Color.H)) continue;
            if (!colors.contains(c)) colors.add(c);
        }
        int numColors = colors.size();
        return (numColors >= 4) ? 4.0 + 0.5 * (numColors - 4) : 1.0;
    }

    @Override
    public Set<Color> getRequiredColors() {
        return new HashSet<Color>();
    }

}
