package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MatchFinder {
    
    private Map<Orb, Match> orbsToMatches;
    private Set<Match> matches;
    
    public Set<Match> findMatches(Board board) {
        
        int numRows = Board.NUM_ROWS;
        int numCols = Board.NUM_COLS;
        
        orbsToMatches = new HashMap<Orb, Match>();
        matches = new HashSet<Match>();
        
        Orb orb;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0 ; c < numCols; c++) {
                orb = board.get(r, c);
                if (orb == null) continue;
                if (orbsToMatches.keySet().contains(orb)) continue;
                
                Orb n1;
                Orb n2;
                if (r > 1) {
                    n1 = board.get(r - 1, c);
                    n2 = board.get(r - 2, c);
                    if (n1 != null && n2 != null && doColorsMatch(orb, n1, n2)) {
                        updateMatches(orb, n1, n2);
                    }
                }
                if (r < numRows - 2) {
                    n1 = board.get(r + 1, c);
                    n2 = board.get(r + 2, c);
                    if (n1 != null && n2 != null && doColorsMatch(orb, n1, n2)) {
                        updateMatches(orb, n1, n2);
                    }
                }
                if (r > 0 && r < numRows - 1) {
                    n1 = board.get(r - 1, c);
                    n2 = board.get(r + 1, c);
                    if (n1 != null && n2 != null && doColorsMatch(orb, n1, n2)) {
                        updateMatches(orb, n1, n2);
                    }
                }
                if (c > 1) {
                    n1 = board.get(r, c - 1);
                    n2 = board.get(r, c - 2);
                    if (n1 != null && n2 != null && doColorsMatch(orb, n1, n2)) {
                        updateMatches(orb, n1, n2);
                    }
                }
                if (c < numCols - 2) {
                    n1 = board.get(r, c + 1);
                    n2 = board.get(r, c + 2);
                    if (n1 != null && n2 != null && doColorsMatch(orb, n1, n2)) {
                        updateMatches(orb, n1, n2);
                    }
                }
                if (c > 0 && c < numCols - 1) {
                    n1 = board.get(r, c - 1);
                    n2 = board.get(r, c + 1);
                    if (n1 != null && n2 != null && doColorsMatch(orb, n1, n2)) {
                        updateMatches(orb, n1, n2);
                    }
                }
            }
        }
        
        for (int r = 0; r < numRows; r++) {
            for (int c = 0 ; c < numCols; c++) {
                orb = board.get(r,c);
                Orb neighbor;
                Match match1;
                Match match2;
                if (r < numRows - 1) {
                    neighbor = board.get(r + 1, c);
                    if (orbsToMatches.containsKey(orb) && orbsToMatches.containsKey(neighbor)
                            && orb.getColor().equals(neighbor.getColor())) {
                        match1 = orbsToMatches.get(orb);
                        match2 = orbsToMatches.get(neighbor);
                        mergeMatches(match1, match2);
                    }
                }
                if (c < numCols - 1) {
                    neighbor = board.get(r, c + 1);
                    if (orbsToMatches.containsKey(orb) && orbsToMatches.containsKey(neighbor)
                            && orb.getColor().equals(neighbor.getColor())) {
                        match1 = orbsToMatches.get(orb);
                        match2 = orbsToMatches.get(neighbor);
                        mergeMatches(match1, match2);
                    }
                }
            }
        }
        return matches;
    }
    
    private boolean doColorsMatch(Orb orb1, Orb orb2, Orb orb3) {
        return (orb1.getColor().equals(orb2.getColor())
                && orb2.getColor().equals(orb3.getColor()));
    }
    
    private void updateMatches(Orb orb1, Orb orb2, Orb orb3) {
        List<Orb> inMatch = new ArrayList<Orb>();
        List<Orb> notInMatch = new ArrayList<Orb>();
        if (orbsToMatches.keySet().contains(orb1)) {
            inMatch.add(orb1);
        } else {
            notInMatch.add(orb1);
        }
        if (orbsToMatches.keySet().contains(orb2)) {
            inMatch.add(orb2);
        } else {
            notInMatch.add(orb2);
        }
        if (orbsToMatches.keySet().contains(orb3)) {
            inMatch.add(orb3);
        } else {
            notInMatch.add(orb3);
        }
        
        Match match;
        if (inMatch.size() == 0) {
            match = new Match();
            matches.add(match);
        } else if (inMatch.size() == 1) {
            match = orbsToMatches.get(inMatch.get(0));
        } else { // if (inMatch.size() == 2)
            Match match1 = orbsToMatches.get(inMatch.get(0));
            Match match2 = orbsToMatches.get(inMatch.get(1));
            mergeMatches(match1, match2);
            match = match1;
        }
        for (Orb orb : notInMatch) {
            addToMatch(orb, match);
        }
    }
    
    private void addToMatch(Orb orb, Match match) {
        match.add(orb);
        orbsToMatches.put(orb, match);
    }
    
    private void mergeMatches(Match match1, Match match2) {
        if (match1.equals(match2)) return;
        for (Orb orb : match2.getMatch()) {
            addToMatch(orb, match1);
        }
        matches.remove(match2);
    }

}
