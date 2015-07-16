package util;

public class RGB {
	
	int red;
	int green;
	int blue;
	
	public RGB(int r, int g, int b) {
		red = r;
		green = g;
		blue = b;
	}
	
	public int getRed() {
		return red;
	}
	
	public int getBlue() {
		return blue;
	}
	
	public int getGreen() {
		return green;
	}
	
	public String toString() {
		return red + "," + green + "," + blue;
	}
	
//	public int hashCode() {
//		return red * 1000000 + green * 1000 + blue;
//	}

}
