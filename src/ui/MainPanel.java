package ui;

import game.Board;
import game.Color;
import game.Move;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import util.FancyPrinter;
import ai.Agent;
import ai.BoardData;
import ai.BoardEvaluator;
import ai.Configurations;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ActionListener {

    DisplayPanel displayPanel;
    InputPanel inputPanel;
    LogPanel logPanel;
    JButton saveButton, solveButton, replayButton;
    BoardData data;

    public MainPanel() {
        super(new BorderLayout());
        UIMaster.setMainPanel(this);
        displayPanel = new DisplayPanel();
        JPanel input = new JPanel();
        BoxLayout layout = new BoxLayout(input, BoxLayout.Y_AXIS);
        input.setLayout(layout);
        inputPanel = new InputPanel();
        logPanel = new LogPanel();

        JPanel buttonsPanel = new JPanel();
        saveButton = new JButton("Save");
        solveButton = new JButton("Solve");
        replayButton = new JButton("Replay");
        saveButton.addActionListener(this);
        solveButton.addActionListener(this);
        replayButton.addActionListener(this);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(solveButton);
        buttonsPanel.add(replayButton);

        input.add(inputPanel);
        input.add(buttonsPanel);

        add(input, BorderLayout.LINE_START);
        add(displayPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.PAGE_END);

        data = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            updateConfigurations();
            Configurations.saveConfigurations();
        } else if (e.getSource() == solveButton) {
            updateConfigurations();
            Agent agent = new Agent();
            agent.solve();
            data = agent.getBestBoardData();
            int cursorRow = data.getBoard().getOriginalCursorRow();
            int cursorCol = data.getBoard().getOriginalCursorCol();
            Board board = Configurations.getBoard();
            board.setCursor(cursorRow, cursorCol);
            List<Move> moveset = data.getMoveset();

            BoardEvaluator boardEvaluator = new BoardEvaluator();
            boardEvaluator.evaluate(data.getBoard());
            logPanel.setDamageReport(boardEvaluator.getDamageReport());
            logPanel.setMoveset(moveset.toString());
            logPanel.setBoard(FancyPrinter.toString(board, moveset));

            BoardDisplayPanel boardDisplayPanel = UIMaster.getBoardDisplayPanel();
            boardDisplayPanel.execute(data);
        } else if (e.getSource() == replayButton) {
            if (data != null) {
                BoardDisplayPanel boardDisplayPanel = UIMaster.getBoardDisplayPanel();
                boardDisplayPanel.execute(data);
            }
        }
    }

    private void updateConfigurations() {
        AttributeInputPanel attributeInputPanel = UIMaster.getAttributeInputPanel();
        LeaderSkillPanel leaderSkillPanel = UIMaster.getLeaderSkillPanel();
        BoardInputPanel boardInputPanel = UIMaster.getBoardInputPanel();

        Map<String, String> teamInfo = new HashMap<String, String>();
        for (Color color : AttributeInputPanel.ATTRIBUTES.keySet()) {
            teamInfo.put(color.toString(), attributeInputPanel.getAttributeValue(color));
            teamInfo.put(color.toString() + Configurations.TPA_KEY, attributeInputPanel.getAttributeTPA(color));
            teamInfo.put(color.toString() + Configurations.ROW_ENHANCE_KEY, attributeInputPanel.getAttributeRE(color));
            teamInfo.put(
                    color.toString() + Configurations.ROW_REQUIRED_KEY,
                    String.valueOf(attributeInputPanel.isRowRequired(color)));
        }
        teamInfo.put(Configurations.LEADERSKILL_KEY + 1, leaderSkillPanel.getLeaderSkill(1));
        teamInfo.put(Configurations.LEADERSKILL_KEY + 2, leaderSkillPanel.getLeaderSkill(2));
        teamInfo.put(Configurations.MIN_COMBO_KEY, inputPanel.getMinCombosValue());
        Configurations.setTeamInfo(teamInfo);
        Configurations.setBoard(boardInputPanel.getBoard());
    }

    public JButton getSolveButton() {
        return solveButton;
    }
}
