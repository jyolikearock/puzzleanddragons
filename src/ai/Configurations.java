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
	
	private static int maxNumMoves = 10;
	private static int numCursorOrbsToTry = 30;

	private static boolean useCache = false;
	private static boolean prune = false;

	public static Board globalBoard;
	public static Map<String, String> globalTeamInfo;
	
	static {
		loadConfigurations();
	}
	
	private Board board;
	private Map<String, String> teamInfo;
	
	public Configurations() {
		if (globalBoard != null) {
			board = new Board(globalBoard);
		} else {
			board = new Board();
			board.fill(Board.NUM_COLORS);
		}
		
		teamInfo = new HashMap<String, String>();
		for (String key : globalTeamInfo.keySet()) {
			if (globalTeamInfo.containsKey(key) && !globalTeamInfo.get(key).isEmpty()) {
				teamInfo.put(key, globalTeamInfo.get(key));
			} else {
				teamInfo.put(key, "");
			}
		}
	}
	
	/*
	 * static methods
	 */
	
	public static int getMaxNumMoves() {
		return maxNumMoves;
	}

	public static void setMaxNumMoves(int maxNumMoves) {
		Configurations.maxNumMoves = maxNumMoves;
	}
	
	public static int getNumCursorOrbsToTry() {
		return numCursorOrbsToTry;
	}

	public static void setNumCursorOrbsToTry(int numCursorOrbsToTry) {
		Configurations.numCursorOrbsToTry = numCursorOrbsToTry;
	}

	public static boolean useCache() {
		return useCache;
	}

	public static void setUseCache(boolean useCache) {
		Configurations.useCache = useCache;
	}

	public static boolean prune() {
		return prune;
	}

	public static void setPrune(boolean prune) {
		Configurations.prune = prune;
	}
	
	public static void setBoard(Board newBoard) {
		globalBoard = newBoard;
	}
	
	public static void setTeamInfo(Map<String,String> newTeamInfo) {
		globalTeamInfo = newTeamInfo;
	}
	
	/*
	 * non-static methods
	 */

	public Board getBoard() {
		return board;
	}
	
	public Map<String, String> getTeamInfo() {
		return teamInfo;
	}
	
	public int getAttributeValue(Color color) {
		String key = color.toString();
		return (teamInfo.containsKey(key) && !teamInfo.get(key).isEmpty()) ? Integer.parseInt(teamInfo.get(key)) : 0;
	}
	
	public double getTPAMultiplier(Color color) {
		String key = color.toString() + TPA_KEY;
		return (teamInfo.containsKey(key) && !teamInfo.get(key).isEmpty()) ? Double.parseDouble(teamInfo.get(key)) : 1.0;
	}
	
	public int getNumRowEnhances(Color color) {
		String key = color.toString() + ROW_ENHANCE_KEY;
		return (teamInfo.containsKey(key) && !teamInfo.get(key).isEmpty()) ? Integer.parseInt(teamInfo.get(key)) : 1;
	}
	
	public LeaderSkill getLeaderSkill(int i) {
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
	
	public int getMinCombos() {
		return (teamInfo.containsKey(MIN_COMBO_KEY) && !teamInfo.get(MIN_COMBO_KEY).isEmpty()) ? Integer.parseInt(teamInfo.get(MIN_COMBO_KEY)) : 0;
	}
	
	public boolean containsKey(String key) {
		return teamInfo.containsKey(key);
	}
	
	/*
	 * private methods
	 */
	
	private static void loadConfigurations() {
		globalTeamInfo = new HashMap<String, String>();
		File file = new File(CONFIGURATIONS_FILE_NAME);
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] split = line.split(":");
				String key = split[0];
				String value = split[1];
				if (key.equals(BOARD_KEY)) globalBoard = new Board(value.split(""));
				else globalTeamInfo.put(split[0], split[1]);
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
			if (globalBoard != null) {
				writer.println(BOARD_KEY + ":" + globalBoard.toCode());
			}
			if (globalTeamInfo != null && !globalTeamInfo.isEmpty()) {
				for (String key : globalTeamInfo.keySet()) {
					if (globalTeamInfo.containsKey(key) && !globalTeamInfo.get(key).isEmpty()) {
						writer.println(key + ":" + globalTeamInfo.get(key));
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

}
