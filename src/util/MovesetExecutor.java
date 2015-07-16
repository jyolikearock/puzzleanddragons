package util;

import java.util.List;
import java.util.Set;

import game.Board;
import game.Match;
import game.MatchFinder;
import game.Move;

public class MovesetExecutor {
	
	private static MatchFinder matchFinder;
	
	public static Board execute(Board board, List<Move> moveset, StringBuilder builder, boolean showMatches, boolean showBoard) {
		matchFinder = new MatchFinder();
		Board boardCopy = new Board(board);
		for (Move move : moveset) {
			boardCopy.move(move);
		}
		Board returnBoard = new Board(boardCopy);
		
		Set<Match> matches;
		while ((matches = matchFinder.findMatches(boardCopy)).size() != 0) {
			for (Match match : matches) {
				StringBuilder myBuilder = new StringBuilder();
				myBuilder.append(match.size());
				myBuilder.append(" x ");
				myBuilder.append(match.getColor());
				if (match.isRow()) myBuilder.append(" (RE)");
				if (showMatches) builder.append(myBuilder.toString() + "\n");
			}
			boardCopy.clear(matches);
			if (showBoard) builder.append("\n" + boardCopy);
			boardCopy.fall();
		}
		
		return returnBoard;
	}
	
	public static Board execute(Board board, List<Move> moveset, StringBuilder builder, boolean showMatches) {
		return execute(board, moveset, builder, showMatches, false);
	}
	
	public static Board execute(Board board, List<Move> moveset, StringBuilder builder) {
		return execute(board, moveset, builder, true, false);
	}
	
	public static Board execute(Board board, List<Move> moveset) {
		return execute(board, moveset, null, false, false);
	}

}
