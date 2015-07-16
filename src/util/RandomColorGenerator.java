package util;

import game.Color;

import java.util.Random;

public class RandomColorGenerator {
	
	private static final Random RNG = new Random();
	
	public static Color getRandomColor(int numColors) {
		int randomNum = RNG.nextInt(numColors);
		if (randomNum == 0) {
			return Color.R;
		} else if (randomNum == 1) {
			return Color.G;
		} else if (randomNum == 2) {
			return Color.B;
		} else if (randomNum == 3) {
			return Color.L;
		} else if (randomNum == 4) {
			return Color.D;
		} else if (randomNum == 5) {
			return Color.H;
		} else if (randomNum == 6) {
			return Color.P;
		} else if (randomNum == 7) {
			return Color.J;
		} else {
			System.out.println("Only 8 colors to choose from; RNG returned " + randomNum);
			return null;
		}
	}

}
