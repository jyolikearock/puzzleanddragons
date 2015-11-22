package ai;

import game.Board;
import game.Color;
import game.Move;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import util.MovesetExecutor;

public class Agent {
    private Map<String, String> teamInfo;

    private BoardData bestBoardData;
    private long time;

    private Board board;
    private BoardEvaluator boardEvaluator;
    private Queue<BoardData> queue;
    private Set<String> cache;
    private boolean doWork;

    public void solve() {
        board = Configurations.getBoard();
        boardEvaluator = new BoardEvaluator();
        double score = boardEvaluator.evaluate(board);
        bestBoardData = new BoardData(board, score, new ArrayList<Move>());
        this.queue = new LinkedList<BoardData>();
        this.cache = new HashSet<String>();

        int numRows = Board.NUM_ROWS;
        int numCols = Board.NUM_COLS;
        BoardData data;
        Board newBoard;

        // initialize work queue
        List<Color> bestCursorColors = boardEvaluator.predictBestCursorColors(board);
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (bestCursorColors.contains(board.get(r, c).getColor())) {
                    newBoard = new Board(board);
                    newBoard.setCursor(r, c);
                    data = new BoardData(newBoard, -1.0, new ArrayList<Move>());
                    queue.add(data);
                }
            }
        }

        // kick off threads
        this.doWork = true;
        for (int i = 0; i < Configurations.getNumThreads(); i++) {
            Thread worker = new Thread(new WorkConsumer(this));
            worker.start();
        }

        // wait
        try {
            Thread.sleep(Configurations.getTimeLimitMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(0);
        }
        this.doWork = false;

        board.setCursor(
                bestBoardData.getBoard().getOriginalCursorRow(),
                bestBoardData.getBoard().getOriginalCursorCol());
        this.board = MovesetExecutor.execute(board, bestBoardData.getMoveset());

        // only return if leader skills are proc'd
//        if (!boardEvaluator.procsLeaderSkills(board))
//            bestBoardData = new BoardData(Configurations.getBoard(), 0.0, new ArrayList<Move>());
    }

    public synchronized BoardData poll() {
        return queue.poll();
    }

    public synchronized void addWork(BoardData data) {
        queue.add(data);
    }

    public synchronized boolean checkCache(String board) {
        return cache.contains(board);
    }

    public synchronized void addToCache(String board) {
        cache.add(board);
    }

    public Board getBoard() {
        return board;
    }

    public Map<String, String> getTeamInfo() {
        return teamInfo;
    }

    public synchronized BoardData getBestBoardData() {
        return bestBoardData;
    }

    public synchronized void setBestBoardData(BoardData data) {
        bestBoardData = data;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static List<Move> appendMoveset(List<Move> moveset, Move newMove) {
        List<Move> newMoveset = new ArrayList<Move>();
        for (Move move : moveset) {
            newMoveset.add(move);
        }
        newMoveset.add(newMove);
        return newMoveset;
    }

    public synchronized boolean doWork() {
        return doWork;
    }
}
