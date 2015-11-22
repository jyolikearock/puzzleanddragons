package runnable;

import game.Board;
import game.Color;

@SuppressWarnings("unused")
public class LSProcRateCalculator {
    
    private static final int NUM_ITERATIONS = 100000;
    
    private static int[] colorCount;
    
    public static void main(String[] args) {
        Board board;
        int successCount = 0;
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            colorCount = new int[Board.NUM_COLORS];
            board = new Board();
            board.fill();
            Color color;
            for (int r = 0; r < Board.NUM_ROWS; r++) {
                for (int c = 0; c < Board.NUM_COLS; c++) {
                    color = board.get(r, c).getColor();
                    if (color.equals(Color.R)) {
                        colorCount[0]++;
                    } else if (color.equals(Color.G)) {
                        colorCount[1]++;
                    } else if (color.equals(Color.B)) {
                        colorCount[2]++;
                    } else if (color.equals(Color.L)) {
                        colorCount[3]++;
                    } else if (color.equals(Color.D)) {
                        colorCount[4]++;
                    } else if (color.equals(Color.H)) {
                        colorCount[5]++;
                    }
                }
            }
            
            if (perseus2()) {
                successCount++;
            }
        }
        
        System.out.println(String.format("Leader skill was proc'd on %d out of %d boards (%.2f%%)", 
                successCount, 
                NUM_ITERATIONS, 
                ((double) successCount) / NUM_ITERATIONS * 100));
    }
    
    // 3 specific colors
    private static boolean suzaku() {
        return (colorCount[0] >= 3 && 
                colorCount[1] >= 3 && 
                colorCount[3] >= 3);
    }
    
    // 3 of 4 colors
    private static boolean suzaku2() {
        int numMatchableColors = 0;
        if (colorCount[0] >= 3) numMatchableColors++;
        if (colorCount[1] >= 3) numMatchableColors++;
        if (colorCount[2] >= 3) numMatchableColors++;
        if (colorCount[3] >= 3) numMatchableColors++;
        return (numMatchableColors >= 3);
    }
    
    // 4 specific colors
    private static boolean kirin() {
        return (colorCount[0] >= 3 && 
                colorCount[1] >= 3 && 
                colorCount[2] >= 3 && 
                colorCount[3] >= 3);
    }

    // 5 specific colors
    private static boolean kaliD() {
        return (colorCount[0] >= 3 && 
                colorCount[1] >= 3 && 
                colorCount[2] >= 3 && 
                colorCount[4] >= 3 && 
                colorCount[5] >= 3);
    }
    
    // 4 colors (hearts exclusive)
    private static boolean horus() {
        int numMatchableColors = 0;
        for (int i = 0; i < 5; i++) {
            if (colorCount[i] >= 3) {
                numMatchableColors++;
            }
        }
        return (numMatchableColors >= 4);
    }
    
    // 5 colors (hearts exclusive)
    private static boolean raLD() {
        int numMatchableColors = 0;
        for (int i = 0; i < 5; i++) {
            if (colorCount[i] >= 3) {
                numMatchableColors++;
            }
        }
        return (numMatchableColors >= 5);
    }
    
    // 3 colors (hearts inclusive)
    private static boolean isis() {
        int numMatchableColors = 0;
        for (int i = 0; i < 6; i++) {
            if (colorCount[i] >= 3) {
                numMatchableColors++;
            }
        }
        return (numMatchableColors >= 3);
    }
    
    // 4 colors (hearts inclusive)
    private static boolean dqxq() {
        int numMatchableColors = 0;
        for (int i = 0; i < 6; i++) {
            if (colorCount[i] >= 3) {
                numMatchableColors++;
            }
        }
        return (numMatchableColors >= 4);
    }
    
    // 5 colors (hearts inclusive)
    private static boolean ra1() {
        int numMatchableColors = 0;
        for (int i = 0; i < 6; i++) {
            if (colorCount[i] >= 3) {
                numMatchableColors++;
            }
        }
        return (numMatchableColors >= 5);
    }
    
    // all 6 colors
    private static boolean ra2() {
        int numMatchableColors = 0;
        for (int i = 0; i < 6; i++) {
            if (colorCount[i] >= 3) {
                numMatchableColors++;
            }
        }
        return (numMatchableColors >= 6);
    }
    
    // 1 match of specific color
    private static boolean vishnu() {
        return (colorCount[0] >= 3);
    }
    
    // 2 matches of specific color
    private static boolean krishna() {
        return (colorCount[0] >= 6);
    }
    
    // 3 matches of specific color
    private static boolean sarasvati() {
        return (colorCount[0] >= 9);
    }
    
    // 7 orbs of a specific color
    private static boolean perseus2() {
        return (colorCount[0] >= 7);
    }
    
    // 8 orbs of a specific color
    private static boolean perseus3() {
        return (colorCount[0] >= 8);
    }
}
