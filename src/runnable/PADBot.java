package runnable;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import ui.MainPanel;

public class PADBot {
	
	private static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	public static void main(String[] args) {
		JFrame mainFrame = new JFrame("PADBot");
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new MainPanel();
		mainFrame.add(mainPanel);
		
		mainFrame.pack();
		mainFrame.setLocation((SCREEN_WIDTH - mainFrame.getWidth()) / 2, (SCREEN_HEIGHT - mainFrame.getHeight()) / 2);
		mainFrame.setVisible(true);
	}

}
