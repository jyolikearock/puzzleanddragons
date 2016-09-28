package ai;

import game.Board;
import game.Move;

import java.util.List;
import java.util.Set;

public class WorkConsumer implements Runnable {

    Agent agent;
    private BoardEvaluator boardEvaluator;

    public WorkConsumer(Agent agent) {
        this.agent = agent;
        this.boardEvaluator = new BoardEvaluator();
    }

    @Override
    public void run() {
        BoardData bestBoardData;
        BoardData data;
        Board board;
        List<Move> moveset;
        Set<Move> validMoves;
        double score;
        int entropyTrend;

        while (agent.doWork()) {
            data = agent.poll();
            if (data == null)
                continue;
            board = data.getBoard();

            // if the board's entropy has been increasing, discard it
            entropyTrend = data.getEntropyTrend();
            if (boardEvaluator.calculateEntropy(board) > 1.9)
                entropyTrend++;
            else
                entropyTrend = 0;
            if (entropyTrend > 3)
                continue;

            // compare this board's score to the best one so far
            moveset = data.getMoveset();
            if (moveset.size() >= Configurations.MEANINGFUL_NUMBER_OF_MOVES) {
                score = boardEvaluator.evaluate(board);
                bestBoardData = agent.getBestBoardData();
                if (score > bestBoardData.getScore() ||
                        (score == bestBoardData.getScore() && moveset.size() < bestBoardData.getMoveset().size())) {
                    agent.setBestBoardData(new BoardData(board, score, moveset));
                }
            }

            // add work to the queue with potential board states
            if (moveset.isEmpty())
                validMoves = board.getValidMoves();
            else
                validMoves = board.getValidMoves(moveset.get(moveset.size() - 1));
            Board newBoard;
            for (Move move : validMoves) {
                newBoard = new Board(board);
                newBoard.move(move);
                agent.addWork(new BoardData(newBoard, -1.0, Agent.appendMoveset(moveset, move), entropyTrend));
            }
        }
    }

}
