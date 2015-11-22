package ai;

import java.util.List;

import game.Board;
import game.Move;

public class BoardData {
    
    private Board board;
    private double score;
    private List<Move> moveset;
    
    public BoardData(Board board, double score, List<Move> moveset) {
        this.board = board;
        this.score = score;
        this.moveset = moveset;
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

}
