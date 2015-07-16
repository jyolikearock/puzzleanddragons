package ai;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import leaderskill.LeaderSkill;
import leaderskill.NoLeaderSkill;

public class TeamInfo {
	
	private static final String TEAM_INFO_PATH = "./resources/team-info.cfg";
	
	private Map<String, String> teamInfo;
	
	public TeamInfo() {
		loadTeamInfo();
	}
	
	private void loadTeamInfo() {
		teamInfo = new HashMap<String, String>();
		File file = new File(TEAM_INFO_PATH);
		try {
			Scanner scanner = new Scanner(file);
			String line;
			String[] lineSplit;
			String key;
			String value;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				lineSplit = line.split(":");
				key = lineSplit[0];
				value = lineSplit[1];
				teamInfo.put(key, value);
			}
			scanner.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public boolean containsKey(String key) {
		return teamInfo.containsKey(key);
	}
	
	public double getInt(String key) {
		String value = get(key);
		return new Integer(value).intValue();
	}
	
	public double getDouble(String key) {
		String value = get(key);
		return new Double(value).doubleValue();
	}
	
	public LeaderSkill getLeaderSkill(String key) {
		String value = get(key);
		if (value.isEmpty()) return new NoLeaderSkill();
		try {
			return (LeaderSkill) Class.forName("leaderskill." + value).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String get(String key) {
		if (!teamInfo.containsKey(key)) {
			System.out.println("Could not find any entry in team info with key '" + key + "'");
			return "";
		}
		return teamInfo.get(key);
	}

}
