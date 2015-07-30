package util;

import game.Board;
import game.Color;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class ImageProcessor {

	private static final double ORB_WIDTH = 0.166666;
	private static final double CAPTURE_WIDTH = ORB_WIDTH / Math.sqrt(2.0);
	private static final double CAPTURE_OFFSET = (ORB_WIDTH - CAPTURE_WIDTH) / 2.0;
	
	private static final int NUM_ROWS = Board.NUM_ROWS;
	private static final int NUM_COLS = Board.NUM_COLS;
	
	private static final String COLORMAP_SAMPLES_PATH = "./resources/colormaps/";
	private static final String OVERRIDE_PATH = "./resources/colormap-override.cfg";
	private static final String COLORMAP_PATH = "./resources/colormap.cfg";
	private static final String RGB_DETAILS_PATH = "./out/rgb-details";
	
	private static Map<RGB, Color> colorMap;
	
	public ImageProcessor() {
		colorMap = new HashMap<RGB, Color>();
		loadColorMap();
		if (colorMap.isEmpty()) populateColorMap();
	}
	
	public String convertImage(BufferedImage image) {
		try {
			loadColorMap();
			int width = image.getWidth();
			int height = image.getHeight();
			int orbWidth = (int) (width * ORB_WIDTH);
			int startX = 0;
			int startY = height - (NUM_ROWS * orbWidth);
			RGB[][] rgbMap = parseBoardImage(image, startX, startY);
			
			StringBuilder boardBuilder = new StringBuilder();
			StringBuilder rgbBuilder = new StringBuilder();
			for (RGB[] col : rgbMap) {
				for (RGB rgb : col) {
					int r = rgb.getRed();
					int g = rgb.getGreen();
					int b = rgb.getBlue();
					int minDiff = Integer.MAX_VALUE;
					String color = " ";
					for (RGB key : colorMap.keySet()) {
						int keyR = key.getRed();
						int keyG = key.getGreen();
						int keyB = key.getBlue();
						int totalDiff = Math.abs(r - keyR) + Math.abs(g - keyG) + Math.abs(b - keyB);
						if (totalDiff < minDiff) {
							minDiff = totalDiff;
							color = colorMap.get(key).toString();
						}
					}
					boardBuilder.append(color);
					rgbBuilder.append(r + "," + g + "," + b + ":" + color + " ");
				}
				rgbBuilder.append("\n");
			}
			String rgbDetails = rgbBuilder.toString();
			PrintWriter writer = new PrintWriter(RGB_DETAILS_PATH);
			writer.print(rgbDetails);
			writer.close();
			
			String board = boardBuilder.toString();
			return board;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return "";
		}
	}
	
	public String convertImage(File file) {
		try {
			BufferedImage image = ImageIO.read(file);
			return convertImage(image);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return "";
		}
	}
	
	public String convertImage(String filepath) {
		File file = new File(filepath);
		return convertImage(file);
	}
	
	public String convertImage(Rectangle screenRect) {
		Robot robot;
		try {
			robot = new Robot();
			BufferedImage image = robot.createScreenCapture(screenRect);
			return convertImage(image);
		} catch (AWTException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public void populateColorMap() {		
		Scanner scanner = null;
		PrintWriter writer = null;
		try {
			
			// populate entries from the colormap override file first
			scanner = new Scanner(new File(OVERRIDE_PATH));
			String line;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				String key = line.split(":")[0];
				String val = line.split(":")[1];
				int r = Integer.parseInt(key.split(",")[0]);
				int g = Integer.parseInt(key.split(",")[1]);
				int b = Integer.parseInt(key.split(",")[2]);
				Color color = StringToColorConverter.getColor(val);
				colorMap.put(new RGB(r, g, b), color);
			}
			
			// populate entries using sample boards
			File folder = new File(COLORMAP_SAMPLES_PATH);
			for (File file : folder.listFiles()) {
				String filename = file.getName();
				if (!filename.contains(".PNG")) {
					System.out.println("Ignoring " + filename + " because it is not .PNG format");
					continue;
				}
				filename = filename.split("//.PNG")[0];
				BufferedImage image = ImageIO.read(file);
				RGB[][] rgbMap = parseBoardImage(image, 0, 0);
				String character;
				Color color;
				for (int r = 0; r < NUM_ROWS; r++) {
					for (int c = 0; c < NUM_COLS; c++) {
						character = filename.substring(r * NUM_COLS + c, r * NUM_COLS + c + 1);
						color = StringToColorConverter.getColor(character);
						colorMap.put(rgbMap[r][c], color);
					}
				}
			}
			
			// write entries to colormap file
			writer = new PrintWriter(COLORMAP_PATH);
			for (RGB key : colorMap.keySet()) {
				writer.println(key + ":" + colorMap.get(key));
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		} finally {
			if (scanner != null) try { scanner.close(); } catch (Exception e) { }
			if (writer != null) try { writer.close(); } catch (Exception e) { }
		}
	}
	
	private void loadColorMap() {
		colorMap = new HashMap<RGB, Color>();
		File colorMapFile = new File(COLORMAP_PATH);
		try {
			Scanner scanner = new Scanner(colorMapFile);
			String line;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				String key = line.split(":")[0];
				String val = line.split(":")[1];
				int r = Integer.parseInt(key.split(",")[0]);
				int g = Integer.parseInt(key.split(",")[1]);
				int b = Integer.parseInt(key.split(",")[2]);
				Color color = StringToColorConverter.getColor(val);
				colorMap.put(new RGB(r, g, b), color);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private RGB[][] parseBoardImage(BufferedImage boardImage, int startX, int startY) {
		int width = boardImage.getWidth();
		int orbWidth = (int) (width * ORB_WIDTH);
		int offset = (int) (width * CAPTURE_OFFSET);
		int captureWidth = (int) (width * CAPTURE_WIDTH);
		
		int orbStartX;
		int orbStartY;
		int captureStartX;
		int captureStartY;
		int[] rgb;
		RGB[][] rgbMap = new RGB[NUM_ROWS][NUM_COLS];
		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0 ; c < NUM_COLS; c++) {
				orbStartX = c * orbWidth + startX;
				orbStartY = r * orbWidth + startY;
				captureStartX = orbStartX + offset;
				captureStartY = orbStartY + offset;
				rgb = boardImage.getRGB(
						captureStartX, 
						captureStartY, 
						captureWidth, 
						captureWidth, 
						null, 0, captureWidth);
				int[] reds = extractColor(rgb, 16);
				int[] greens = extractColor(rgb, 8);
				int[] blues = extractColor(rgb, 0);
				rgbMap[r][c] = new RGB(average(reds), average(greens), average(blues));
			}
		}
		return rgbMap;
	}
	
	private int[] extractColor(int[] rgb, int offset) {
		int[] colors = new int[rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			colors[i] = (rgb[i] >> offset) & 0xFF;
		}
		return colors;
	}
	
	public int mode(int[] ints) {
		Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
		int maxCount = 0;
		int mode = 0;
		Integer key;
		int count;
		for (int i : ints) {
			key = new Integer(i);
			if (counts.keySet().contains(key)) {
				count = counts.get(key).intValue() + 1;
			} else {
				count = 1;
			}
			if (count > maxCount) {
				maxCount = count;
				mode = i;
			}
			counts.put(key, count);
		}
		return mode;
	}
	
	private int average(int[] ints) {
		int total = 0;
		for (int i : ints) {
			total += i;
		}
		return total/ints.length;
	}

}
