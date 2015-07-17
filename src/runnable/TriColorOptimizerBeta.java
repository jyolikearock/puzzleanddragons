package runnable;

import game.Board;
import game.Color;

import java.util.Random;

import ai.BoardEvaluator;

/**
 * Finds the optimal board set up for a board with 2 colors
 * @author Jiyong
 *
 */
public class TriColorOptimizerBeta {
	
	private static final Color COLOR1 = Color.R;
	private static final Color COLOR2 = Color.D;
	private static final Color COLOR3 = Color.L;
	
	private static final int NUM_TOTAL_ORBS = Board.NUM_COLS * Board.NUM_ROWS;
	private static final BoardEvaluator EVALUATOR = new BoardEvaluator();
	
	private static int bestCombo;
	private static Board bestBoard;
	private static int goalCombo;
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		int num1 = 7;
		int num2 = 10;
		int num3 = 13;
		if (num1 + num2 + num3 == NUM_TOTAL_ORBS) {
			kickOff(num1, num2, num3);
			System.out.println(String.format("%d, %d, %d\t%d combos\t%s", num1, num2, num3, bestCombo, bestBoard.toCode()));
		} else {
			for (int i = 3; i <= NUM_TOTAL_ORBS / 3 ; i++) {
				for (int j = i; j <= (NUM_TOTAL_ORBS - i) / 2; j++) {
					long start = System.currentTimeMillis();
					kickOff(i, j, NUM_TOTAL_ORBS - i - j);
					long end = System.currentTimeMillis();
					System.out.println(String.format("%d, %d, %d\t%d combos\t%s\n\n\n\n\n\n\n", 
							i, j, NUM_TOTAL_ORBS - i - j, 
							bestCombo, 
							bestBoard.toCode()));
//					System.out.println(bestBoard);
				}
			}
		}
	}
	
	private static void kickOff(int numColor1, int numColor2, int numColor3) {
		if (numColor1 + numColor2 + numColor3 != NUM_TOTAL_ORBS) {
			System.out.println(String.format("Total numbers of orbs must equal %d", NUM_TOTAL_ORBS));
		} else {
			bestCombo = -1;
			goalCombo = getReasonableGoal(numColor1, numColor2, numColor3);
			
			StringBuilder randomBoardBuilder = new StringBuilder();
			Random rng = new Random();
			int num1 = numColor1;
			int num2 = numColor2;
			int orbsLeft = numColor1 + numColor2 + numColor3;
			while(orbsLeft > 0) {
				int randomNumber = rng.nextInt(orbsLeft);
				if (randomNumber < num1) {
					randomBoardBuilder.append(COLOR1);
					num1--;
				} else if (randomNumber < num1 + num2) {
					randomBoardBuilder.append(COLOR2);
					num2--;
				} else {
					randomBoardBuilder.append(COLOR3);
				}
				orbsLeft--;
			}
			
			String[] randomBoardString = randomBoardBuilder.toString().split("");
			bestBoard = new Board(randomBoardString);
			bestCombo = EVALUATOR.getNumCombos(bestBoard);
			Board board;
			int combos;
			int iterations = 0;
			while (bestCombo < goalCombo) {
				iterations++;
				if (iterations > 200000) {
					goalCombo--;
					iterations = 0;
//					if (!((numColor1 % 3 == 0 && numColor1 >= 6) &&
//							(numColor2 % 3 == 0 && numColor2 >= 6) &&
//							(numColor3 % 3 == 0 && numColor3 >= 6))) {
//					}
				}
				int index1 = rng.nextInt(NUM_TOTAL_ORBS);
				int index2 = rng.nextInt(NUM_TOTAL_ORBS);
				while (index2 == index1 || randomBoardString[index1].equals(randomBoardString[index2])) {
					index2 = rng.nextInt(NUM_TOTAL_ORBS);
				}
				String temp = randomBoardString[index1];
				randomBoardString[index1] = randomBoardString[index2];
				randomBoardString[index2] = temp;
				board = new Board(randomBoardString);
				combos = EVALUATOR.getNumCombos(board);
				if (combos > bestCombo) {
					bestBoard = board;
					bestCombo = combos;
				}
			}
		}
	}
	
	private static int getReasonableGoal(int num1, int num2, int num3) {
		int total = num1 * num2 * num3;
		return (int) Math.ceil(Math.pow(total, 1.0/3.0));
	}
}
