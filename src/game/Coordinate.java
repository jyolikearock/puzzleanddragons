package game;

public class Coordinate {

    int row;
    int col;

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int hashCode() {
        return row * 10 + col;
    }

    public boolean equals(Object o) {
        if (o instanceof Coordinate) {
            Coordinate that = (Coordinate) o;
            return (that.row == this.row && that.col == this.col);
        }
        return false;
    }

    public String toString() {
        return "" + hashCode();
    }

}
