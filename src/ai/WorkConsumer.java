package ai;

import game.Board;
import game.Move;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class WorkConsumer implements Runnable {
	
	Agent agent;
	private int workerId1;
	private int workerId2;
	private BoardEvaluator boardEvaluator;
	private Queue<BoardData> queue;
	private Set<String> cache;
	
	private int maxNumMoves;
	private boolean useCache;
	
	public WorkConsumer(Agent agent, BoardData data, int workerId1, int workerId2) {
		this.agent = agent;
		this.workerId1 = workerId1;
		this.workerId2 = workerId2;
		this.maxNumMoves = Configurations.getMaxNumMoves();
		this.useCache = Configurations.useCache();
		this.boardEvaluator = new BoardEvaluator(new Configurations());
		this.queue = new LinkedList<BoardData>();
		this.cache = new HashSet<String>();
		queue.add(data);
	}

	@Override
	public void run() {
		BoardData bestBoardData;
		BoardData data;
		Board board;
		List<Move> moveset;
		Set<Move> validMoves;
		double score;
		while (!queue.isEmpty()) {
			data = queue.poll();
			board = data.getBoard();
			
			// skip if this board has already been explored
			// otherwise, add it to the cache and continue
			if (useCache) {
				if (cache.contains(board.uniqueCode())) {
					continue;
				}
				else cache.add(board.uniqueCode());
			}
			
			moveset = data.getMoveset();
			
			// skip if the leader skill definitely cannot be proc'd with the remaining number of moves
			if (Configurations.prune()) {
				if (!boardEvaluator.canProcLeaderSkills(board, maxNumMoves - moveset.size())) {
					continue;
				}
			}
			
			score = boardEvaluator.evaluate(board);
			bestBoardData = agent.getBestBoardData();
			if (score > bestBoardData.getScore() ||
					(score == bestBoardData.getScore() && moveset.size() < bestBoardData.getMoveset().size())) {
				agent.setBestBoardData(new BoardData(board, score, moveset));
			}
			if (moveset.size() < maxNumMoves) {
				if (moveset.isEmpty()) validMoves = board.getValidMoves();
				else validMoves = board.getValidMoves(moveset.get(moveset.size() - 1));
				Board newBoard;
				for (Move move : validMoves) {
					newBoard = new Board(board);
					newBoard.move(move);
					queue.add(new BoardData(newBoard, -1.0, Agent.appendMoveset(moveset, move)));
				}
			}			
		}
		agent.reportStatus(workerId1, workerId2);
	}

}
