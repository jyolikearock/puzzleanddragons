package leaderskill;

import game.Color;
import game.Match;

import java.util.HashSet;
import java.util.Set;

public class IsisBL implements LeaderSkill {

    @Override
    public double getMultiplier(Set<Match> matches, Color color) {
        if (color.equals(Color.H)) return 1.0;
        Set<Color> colors = new HashSet<Color>();
        for (Match match : matches) {
            Color c = match.getColor();
            if (!colors.contains(c)) colors.add(c);
        }
        int numColors = colors.size();
        return (numColors >= 3) ? 3.0 + 0.5 * (numColors - 3) : 1.0;
    }

    @Override
    public Set<Color> getRequiredColors() {
        return new HashSet<Color>();
    }

}
