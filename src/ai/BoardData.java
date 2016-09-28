package ai;

import game.Board;
import game.Move;

import java.util.List;

public class BoardData {

    private Board board;
    private double score;
    private List<Move> moveset;
    private int entropyTrend;

    public BoardData(Board board, double score, List<Move> moveset) {
        this(board, score, moveset, 0);
    }

    public BoardData(Board board, double score, List<Move> moveset, int entropyTrend) {
        this.board = board;
        this.score = score;
        this.moveset = moveset;
        this.entropyTrend = entropyTrend;
    }

    public Board getBoard() {
        return board;
    }

    public double getScore() {
        return score;
    }

    public List<Move> getMoveset() {
        return moveset;
    }

    public int getEntropyTrend() {
        return entropyTrend;
    }

}
