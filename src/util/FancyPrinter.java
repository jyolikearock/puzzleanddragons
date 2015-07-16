package util;

import java.util.List;

import game.Board;
import game.Color;
import game.Move;

public class FancyPrinter {
	
	private static final int NUM_ROWS = Board.NUM_ROWS;
	private static final int NUM_COLS = Board.NUM_COLS;
	
	private static String makePretty(String string, int width) {
		if (string.length() == width) return string;
		else {
			StringBuilder builder = new StringBuilder();
			builder.append(string);
			for (int i = 0; i < width - string.length(); i++) {
				builder.append(" ");
			}
			return builder.toString();
		}
	}
	
	public static String toString(List<Move> moveset) {
		int counter = 0;
		StringBuilder builder = new StringBuilder();
		for (Move move : moveset) {
			counter++;
			builder.append(makePretty(move.toString(), 5));
			if (counter != moveset.size()) builder.append(", ");
		}
		return builder.toString();
	}
	
	public static String toString(Board board) {
		Square[][] squares = new Square[NUM_ROWS][NUM_COLS];
		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0; c < NUM_COLS; c++) {
				Square square = new Square();
				square.setColor(board.get(r, c).getColor());
				squares[r][c] = square;
			}
		}
		return toString(squares);
	}
	
	public static String toString(Board board, List<Move> moveset) {
		int cursorRow = board.getOriginalCursorRow();
		int cursorCol = board.getOriginalCursorCol();
		Square[][] squares = new Square[NUM_ROWS][NUM_COLS];
		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0; c < NUM_COLS; c++) {
				Square square = new Square();
				if (r == cursorRow && c == cursorCol) square.setCursor();
				squares[r][c] = square;
			}
		}
		for (Move move : moveset) {
			squares[cursorRow][cursorCol].addMoveOut(move);
			squares[cursorRow][cursorCol].setColor(board.get(cursorRow, cursorCol).getColor());
			if (move.equals(Move.UP)) cursorRow--;
			else if (move.equals(Move.DOWN)) cursorRow++;
			else if (move.equals(Move.LEFT)) cursorCol--;
			else if (move.equals(Move.RIGHT)) cursorCol++;
			squares[cursorRow][cursorCol].addMoveIn(move);
		}
		return toString(squares);
	}
	
	private static String toString(Square[][] squares) {
		StringBuilder builder = new StringBuilder();
		Square square;
		for (int r = 0; r < NUM_ROWS; r++) {
			for (int innerRow = 0; innerRow < 3; innerRow++) {
				for (int c = 0; c < NUM_COLS; c++) {
					square = squares[r][c];
					builder.append(square.toString(innerRow));
				}
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	
	private static class Square {
		String[][] square;
		
		public Square() {
			square = new String[3][3];
			for (int r = 0; r < 3; r++) {
				for (int c = 0; c < 3; c++) {
					square[r][c] = " ";
				}
			}
			square[0][0] = "/"; square[0][1] = " "; square[0][2] = "\\";
			square[1][0] = "|"; square[1][1] = " "; square[1][2] = "|";
			square[2][0] = "\\"; square[2][1] = "_"; square[2][2] = "/";
		}
		
		public void addMoveOut(Move move) {
			if (move.equals(Move.UP)) square[0][1] = "^";
			else if (move.equals(Move.DOWN)) square[2][1] = "v";
			else if (move.equals(Move.LEFT)) square[1][0] = "<";
			else if (move.equals(Move.RIGHT)) square[1][2] = ">";
		}
		
		public void addMoveIn(Move move) {
			if (move.equals(Move.UP)) square[2][1] = "^";
			else if (move.equals(Move.DOWN)) square[0][1] = "v";
			else if (move.equals(Move.LEFT)) square[1][2] = "<";
			else if (move.equals(Move.RIGHT)) square[1][0] = ">";
		}
		
		public void setColor(Color color) {
			square[1][1] = color.toString();
		}
		
		public void setCursor() {
			square[0][0] = "*";
			square[0][2] = "*";
			square[2][0] = "*";
			square[2][2] = "*";
		}
		
		public String toString (int row) {
			StringBuilder builder = new StringBuilder();
			for (int c = 0; c < 3; c++) {
				builder.append(square[row][c] + "  ");
			}
			return builder.toString();
		}
	}

}
