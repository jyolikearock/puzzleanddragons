package runnable;

import game.Board;
import game.Color;

import java.util.LinkedList;
import java.util.Queue;

import ai.BoardEvaluator;
import ai.Configurations;

/**
 * Finds the optimal board set up for a board with 2 colors
 * @author Jiyong
 *
 */
public class TriColorOptimizerAlpha {
	
	private static final Color COLOR1 = Color.R;
	private static final Color COLOR2 = Color.L;
	private static final Color COLOR3 = Color.D;
	
	private static final int NUM_TOTAL_ORBS = Board.NUM_COLS * Board.NUM_ROWS;
	private static final Configurations CONFIGURATIONS = new Configurations();
	private static final BoardEvaluator EVALUATOR = new BoardEvaluator(CONFIGURATIONS);
	
	private static Queue<WorkUnit> workQueue;
	private static int bestCombo;
	private static Board bestBoard;
	private static int goalCombo;
	
	public static void main(String[] args) {
		for (int i = 3; i <= NUM_TOTAL_ORBS / 3 ; i++) {
			for (int j = i; j <= (NUM_TOTAL_ORBS - i) / 2; j++) {
				long start = System.currentTimeMillis();
				//kickOff(i, j, NUM_TOTAL_ORBS - i - j);
				kickOff(10, 10, 10);
				long end = System.currentTimeMillis();
				System.out.println(String.format("%d, %d, %d : %d combos | time: %dms", 
						i, 
						j, 
						NUM_TOTAL_ORBS - i - j,
						bestCombo,
						end - start));
				System.out.println(bestBoard);
			}
		}
	}
	
	private static void kickOff(int numColor1, int numColor2, int numColor3) {
		if (numColor1 + numColor2 + numColor3 != NUM_TOTAL_ORBS) {
			System.out.println(String.format("Total numbers of orbs must equal %d", NUM_TOTAL_ORBS));
		} else {
			workQueue = new LinkedList<WorkUnit>();
			bestCombo = -1;
			goalCombo = getReasonableGoal(numColor1, numColor2, numColor3);
			System.out.println(String.format("Goal is set to %d combos", goalCombo));
			
			WorkUnit workUnit = new WorkUnit(new StringBuilder(), numColor1, numColor2, numColor3);
			workQueue.add(workUnit);
			Board board;
			int numCombos;
			int iteration = 0;
			int lastPrint = 0;
			while (bestCombo < goalCombo && !workQueue.isEmpty()) {
				iteration++;
				if (iteration >= lastPrint + 1000000) {
					lastPrint = iteration;
					System.out.println(String.format("Iteration %d; queue size: %d", iteration, workQueue.size()));
				}
				workUnit = workQueue.poll();
				if (workUnit.isBuilt()) {
					board = new Board(workUnit.getBoardBuilder().toString().split(""));
					numCombos = EVALUATOR.getNumCombos(board);
					if (numCombos > bestCombo) {
						bestBoard = board;
						bestCombo = numCombos;
					}
					if (numCombos >= goalCombo)
						break;
				} else {
					requeueWorkUnit(workUnit.getBoardBuilder(), workUnit.getNum1(), workUnit.getNum2(), workUnit.getNum3());
				}
			}
		}
	}
	
	private static int getReasonableGoal(int num1, int num2, int num3) {
		int goal = 9;
		int total = num1 * num2 * num3;
		goal = Math.min(goal, (int) Math.floor(Math.pow(total, 1.0/3.0)) - 1);
		
		return goal;
	}
	
	private static void requeueWorkUnit(StringBuilder boardBuilder, int num1, int num2, int num3) {
		if (num1 == 0 && num2 == 0) {
			StringBuilder newBoardBuilder = new StringBuilder(boardBuilder.toString());
			while (num3 > 0) {
				newBoardBuilder.append(COLOR3);
				num3--;
			}
			workQueue.add(new WorkUnit(newBoardBuilder, 0, 0, 0));
		} else if (num2 == 0 && num3 == 0) {
			StringBuilder newBoardBuilder = new StringBuilder(boardBuilder.toString());
			while (num1 > 0) {
				newBoardBuilder.append(COLOR1);
				num1--;
			}
			workQueue.add(new WorkUnit(newBoardBuilder, 0, 0, 0));
		} else if (num1 == 0 && num3 == 0) {
			StringBuilder newBoardBuilder = new StringBuilder(boardBuilder.toString());
			while (num2 > 0) {
				newBoardBuilder.append(COLOR2);
				num2--;
			}
			workQueue.add(new WorkUnit(newBoardBuilder, 0, 0, 0));
		} else if (num1 == 0) {
			workQueue.add(new WorkUnit(new StringBuilder(boardBuilder.toString()).append(COLOR2), num1, num2 - 1, num3));
			workQueue.add(new WorkUnit(new StringBuilder(boardBuilder.toString()).append(COLOR3), num1, num2, num3 - 1));
		} else if (num2 == 0) {
			workQueue.add(new WorkUnit(new StringBuilder(boardBuilder.toString()).append(COLOR1), num1 - 1, num2, num3));
			workQueue.add(new WorkUnit(new StringBuilder(boardBuilder.toString()).append(COLOR3), num1, num2, num3 - 1));
		} else if (num3 == 0) {
			workQueue.add(new WorkUnit(new StringBuilder(boardBuilder.toString()).append(COLOR1), num1 - 1, num2, num3));
			workQueue.add(new WorkUnit(new StringBuilder(boardBuilder.toString()).append(COLOR2), num1, num2 - 1, num3));
		} else {
			workQueue.add(new WorkUnit(new StringBuilder(boardBuilder.toString()).append(COLOR1), num1 - 1, num2, num3));
			workQueue.add(new WorkUnit(new StringBuilder(boardBuilder.toString()).append(COLOR2), num1, num2 - 1, num3));
			workQueue.add(new WorkUnit(new StringBuilder(boardBuilder.toString()).append(COLOR3), num1, num2, num3 - 1));
		}
	}
	
	private static class WorkUnit {
		private StringBuilder boardBuilder;
		private int num1;
		private int num2;
		private int num3;
		private boolean isBuilt;
		
		public WorkUnit(StringBuilder boardBuilder, int num1, int num2, int num3) {
			this.boardBuilder = boardBuilder;
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
			isBuilt = (num1 == 0 && num2 == 0 && num3 == 0);
		}

		public StringBuilder getBoardBuilder() {
			return boardBuilder;
		}
		
		public int getNum1() {
			return num1;
		}
		
		public int getNum2() {
			return num2;
		}
		
		public int getNum3() {
			return num3;
		}
		
		public boolean isBuilt() {
			return isBuilt;
		}
	}
}
