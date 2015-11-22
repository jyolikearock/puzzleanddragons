package ui;

import game.Board;
import game.Color;
import game.Move;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import ai.BoardData;
import ai.Configurations;
import util.StringToColorConverter;

@SuppressWarnings("serial")
public class BoardDisplayPanel extends JPanel implements ActionListener {

    private static final int DELAY = 200;
    private static final int SET_CURSOR_FRAME = -1;
    private static final int STOP_ANIMATION_FRAME = -2;
    private static final int ORB_WIDTH = 100;
    
    private Timer timer;
    private int animationFrame;
    private Board board;
    private List<Move> moveset;
    private int cursorRow, cursorCol;
    
    public BoardDisplayPanel() {
        super(new GridLayout(Board.NUM_ROWS, Board.NUM_COLS));
        UIMaster.setBoardDisplayPanel(this);
        this.setPreferredSize(new Dimension(Board.NUM_COLS * ORB_WIDTH, Board.NUM_ROWS * ORB_WIDTH));
        Board board = Configurations.getBoard();
        drawBoard(board.toCode());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Move move;
        Color color;
        if (animationFrame == STOP_ANIMATION_FRAME || animationFrame >= moveset.size()) {
            timer.stop();
            timer = null;
            return;
        } else if (animationFrame == SET_CURSOR_FRAME) {
            color = board.get(cursorRow, cursorCol).getColor();
            setCursor(cursorRow, cursorCol, color);
            animationFrame++;
            timer.setDelay(DELAY);
        } else {
            move = moveset.get(animationFrame);
            board.move(move);
            color = board.get(cursorRow, cursorCol).getColor();
            setFaded(cursorRow, cursorCol, color);
            
            if (move.equals(Move.UP)) cursorRow--;
            else if (move.equals(Move.DOWN)) cursorRow++;
            else if (move.equals(Move.LEFT)) cursorCol--;
            else if (move.equals(Move.RIGHT)) cursorCol++;
            
            color = board.get(cursorRow, cursorCol).getColor();
            setCursor(cursorRow, cursorCol, color);
            animationFrame++;
        }
    }
    
    public void drawBoard(String boardText) {
        String color;
        String[] split = boardText.split("");
        for (int r = 0; r < Board.NUM_ROWS; r++) {
            for (int c = 0; c < Board.NUM_COLS; c++) {
                color = split[r * Board.NUM_COLS + c];
                set(r, c, StringToColorConverter.getColor(color));
            }
        }
    }
    
    public void execute(BoardData data) {
        int cursorRow = data.getBoard().getOriginalCursorRow();
        int cursorCol = data.getBoard().getOriginalCursorCol();
        Board board = Configurations.getBoard();
        board.setCursor(cursorRow, cursorCol);
        execute(board, data.getMoveset());
    }
    
    private void execute(Board board, List<Move> moveset) {
        drawBoard(board.toCode());
        
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        timer = new Timer(5 * DELAY, this);
        animationFrame = SET_CURSOR_FRAME;
        this.board = new Board(board);
        this.moveset = moveset;
        this.cursorRow = board.getOriginalCursorRow();
        this.cursorCol = board.getOriginalCursorCol();
        timer.start();
    }
    
    private void set(int row, int col, Color color) {
        Component[] components = getComponents();
        if (components.length != Board.NUM_ROWS * Board.NUM_COLS) {
            OrbPanel orb = new OrbPanel(color, false, false);
            this.add(orb);
        } else {
            OrbPanel orb = (OrbPanel) components[row * Board.NUM_COLS + col];
            orb.setColor(color);
        }
    }
    
    private void setFaded(int row, int col, Color color) {
        OrbPanel orb = (OrbPanel) getComponent(row * Board.NUM_COLS + col);
        orb.setColor(color, true);
    }
    
    private void setCursor(int row, int col, Color color) {
        OrbPanel orb = (OrbPanel) getComponent(row * Board.NUM_COLS + col);
        orb.setColor(color, false, true);
    }

}
