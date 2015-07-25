package runnable;

import java.util.List;

import leaderskill.RaLD;
import util.Constants;
import util.FancyPrinter;
import util.Report;
import game.Board;
import game.Color;
import game.Move;
import ai.Agent;
import ai.BoardData;
import ai.BoardEvaluator;
import ai.Configurations;

@SuppressWarnings("unused")
public class Test {
	
	private static final int ITERATIONS = 5;

	public static void main(String[] args) {
		testFindThreeClosestOrbs();
	}
	
	private static void tryRa() {
		Configurations.setLeaderSkill(1, new RaLD());
		Configurations.setLeaderSkill(2, new RaLD());
		System.out.println(report(run(5)));
	}
	
	private static void testFindThreeClosestOrbs() {
		Configurations.setLeaderSkill(1, new RaLD());
		Configurations.setLeaderSkill(2, new RaLD());
		BoardEvaluator evaluator = new BoardEvaluator();
		Board board = new Board();
		board.fill();

		System.out.println(board);
		System.out.println(board.toCode());
		System.out.println(evaluator.predictBestCursorColors(board));
		for (Color color : Constants.COLORS) {
			System.out.println(String.format(
					"%s : %d",
					color,
					evaluator.findThreeClosestOrbs(board, color)));
		}
	}
	
	private static void findOptimalNumCursors() {
		Board[] boards = new Board[ITERATIONS];
		for (int i = 0; i < ITERATIONS; i++) {
			Board board = new Board();
			board.fill();
			boards[i] = board;
		}
		
		double score = 0.0;
		long time = 0;
		for (int numRandomCursors = 1; numRandomCursors <= 30; numRandomCursors++) {
			Report report = new Report();
			for (int i = 0; i < ITERATIONS; i++) {
				Configurations.setNumCursorOrbsToTry(numRandomCursors);
				Agent agent = run(boards[i]);
				report.aggregate(agent);
			}
			System.out.println(String.format("Cursor Orbs: %d\t| Score: %.2f\t| Moves: %.2f\t| Time: %.2f",
					numRandomCursors,
					report.getScore(),
					report.getNumMoves(),
					report.getTime()));
		}
	}
	
	private static void testThreadPerformance() {
		int minThreads = 10;
		int maxThreads = 20;
		Report[] reports = new Report[maxThreads];
		for (int i = 0; i < ITERATIONS; i++) {
			Board board = new Board();
			board.fill();
			for (int n = minThreads; n <= maxThreads; n++) {
				Configurations.setNumThreads(n);
				Agent agent = run(board);
				if (reports[n-1] == null)
					reports[n-1] = new Report();
				
				reports[n-1].aggregate(agent);
			}
			System.out.println(".");
		}
		
		for (int n = minThreads; n <= maxThreads; n++) {
			System.out.println(String.format("Threads: %d\t| Score: %.2f\t| Moves: %.2f\t| Time: %.2f",
					n,
					reports[n-1].getScore(),
					reports[n-1].getNumMoves(),
					reports[n-1].getTime()));
		}
	}
	
	private static Agent run() {
		Board board = new Board();
		board.fill();
		return run(board);
	}
	
	private static Agent run(int i) {
		Board board = new Board();
		board.fill(i);
		return run(board);
	}
	
	private static Agent run(Board board) {
		Configurations.setBoard(board);
		Agent agent = new Agent();
		long start = System.currentTimeMillis();
		agent.solve();
		long end = System.currentTimeMillis();
		long time = end - start;
		agent.setTime(time);
		
		return agent;
	}
	
	private static String report(Agent agent) {
		BoardEvaluator evaluator = new BoardEvaluator();
		Board originalBoard = Configurations.getBoard();
		originalBoard.setCursor(
				agent.getBestBoardData().getBoard().getOriginalCursorRow(),
				agent.getBestBoardData().getBoard().getOriginalCursorCol());
		Board board = agent.getBoard();
		boolean proc = evaluator.procsLeaderSkills(board);
		double score = agent.getBestBoardData().getScore();
		int numMoves = agent.getBestBoardData().getMoveset().size();
		long time = agent.getTime();
		List<Move> moveset = agent.getBestBoardData().getMoveset();
		return String.format("LS Proc: %b\t| Score: %.2f\t| Moves: %d\t| Time: %d"
				+ "\n%s"
				+ "\n"
				+ "\n%s"
				+ "\n%s"
				+ "\n%s",
				proc,
				score,
				numMoves,
				time,
				moveset,
				originalBoard.toCode(),
				originalBoard,
				board.toCode());
	}

}
