package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import util.RandomColorGenerator;
import util.StringToColorConverter;

public class Board {
	
	public static final int NUM_ROWS = 5;
	public static final int NUM_COLS = 6;
	public static final int NUM_COLORS = 6;
	
	private Orb[][] orbs;
	private int currentCursorRow;
	private int currentCursorCol;
	private int originalCursorRow;
	private int originalCursorCol;
	private boolean hasCursor;
	
	public Board() {
		orbs = new Orb[NUM_ROWS][NUM_COLS];
	}
	
	public Board(String filepath) {
		orbs = new Orb[NUM_ROWS][NUM_COLS];
		
		File file = new File(filepath);
		try {
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter("");
			
			int row = 0;
			int col = 0;
			String line;
			String[] lineSplit;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				lineSplit = line.split("");
				for (String character : lineSplit) {
					Color color = StringToColorConverter.getColor(character);
					if (color == null) {
						orbs[row][col] = null;
					} else {
						orbs[row][col] = new Orb(color, row, col);
					}
					col++;
					if (col == NUM_COLS) {
						col = 0;
						row++;
					}					
				}
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Caught an exception while trying to parse board file");
			e.printStackTrace();
		}
	}
	
	public Board(String[] orbsAsStrings) {
		if (orbsAsStrings.length != NUM_ROWS * NUM_COLS) {
			System.out.println("Failed to parse board because length was not " + NUM_ROWS * NUM_COLS);
			return;
		}
		orbs = new Orb[NUM_ROWS][NUM_COLS];
		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0 ; c < NUM_COLS; c++) {
				orbs[r][c] = new Orb(StringToColorConverter.getColor(orbsAsStrings[r * NUM_COLS + c]), r, c);
			}
		}
	}
	
	public Board(Board board) {
		orbs = new Orb[NUM_ROWS][NUM_COLS];
		currentCursorRow = board.getCurrentCursorRow();
		currentCursorCol = board.getCurrentCursorCol();
		originalCursorRow = board.getOriginalCursorRow();
		originalCursorCol = board.getOriginalCursorCol();
		hasCursor = board.hasCursor();
		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0 ; c < NUM_COLS; c++) {
				orbs[r][c] = new Orb(board.get(r, c));
			}
		}
	}
	
	public void fill() {
		fill(NUM_COLORS);
	}
	
	public void fill(int numColors) {
		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0; c < NUM_COLS; c++) {
				orbs[r][c] = new Orb(RandomColorGenerator.getRandomColor(numColors), r, c);
			}
		}
	}
	
	public void clear(Set<Match> matches) {
		for (Match match : matches) {
			for (Orb orb : match.getMatch()) {
				if (orb.getRow() == -1 || orb.getCol() == -1) {
					System.out.println("Failed to remove orb from board\n" + this);
				} else {
					orbs[orb.getRow()][orb.getCol()] = null;
				}
			}
		}
	}
	
	public void fall() {
		Orb[] column;
		int index;
		for (int c = 0; c < NUM_COLS; c++) {
			column = new Orb[NUM_ROWS];
			index = 0;
			for (int r = NUM_ROWS - 1; r >= 0; r--) {
				if (orbs[r][c] != null) {
					column[index] = orbs[r][c];
					index++;
				}
			}
			for (int i = 0; i < NUM_ROWS; i++) {
				orbs[NUM_ROWS - 1 - i][c] = column[i];
				if (column[i] != null) column[i].move(NUM_ROWS - 1 - i, c);
			}
		}
	}
	
	public boolean hasCursor() {
		return hasCursor;
	}
	
	public void setCursor(int row, int col) {
		if (row >= 0 && row < NUM_ROWS && col >= 0 && col < NUM_COLS) {
			currentCursorRow = row;
			currentCursorCol = col;
			originalCursorRow = row;
			originalCursorCol = col;
			hasCursor = true;
			orbs[row][col].setCursor();
		}
	}
	
	private void swap(int row1, int col1, int row2, int col2) {
		Orb orb1 = get(row1, col1);
		Orb orb2 = get(row2, col2);
		orbs[row1][col1] = orb2;
		orbs[row2][col2] = orb1;
		orb1.move(row2, col2);
		orb2.move(row1, col1);
	}
	
	public void move(Move move) {
		int r = currentCursorRow;
		int c = currentCursorCol;
		if (move.equals(Move.UP) && r > 0) {
			swap(r, c, r - 1, c);
			currentCursorRow--;
		} else if (move.equals(Move.DOWN) && r < NUM_ROWS - 1) {
			swap(r, c, r + 1, c);
			currentCursorRow++;
		} else if (move.equals(Move.LEFT) && c > 0) {
			swap(r, c, r, c - 1);
			currentCursorCol--;
		} else if (move.equals(Move.RIGHT) && c < NUM_COLS - 1) {
			swap(r, c, r, c + 1);
			currentCursorCol++;
		} else {
			System.out.println("Failed to execute move " + move + " (" + originalCursorRow + "," + originalCursorCol + ")");
			System.out.println(this);
		}
	}
	
	public Set<Move> getValidMoves(Move lastMove) {
		Set<Move> validMoves = new HashSet<Move>();
		
		int r = currentCursorRow;
		int c = currentCursorCol;
		if (r > 0 && !lastMove.equals(Move.DOWN)) validMoves.add(Move.UP);
		if (r < NUM_ROWS - 1 && !lastMove.equals(Move.UP)) validMoves.add(Move.DOWN);
		if (c > 0 && !lastMove.equals(Move.RIGHT)) validMoves.add(Move.LEFT);
		if (c < NUM_COLS - 1 && !lastMove.equals(Move.LEFT)) validMoves.add(Move.RIGHT);
		return validMoves;
	}
	
	public Set<Move> getValidMoves() {
		Set<Move> validMoves = new HashSet<Move>();
		
		int r = currentCursorRow;
		int c = currentCursorCol;
		if (r > 0) validMoves.add(Move.UP);
		if (r < NUM_ROWS - 1) validMoves.add(Move.DOWN);
		if (c > 0) validMoves.add(Move.LEFT);
		if (c < NUM_COLS - 1) validMoves.add(Move.RIGHT);
		return validMoves;
	}
	
	public Set<Move> getValidMoves(int r, int c) {
		Set<Move> validMoves = new HashSet<Move>();
		if (r > 0) validMoves.add(Move.UP);
		if (r < NUM_ROWS - 1) validMoves.add(Move.DOWN);
		if (c > 0) validMoves.add(Move.LEFT);
		if (c < NUM_COLS - 1) validMoves.add(Move.RIGHT);
		return validMoves;
	}
	
	public Orb get(int row, int col) {
		if (orbs == null) {
			System.out.println("Board hasn't been initialized yet");
			return null;
		} else {
			return orbs[row][col];
		}
	}
	
	public void put(Orb orb, int row, int col) {
		if (row >= 0 && row < NUM_ROWS && col >= 0 && col < NUM_COLS) {
			orbs[row][col] = orb;
		}
		else System.out.println("Failed to place orb " + orb + " at location (" + row + ", " + col + ")");
	}
	
	public int getCurrentCursorRow() {
		return currentCursorRow;
	}
	
	public int getCurrentCursorCol() {
		return currentCursorCol;
	}
	
	public int getOriginalCursorRow() {
		return originalCursorRow;
	}
	
	public int getOriginalCursorCol() {
		return originalCursorCol;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0; c < NUM_COLS; c++) {
				if (orbs[r][c] == null) builder.append("( ) ");
				else builder.append(orbs[r][c] + " ");
			}
			builder.append("\n\n");
		}
		return builder.toString();
	}
	
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0; c < NUM_COLS; c++) {
				if (orbs[r][c] == null) builder.append(" ");
				else builder.append(orbs[r][c].getColor());
			}
		}
		return builder.toString();
	}
	
	public void write(String path) {
		try {
			PrintWriter writer = new PrintWriter(path);
			writer.print(toCode());
			
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String uniqueCode() {
		return toCode() + currentCursorRow + currentCursorCol;
	}

}
