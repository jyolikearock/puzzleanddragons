package ai;

import game.Board;
import game.Move;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import util.MovesetExecutor;

public class Agent {
	private Map<String, String> teamInfo;
	
	private BoardData bestBoardData;
	private long time;

	private Configurations configurations;
	private Board board;
	private int maxNumMoves;
	private BoardEvaluator boardEvaluator;
	private Queue<BoardData> queue;
	private Set<String> cache;
	
	public Agent() {
		this.configurations = new Configurations();
	}
	
	public void solve() {
		board = configurations.getBoard();
		boardEvaluator = new BoardEvaluator(configurations);
		double score = boardEvaluator.evaluate(board);
		bestBoardData = new BoardData(board, score, new ArrayList<Move>());
		this.queue = new LinkedList<BoardData>();
		this.cache = new HashSet<String>();
		
		int numRows = Board.NUM_ROWS;
		int numCols = Board.NUM_COLS;
		BoardData data;
		Board newBoard;
		
		// initialize work queue
		int numCursorOrbsToTry = Configurations.getNumCursorOrbsToTry();
		if (numCursorOrbsToTry == numRows * numCols) {
			for (int r = 0; r < numRows; r++) {
				for (int c = 0; c < numCols; c++) {
					newBoard = new Board(board);
					newBoard.setCursor(r, c);
					data = new BoardData(newBoard, -1.0, new ArrayList<Move>());
					queue.add(data);
				}
			}
		} else {
			List<Point> randomCoordinates = getRandomCoordinates(numCursorOrbsToTry);
			for (Point p : randomCoordinates) {
				newBoard = new Board(board);
				newBoard.setCursor(p.x, p.y);
				data = new BoardData(newBoard, -1.0, new ArrayList<Move>());
				queue.add(data);
			}
		}
		
		// kick off threads
		for (int i = 0; i < Configurations.NUM_THREADS; i++) {
			Thread worker = new Thread(new WorkConsumer(this));
			worker.start();
		}
		
		// wait 5 seconds
		try {
			Thread.sleep(5 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		board.setCursor(
				bestBoardData.getBoard().getOriginalCursorRow(), 
				bestBoardData.getBoard().getOriginalCursorCol());
		this.board = MovesetExecutor.execute(board, bestBoardData.getMoveset());
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
	
	public int getMaxNumMoves() {
		return maxNumMoves;
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
	
	private List<Point> getRandomCoordinates(int numCoordinates) {
		List<Point> randomCoordinates = new ArrayList<Point>();
		
		for (int r = 0; r < Board.NUM_ROWS; r++) {
			for (int c = 0; c < Board.NUM_COLS; c++) {
				randomCoordinates.add(new Point(r, c));
			}
		}
		
		Random rng = new Random();
		while (randomCoordinates.size() > numCoordinates) {
			int removeIndex = rng.nextInt(randomCoordinates.size());
			randomCoordinates.remove(removeIndex);
		}
		
		return randomCoordinates;
	}
	
	public static List<Move> appendMoveset(List<Move> moveset, Move newMove) {
		List<Move> newMoveset = new ArrayList<Move>();
		for (Move move : moveset) {
			newMoveset.add(move);
		}
		newMoveset.add(newMove);
		return newMoveset;
	}
}
