package ai;

import game.Board;
import game.Move;

import java.util.List;
import java.util.Set;

public class WorkConsumer implements Runnable {
	
	Agent agent;
	private BoardEvaluator boardEvaluator;
	private boolean useCache;
	
	public WorkConsumer(Agent agent) {
		this.agent = agent;
		this.boardEvaluator = new BoardEvaluator(new Configurations());
		this.useCache = Configurations.useCache();
	}

	@Override
	public void run() {
		BoardData bestBoardData;
		BoardData data;
		Board board;
		List<Move> moveset;
		Set<Move> validMoves;
		double score;
		
		while (true) {
			data = agent.poll();
			board = data.getBoard();
			
			// skip if this board has already been explored
			// otherwise, add it to the cache and continue
			if (useCache) {
				if (agent.checkCache(board.uniqueCode())) {
					continue;
				}
				else agent.addToCache(board.uniqueCode());
			}
			
			// compare this board's score to the best one so far
			moveset = data.getMoveset();
			score = boardEvaluator.evaluate(board);
			bestBoardData = agent.getBestBoardData();
			if (score > bestBoardData.getScore() ||
					(score == bestBoardData.getScore() && moveset.size() < bestBoardData.getMoveset().size())) {
				agent.setBestBoardData(new BoardData(board, score, moveset));
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
				agent.addWork(new BoardData(newBoard, -1.0, Agent.appendMoveset(moveset, move)));
			}
		}
	}

}
