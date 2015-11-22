package ui;

import runnable.PADBot;

public class UIMaster {
    
    /*
     *     PADBot (JFrame)
     *         MainPanel
     *             DisplayPanel
     *                 BoardDisplayPanel
     *                     drawBoard()
     *                     execute()
     *                 LogPanel
     *             InputPanel
     *                 AttributeInputPanel
     *                 LeaderSkillPanel
     *                 BoardInputPanel
     *                     convertButton
     *                     browseButton
     *                     screenCapButton
     *                 minCombos (JTextField)
     *             saveConfigButton
     *             solveButton
     */
    
    private static PADBot mainFrame;
    private static MainPanel mainPanel;
    private static DisplayPanel displayPanel;
    private static BoardDisplayPanel boardDisplayPanel;
    private static InputPanel inputPanel;
    private static AttributeInputPanel attributeInputPanel;
    private static LeaderSkillPanel leaderSkillPanel;
    private static BoardInputPanel boardInputPanel;
    
    public static PADBot getMainFrame() {
        return mainFrame;
    }
    
    public static void setMainFrame(PADBot mainFrame) {
        UIMaster.mainFrame = mainFrame;
    }
    
    public static MainPanel getMainPanel() {
        return mainPanel;
    }
    
    public static void setMainPanel(MainPanel mainPanel) {
        UIMaster.mainPanel = mainPanel;
    }
    
    public static DisplayPanel getDisplayPanel() {
        return displayPanel;
    }
    
    public static void setDisplayPanel(DisplayPanel displayPanel) {
        UIMaster.displayPanel = displayPanel;
    }
    
    public static BoardDisplayPanel getBoardDisplayPanel() {
        return boardDisplayPanel;
    }
    
    public static void setBoardDisplayPanel(BoardDisplayPanel boardDisplayPanel) {
        UIMaster.boardDisplayPanel = boardDisplayPanel;
    }
    
    public static InputPanel getInputPanel() {
        return inputPanel;
    }
    
    public static void setInputPanel(InputPanel inputPanel) {
        UIMaster.inputPanel = inputPanel;
    }
    
    public static AttributeInputPanel getAttributeInputPanel() {
        return attributeInputPanel;
    }
    
    public static void setAttributeInputPanel(
            AttributeInputPanel attributeInputPanel) {
        UIMaster.attributeInputPanel = attributeInputPanel;
    }
    
    public static LeaderSkillPanel getLeaderSkillPanel() {
        return leaderSkillPanel;
    }
    
    public static void setLeaderSkillPanel(LeaderSkillPanel leaderSkillPanel) {
        UIMaster.leaderSkillPanel = leaderSkillPanel;
    }
    
    public static BoardInputPanel getBoardInputPanel() {
        return boardInputPanel;
    }
    
    public static void setBoardInputPanel(BoardInputPanel boardInputPanel) {
        UIMaster.boardInputPanel = boardInputPanel;
    }
}
