package ui;

import game.Board;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class LogPanel extends JPanel {
	
	private static final int ORB_WIDTH = 100;
	private static final int TOTAL_WIDTH = ORB_WIDTH * (Board.NUM_COLS + 2);
	private static final int HEIGHT = ORB_WIDTH * Board.NUM_COLS / 2;
	
	private JTextArea damageReport;
	private JTextArea moveset;
	private JTextArea board;

	public LogPanel() {
		super();
		damageReport = new JTextArea();
		damageReport.setEditable(false);
		damageReport.setFont(new Font("", Font.PLAIN, 14));
		JScrollPane scrollPane = new JScrollPane(damageReport);
		scrollPane.setPreferredSize(new Dimension(TOTAL_WIDTH * 2 / 8, HEIGHT));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		moveset = new JTextArea();
		moveset.setEditable(false);
		moveset.setFont(new Font("", Font.PLAIN, 16));
		moveset.setPreferredSize(new Dimension(TOTAL_WIDTH * 2 / 8, HEIGHT));
		moveset.setLineWrap(true);
		moveset.setWrapStyleWord(true);
		
		board = new JTextArea();
		board.setEditable(false);
		board.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
		board.setPreferredSize(new Dimension(ORB_WIDTH * (Board.NUM_COLS + 2) * 4 / 8, HEIGHT));
		
		add(scrollPane);
		add(moveset);
		add(board);
	}
	
	public void setDamageReport(String text) {
		damageReport.setText(text);
	}
	
	public void setMoveset(String text) {
		moveset.setText(text);
	}
	
	public void setBoard(String text) {
		board.setText(text);
	}

}
