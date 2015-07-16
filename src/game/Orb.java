package game;

public class Orb {
	
	private Color color;
	int row;
	int col;
	private boolean isCursor;
	
	public Orb(Color color) {
		this.color = color;
		this.isCursor = false;
	}
	
	public Orb(Color color, int row, int col) {
		this.color = color;
		this.row = row;
		this.col = col;
		this.isCursor = false;
	}
	
	public Orb(Orb orb) {
		this.color = orb.getColor();
		this.row = orb.getRow();
		this.col = orb.getCol();
		this.isCursor = orb.isCursor();
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void move(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public boolean isCursor() {
		return isCursor;
	}
	
	public void setCursor() {
		isCursor = true;
	}
	
	public String toString() {
		return isCursor ? ">" + color + "<" : "(" + color + ")";
	}

}
