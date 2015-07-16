package runnable;

import java.util.List;

import util.FancyPrinter;
import game.Board;
import game.Move;
import ai.Agent;
import ai.BoardData;
import ai.BoardEvaluator;
import ai.Configurations;

@SuppressWarnings("unused")
public class Test {
	
	private static final int ITERATIONS = 10;

	public static void main(String[] args) {
		findOptimalNumCursors();
	}
	
	private static Agent run() {
		Board board = new Board();
		board.fill();
		return run(board);
	}
	
	private static Agent run(Board board) {
		Agent agent = new Agent();
		Configurations.setBoard(board);
		long start = System.currentTimeMillis();
		agent.solve();
		long end = System.currentTimeMillis();
		long time = end - start;
		agent.setTime(time);
		
		return agent;
	}
	
	private static String report(Agent agent) {
		BoardEvaluator evaluator = new BoardEvaluator(new Configurations());
		boolean proc = evaluator.canProcLeaderSkills(agent.getBoard(), 0);
		double score = agent.getBestBoardData().getScore();
		long time = agent.getTime();
		return String.format("LS Proc: %b\t| Score: %.2f\t| Time: %d",
				proc,
				score,
				time);
	}
	
	private static void findOptimalNumCursors() {
		int iterations = 20;
		Board[] boards = new Board[iterations];
		for (int i = 0; i < iterations; i++) {
			Board board = new Board();
			board.fill();
			boards[i] = board;
		}
		
		double score = 0.0;
		long time = 0;
		for (int numRandomCursors = 1; numRandomCursors <= 30; numRandomCursors++) {
			for (int i = 0; i < iterations; i++) {
				Configurations.setNumCursorOrbsToTry(numRandomCursors);
				Agent agent = run(boards[i]);
				score += agent.getBestBoardData().getScore();
				time += agent.getTime();
			}
			score /= iterations;
			time /= iterations;
			System.out.println(String.format("%d cursor orbs sampled\t| "
					+ "Average score: %.2f\t| "
					+ "Average time: %d",
					numRandomCursors,
					score,
					time));
		}
	}
	
	private static void testPruningPerformance() {
		
		Agent agent;
		BoardEvaluator boardEvaluator = new BoardEvaluator(new Configurations());
		int numMoves = 12;
		
		Board board = new Board();
		board.fill(5);
		Configurations.setBoard(board);
		Configurations.setMaxNumMoves(numMoves);
		long start, end, time;
		boolean proc;
		
		System.out.println("With Pruning:");
		Configurations.setPrune(true);
		start = System.currentTimeMillis();
		agent = new Agent();
		agent.solve();
		end = System.currentTimeMillis();
		time = end - start;
		proc = boardEvaluator.canProcLeaderSkills(agent.getBestBoardData().getBoard(), 0);
		System.out.println(String.format("LS Proc: %b\t | Time: %d ms", proc, time));
		
		System.out.println("Without Pruning:");
		Configurations.setPrune(false);
		start = System.currentTimeMillis();
		agent = new Agent();
		agent.solve();
		end = System.currentTimeMillis();
		time = end - start;
		proc = boardEvaluator.canProcLeaderSkills(agent.getBestBoardData().getBoard(), 0);
		System.out.println(String.format("LS Proc: %b\t | Time: %d ms", proc, time));
	}
	
	private static void testCachePerformance() {
		Agent agent;
		Board board;
		BoardData data1, data2;
		double score1, score2;
		long start, end, time1, time2;
		List<Move> moveset1, moveset2;
		System.out.println("Iteration i: 10-move, no-cache (time) vs. 10-move, with-cache (time)");
		for (int i = 0; i < ITERATIONS; i++) {
			board = new Board();
			board.fill(Board.NUM_COLORS);
			Configurations.setBoard(board);
			
			Configurations.setMaxNumMoves(10);
			Configurations.setUseCache(false);
			agent = new Agent();
			start = System.currentTimeMillis();
			agent.solve();
			end = System.currentTimeMillis();
			time1 = end - start;
			data1 = agent.getBestBoardData();
			
			Configurations.setMaxNumMoves(11);
			Configurations.setUseCache(true);
			agent = new Agent();
			start = System.currentTimeMillis();
			agent.solve();
			end = System.currentTimeMillis();
			time2 = end - start;
			data2 = agent.getBestBoardData();
			
			score1 = data1.getScore();
			score2 = data2.getScore();
			System.out.println("Iteration " + i + ": " + score1 + "(" + time1 + " ms)" + " vs. " + score2 + "(" + time2 + " ms)");
		}
	}

}
