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
		System.out.println(report(run(5)));
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
		Board board = agent.getBoard();
		boolean proc = evaluator.procsLeaderSkills(board);
		double score = agent.getBestBoardData().getScore();
		int numMoves = agent.getBestBoardData().getMoveset().size();
		long time = agent.getTime();
		List<Move> moveset = agent.getBestBoardData().getMoveset();
		return String.format("LS Proc: %b\t| Score: %.2f\t| Moves: %d\t| Time: %d"
				+ "\n%s"
				+ "\nBoard:"
				+ "\n%s"
				+ "\n%s",
				proc,
				score,
				numMoves,
				time,
				moveset,
				board.toCode(),
				board);
	}
	
	private static void findOptimalNumCursors() {
		int iterations = 10;
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

}
