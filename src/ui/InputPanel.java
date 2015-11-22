package ui;

import game.Board;

import java.awt.Dimension;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ai.Configurations;

@SuppressWarnings("serial")
public class InputPanel extends JPanel {

    private static final int ORB_WIDTH = 100;

    private Map<String, String> teamInfo;
    private AttributeInputPanel attributeInputPanel;
    private LeaderSkillPanel leaderSkillPanel;
    private BoardInputPanel boardInputPanel;
    private JTextField minCombosValue;

    public InputPanel() {
        super();
        UIMaster.setInputPanel(this);
        setPreferredSize(new Dimension(2 * ORB_WIDTH, (Board.NUM_ROWS - 1) * ORB_WIDTH));
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        teamInfo = Configurations.getTeamInfo();

        attributeInputPanel = new AttributeInputPanel();
        leaderSkillPanel = new LeaderSkillPanel();
        boardInputPanel = new BoardInputPanel();

        JPanel minCombosPanel = new JPanel();
        JLabel minCombosLabel = new JLabel("Min Combos");
        minCombosValue = new JTextField(teamInfo.get(Configurations.MIN_COMBO_KEY), 2);
        minCombosPanel.add(minCombosLabel);
        minCombosPanel.add(minCombosValue);

        add(attributeInputPanel);
        add(leaderSkillPanel);
        add(boardInputPanel);
        add(minCombosPanel);
    }

    public AttributeInputPanel getAttributeInputPanel() {
        return attributeInputPanel;
    }

    public LeaderSkillPanel getLeaderSkillPanel() {
        return leaderSkillPanel;
    }

    public BoardInputPanel getBoardInputPanel() {
        return boardInputPanel;
    }

    public String getMinCombosValue() {
        return minCombosValue.getText();
    }

}
