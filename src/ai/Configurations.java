package ai;

import game.Board;
import game.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import leaderskill.LeaderSkill;

public class Configurations {
	
	private static final String CONFIGURATIONS_FILE_NAME = "./configurations/saved-configurations.cfg";
	public static final String TPA_KEY = "TPA";
	public static final String ROW_ENHANCE_KEY = "RE";
	public static final String LEADERSKILL_KEY = "LS";
	public static final String MIN_COMBO_KEY = "mincombo";
	public static final String BOARD_KEY = "board";
	
	// effectively constants, but can be configured
	private static int numThreads = 4;
	private static int timeLimitMillis = 3 * 1000;
	private static int numCursorOrbsToTry = 30;
	private static Board board;
	private static Map<String, String> teamInfo;
	
	static {
		loadConfigurations();
	}
	
	/*
	 * methods for accessing configurations
	 */
	
	public static int getAttributeValue(Color color) {
		String key = color.toString();
		return 
				(teamInfo.containsKey(key) && !teamInfo.get(key).isEmpty()) ? 
				Integer.parseInt(teamInfo.get(key)) : 
				0;
	}
	
	public static double getTPAMultiplier(Color color) {
		String key = color.toString() + TPA_KEY;
		return 
				(teamInfo.containsKey(key) && !teamInfo.get(key).isEmpty()) ? 
				Double.parseDouble(teamInfo.get(key)) : 
				1.0;
	}
	
	public static int getNumRowEnhances(Color color) {
		String key = color.toString() + ROW_ENHANCE_KEY;
		return 
				(teamInfo.containsKey(key) && !teamInfo.get(key).isEmpty()) ? 
				Integer.parseInt(teamInfo.get(key)) : 
				1;
	}
	
	public static LeaderSkill getLeaderSkill(int i) {
		if (i != 1 && i != 2) {
			System.out.println("You must select either Leaderskill 1 or Leaderskill 2");
			System.exit(0);
		}
		String key = LEADERSKILL_KEY + i;
		String name = teamInfo.get(key);
		try {
			return (LeaderSkill) Class.forName("leaderskill." + name).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			return null;
		}
	}
	
	public static int getMinCombos() {
		return 
				(teamInfo.containsKey(MIN_COMBO_KEY) && !teamInfo.get(MIN_COMBO_KEY).isEmpty()) ? 
				Integer.parseInt(teamInfo.get(MIN_COMBO_KEY)) : 
				0;
	}
	
	public static boolean containsKey(String key) {
		return teamInfo.containsKey(key);
	}
	
	/*
	 * private methods
	 */
	
	private static void loadConfigurations() {
		teamInfo = new HashMap<String, String>();
		File file = new File(CONFIGURATIONS_FILE_NAME);
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] split = line.split(":");
				String key = split[0];
				String value = split[1];
				if (key.equals(BOARD_KEY)) board = new Board(value.split(""));
				else teamInfo.put(split[0], split[1]);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} finally {
			if (scanner != null) scanner.close();
		}
	}
	
	public static void saveConfigurations() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(CONFIGURATIONS_FILE_NAME);
			if (board != null) {
				writer.println(BOARD_KEY + ":" + board.toCode());
			}
			if (teamInfo != null && !teamInfo.isEmpty()) {
				for (String key : teamInfo.keySet()) {
					if (teamInfo.containsKey(key) && !teamInfo.get(key).isEmpty()) {
						writer.println(key + ":" + teamInfo.get(key));
					}
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) writer.close();
		}
	}
	
	/*
	 * getters and setters
	 */

	public static int getNumThreads() {
		return numThreads;
	}

	public static void setNumThreads(int numThreads) {
		Configurations.numThreads = numThreads;
	}

	public static int getTimeLimitMillis() {
		return timeLimitMillis;
	}

	public static void setTimeLimitMillis(int timeLimitMillis) {
		Configurations.timeLimitMillis = timeLimitMillis;
	}

	public static int getNumCursorOrbsToTry() {
		return numCursorOrbsToTry;
	}

	public static void setNumCursorOrbsToTry(int numCursorOrbsToTry) {
		Configurations.numCursorOrbsToTry = numCursorOrbsToTry;
	}

	public static Board getBoard() {
		return new Board(board);
	}

	public static void setBoard(Board board) {
		Configurations.board = board;
	}

	public static Map<String, String> getTeamInfo() {
		return teamInfo;
	}

	public static void setTeamInfo(Map<String, String> teamInfo) {
		Configurations.teamInfo = teamInfo;
	}

}
