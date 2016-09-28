package game;

import java.util.HashSet;
import java.util.Set;

public class Match {

    private Color color;
    private Set<Orb> match;

    public Match() {
        match = new HashSet<Orb>();
    }

    public Set<Orb> getMatch() {
        return match;
    }

    public void add(Orb orb) {
        if (color == null) {
            match.add(orb);
            color = orb.getColor();
        }
        else if (color == orb.getColor()) {
            match.add(orb);
        }
        else System.out.println("Orb's color (" + orb.getColor() + ") does not match this Match's color (" + color + ")");
    }

    public void remove(Orb orb) {
        if (match.contains(orb)) remove(orb);
    }

    public int size() {
        return match.size();
    }

    public Color getColor() {
        return color;
    }

    public double getValue() {
        return 1.0 + 0.25 * (match.size() - 3);
    }

    public boolean isRow() {
        if (match.size() < Board.NUM_COLS) return false;
        int[] numOrbsPerRow = new int[Board.NUM_ROWS];
        int row;
        for (Orb orb : match) {
            row = orb.getRow();
            numOrbsPerRow[row]++;
            if (numOrbsPerRow[row] == Board.NUM_COLS) return true;
        }
        return false;
    }

    public boolean isCross() {
        if (match.size() != 5) {
            return false;
        }

        Set<Coordinate> coordinates = new HashSet<Coordinate>();
        for (Orb orb : match) {
            coordinates.add(new Coordinate(orb.getRow(), orb.getCol()));
        }

        for (Coordinate coordinate : coordinates) {
            Coordinate left = new Coordinate(coordinate.getRow(), coordinate.getCol() - 1);
            Coordinate right = new Coordinate(coordinate.getRow(), coordinate.getCol() + 1);
            Coordinate up = new Coordinate(coordinate.getRow() - 1, coordinate.getCol());
            Coordinate down = new Coordinate(coordinate.getRow() + 1, coordinate.getCol());
            if (coordinates.contains(left)
                    && coordinates.contains(right)
                    && coordinates.contains(up)
                    && coordinates.contains(down)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(match.size() + " x " + color);
        if (match.size() >= 6 && isRow())
            builder.append(" (ROW)");
        return builder.toString();
    }

    public String toPrettyString() {
        Board board = new Board();
        for (Orb orb : match) {
            board.put(orb, orb.getRow(), orb.getCol());
        }
        return board.toString();
    }

}
