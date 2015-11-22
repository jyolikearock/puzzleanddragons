package ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DisplayPanel extends JPanel {
    
    BoardDisplayPanel boardDisplayPanel;
    LogPanel logPanel;

    public DisplayPanel() {
        super(new BorderLayout());
        UIMaster.setDisplayPanel(this);
        boardDisplayPanel = new BoardDisplayPanel();
        
        add(boardDisplayPanel);
    }
    
    public BoardDisplayPanel getBoardDisplayPanel() {
        return boardDisplayPanel;
    }

}
