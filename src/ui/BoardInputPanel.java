package ui;

import game.Board;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.ImageProcessor;
import ai.Configurations;

@SuppressWarnings("serial")
public class BoardInputPanel extends JPanel implements ActionListener {
	
	private static final int FONT_SIZE = 16;
	
	private static final String BOARDS_DIR = "./boards/";
	private static final String COLOR_MAP_DIR = "./resources/colormaps/";
	private static final String SCREEN_CAPTURE_CONFIGS_DIR = "./configurations/screen-capture-configurations.cfg";
	private static final String OFFSET_X_KEY = "offsetx";
	private static final String OFFSET_Y_KEY = "offsety";
	private static final String WIDTH_KEY = "width";
	private static final String HEIGHT_KEY = "height";

	JTextArea boardText;
	JButton convertButton, browseButton, captureButton;
	Board board;
	
	public BoardInputPanel() {
		super();
		UIMaster.setBoardInputPanel(this);
		board = new Configurations().getBoard();
		
		boardText = new JTextArea();
		boardText.setFont(new Font("monospaced", Font.PLAIN, FONT_SIZE));
		boardText.setLineWrap(true);
		boardText.setWrapStyleWord(false);
		boardText.setPreferredSize(new Dimension(Board.NUM_COLS * FONT_SIZE * 2 / 3, Board.NUM_ROWS * FONT_SIZE * 4 / 3));
		boardText.setText(board.toCode());
		
		JPanel buttonsPanel = new JPanel();
		BoxLayout layout = new BoxLayout(buttonsPanel,BoxLayout.Y_AXIS);
		buttonsPanel.setLayout(layout);
		convertButton = new JButton("Convert");
		browseButton = new JButton("Browse");
		captureButton = new JButton("Screen Cap");
		
		convertButton.addActionListener(this);
		browseButton.addActionListener(this);
		captureButton.addActionListener(this);
		
		buttonsPanel.add(convertButton);
		buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		buttonsPanel.add(browseButton);
		buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		buttonsPanel.add(captureButton);
		
		add(boardText);
		add(buttonsPanel);
	}
	
	public Board getBoard() {
		return new Board(boardText.getText().split(""));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BoardDisplayPanel boardDisplayPanel = UIMaster.getBoardDisplayPanel();
		if (e.getSource() == convertButton) {
			String input = boardText.getText();
			if (input == null || input.isEmpty()) {
				displayMessage("Couldn't generate board because input was empty");
			} else if (input.length() != Board.NUM_ROWS * Board.NUM_COLS) {
				displayMessage("Couldn't generate board because length was not 30");
			} else {
				String[] boardText = input.split("");
				board = new Board(boardText);
				boardDisplayPanel.drawBoard(input);
				Configurations.setBoard(board);
			}
		} else if (e.getSource() == browseButton) {
			File boardImageFile = null;
			JFileChooser chooser = new JFileChooser(BOARDS_DIR);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "bmp", "gif");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(new Container());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				boardImageFile = chooser.getSelectedFile();
				System.out.println("Selected file " + boardImageFile.getName());
			} else {
				displayMessage("Failed to select image file");
			}
			ImageProcessor imageProcessor = new ImageProcessor(COLOR_MAP_DIR);
			String boardAsString = imageProcessor.convertImage(boardImageFile);
			boardText.setText(boardAsString);
			board = new Board(boardAsString.split(""));
			boardDisplayPanel.drawBoard(boardAsString);
			Configurations.setBoard(board);
		} else if (e.getSource() == captureButton) {
			ImageProcessor imageProcessor = new ImageProcessor(COLOR_MAP_DIR);
			String boardAsString = imageProcessor.convertImage(createScreenCaptureArea());
			boardText.setText(boardAsString);
			board = new Board(boardAsString.split(""));
			boardDisplayPanel.drawBoard(boardAsString);
			Configurations.setBoard(board);
			
			MainPanel mainPanel = UIMaster.getMainPanel();
			ActionEvent action = new ActionEvent(mainPanel.getSolveButton(), ActionEvent.ACTION_FIRST, "solve");
			mainPanel.actionPerformed(action);
		}
	}
	
	private Rectangle createScreenCaptureArea() {
		try {
			Scanner scanner = new Scanner(new File(SCREEN_CAPTURE_CONFIGS_DIR));
			int offsetx = 0;
			int offsety = 0;
			int width = 0;
			int height = 0;
			String line, key, value;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				key = line.split(":")[0];
				value = line.split(":")[1];
				if (key.equals(OFFSET_X_KEY)) offsetx = Integer.parseInt(value);
				if (key.equals(OFFSET_Y_KEY)) offsety = Integer.parseInt(value);
				if (key.equals(WIDTH_KEY)) width = Integer.parseInt(value);
				if (key.equals(HEIGHT_KEY)) height = Integer.parseInt(value);
			}
			scanner.close();
			return new Rectangle(offsetx, offsety, width, height);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private void displayMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

}
