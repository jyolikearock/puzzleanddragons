package ui;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ai.Configurations;

@SuppressWarnings("serial")
public class LeaderSkillPanel extends JPanel {
	
	private static final String LEADER_SKILLS_DIR = "./src/leaderskill/";

	JComboBox<String> leaderSkill1, leaderSkill2;
	Map<String, String> teamInfo;
	
	public LeaderSkillPanel() {
		super();
		UIMaster.setLeaderSkillPanel(this);
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		teamInfo = new Configurations().getTeamInfo();
		String[] availableLeaderSkills = getAvailableLeaderSkills();
		
		JPanel lsPanel1 = new JPanel();
		JLabel lsLabel1 = new JLabel("LSkill 1");
		leaderSkill1 = new JComboBox<String>(availableLeaderSkills);
		leaderSkill1.setSelectedItem(teamInfo.get(Configurations.LEADERSKILL_KEY + 1));
		lsPanel1.add(lsLabel1);
		lsPanel1.add(leaderSkill1);
		
		JPanel lsPanel2 = new JPanel();
		JLabel lsLabel2 = new JLabel("LSkill 2");
		leaderSkill2 = new JComboBox<String>(availableLeaderSkills);
		leaderSkill2.setSelectedItem(teamInfo.get(Configurations.LEADERSKILL_KEY + 2));
		lsPanel2.add(lsLabel2);
		lsPanel2.add(leaderSkill2);
		
		add(lsPanel1);
		add(lsPanel2);
	}
	
	public String getLeaderSkill(int i) {
		if (i == 1) {
			return (String) leaderSkill1.getSelectedItem();
		} else if (i == 2) {
			return (String) leaderSkill2.getSelectedItem();
		}
		return "";
	}
	
	private static String[] getAvailableLeaderSkills() {
		Set<String> availableLeaderSkills = new TreeSet<String>();
		File file = new File(LEADER_SKILLS_DIR);
		for (File leaderSkill : file.listFiles()) {
			String name = leaderSkill.getName().split(".java")[0];
			if (name.equals("LeaderSkill")) continue;
			availableLeaderSkills.add(name);
		}
		String[] asArray = new String[availableLeaderSkills.size()];
		int index = 0;
		for (String leaderSkill : availableLeaderSkills) {
			asArray[index] = leaderSkill;
			index++;
		}
		return asArray;
	}

}
