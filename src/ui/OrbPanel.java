package ui;

import game.Color;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class OrbPanel extends JPanel implements ImageObserver {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String IMAGES_DIR = "./resources/images/";
    private static final String IMAGE_FORMAT = ".png";
    private static final String FADED_SUFFIX = "-fade";
    private static final String CURSOR_SUFFIX = "-cursor";

    BufferedImage image;
    
    public OrbPanel(Color color, boolean faded, boolean cursor) {
        String filename;
        if (faded) filename = IMAGES_DIR + color.toString().toLowerCase() + FADED_SUFFIX + IMAGE_FORMAT;
        else if (cursor) filename = IMAGES_DIR + color.toString().toLowerCase() + CURSOR_SUFFIX + IMAGE_FORMAT;
        else filename = IMAGES_DIR + color.toString().toLowerCase() + IMAGE_FORMAT;
        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.out.println("Caught an exception while trying read image file " + filename);
            e.printStackTrace();
        }
        repaint();
    }
    
    public void setColor(Color color, boolean faded, boolean cursor) {
        String filename;
        if (faded) filename = IMAGES_DIR + color.toString().toLowerCase() + FADED_SUFFIX + IMAGE_FORMAT;
        else if (cursor) filename = IMAGES_DIR + color.toString().toLowerCase() + CURSOR_SUFFIX + IMAGE_FORMAT;
        else filename = IMAGES_DIR + color.toString().toLowerCase() + IMAGE_FORMAT;
        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.out.println("Caught an exception while trying read image file " + filename);
            e.printStackTrace();
        }
        repaint();
    }
    
    public void setColor(Color color, boolean faded) {
        setColor(color, faded, false);
    }
    
    public void setColor(Color color) {
        setColor(color, false, false);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(image, 0, 0, this);
    }

}
