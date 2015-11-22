package runnable;

import game.Board;
import game.Color;
import ai.BoardEvaluator;

/**
 * Finds the optimal board set up for a board with 2 colors
 * @author Jiyong
 *
 */
public class BiColorOptimizer {
    
    private static final Color COLOR1 = Color.R;
    private static final Color COLOR2 = Color.L;
    
    private static final int NUM_TOTAL_ORBS = Board.NUM_COLS * Board.NUM_ROWS;
    private static final BoardEvaluator EVALUATOR = new BoardEvaluator();
    
    public static void main(String[] args) {
        for (int i = 0; i <= NUM_TOTAL_ORBS / 2 ; i++) {
            long start = System.currentTimeMillis();
            BoardAndScore boardAndScore = kickOff(i, NUM_TOTAL_ORBS - i);
            long end = System.currentTimeMillis();
            System.out.println(String.format("%d, %d : %.2f | time: %dms", i, NUM_TOTAL_ORBS - i, boardAndScore.getScore(), end - start));
            System.out.println(boardAndScore.getBoard());
        }
    }
    
    private static BoardAndScore kickOff(int numColor1, int numColor2) {
        if (numColor1 + numColor2 != NUM_TOTAL_ORBS) {
            System.out.println(String.format("Total numbers of orbs must equal %d", NUM_TOTAL_ORBS));
            return null;
        } else {
            return findBestBoard(new StringBuilder(), numColor1, numColor2);
        }
    }
    
    private static BoardAndScore findBestBoard(StringBuilder boardText, int numColor1, int numColor2) {
        if (boardText.length() < NUM_TOTAL_ORBS) {
            if (numColor1 == 0) {
                StringBuilder newBoardText = new StringBuilder(boardText.toString());
                while (numColor2 > 0) {
                    newBoardText.append(COLOR2);
                    numColor2--;
                }
                return findBestBoard(newBoardText, 0, 0);
            } else if (numColor2 == 0) {
                StringBuilder newBoardText = new StringBuilder(boardText.toString());
                while (numColor1 > 0) {
                    newBoardText.append(COLOR1);
                    numColor1--;
                }
                return findBestBoard(newBoardText, 0, 0);
            } else {
                BoardAndScore data1 = findBestBoard(new StringBuilder(boardText.toString()).append(COLOR1), numColor1 - 1, numColor2);
                BoardAndScore data2 = findBestBoard(new StringBuilder(boardText.toString()).append(COLOR2), numColor1, numColor2 - 1);
                return (data1.getScore() > data2.getScore()) ? data1 : data2;
            }
        } else {
            Board board = new Board(boardText.toString().split(""));
            double score = EVALUATOR.evaluate(board);
            return new BoardAndScore(board, score);
        }
    }
    
    private static class BoardAndScore {
        private Board board;
        private double score;
        
        public BoardAndScore(Board board, double score) {
            this.board = board;
            this.score = score;
        }

        public Board getBoard() {
            return board;
        }

        public double getScore() {
            return score;
        }
    }
}
