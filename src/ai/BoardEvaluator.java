package ai;

import game.Board;
import game.Color;
import game.Match;
import game.MatchFinder;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import leaderskill.LeaderSkill;

public class BoardEvaluator {
	
	private Configurations configurations;
	private LeaderSkill leaderSkill1;
	private LeaderSkill leaderSkill2;
	
	private Board boardCopy;
	private Set<Match> matches;
	private Set<Match> allMatches;
	private Map<Color, Double> damageReport;
	private MatchFinder matchFinder;
	
	public BoardEvaluator(Configurations configurations) {
		this.configurations = configurations;
		leaderSkill1 = configurations.getLeaderSkill(1);
		leaderSkill2 = configurations.getLeaderSkill(2);
		matchFinder = new MatchFinder();
	}
	
	public double evaluate(Board board) {
		boardCopy = new Board(board);
		allMatches = new HashSet<Match>();
		while ((matches = matchFinder.findMatches(boardCopy)).size() != 0) {
			for (Match match : matches) {
				allMatches.add(match);
			}
			boardCopy.clear(matches);
			boardCopy.fall();
		}
		return evaluateMatches(allMatches);
	}
	
	private double evaluateMatches(Set<Match> matches) {
		damageReport = new HashMap<Color, Double>();
		if (matches.size() < configurations.getMinCombos()) return 0.0;
		
		Map<Color, Integer> rowEnhances = new HashMap<Color, Integer>();
		Color color;
		for (Match match : matches) {
			color = match.getColor();
			if (match.isRow() && configurations.containsKey(color.toString() + Configurations.ROW_ENHANCE_KEY)) {
				if (rowEnhances.containsKey(color)) {
					int numRowEnhances = rowEnhances.get(color).intValue();
					rowEnhances.put(color, new Integer(numRowEnhances + 1));
				} else {
					rowEnhances.put(color, new Integer(1));
				}
			}
		}

		int combos = matches.size();
		for (Match match : matches) {
			color = match.getColor();
			double score;
			if (rowEnhances.containsKey(color)) {
				double rowEnhanceMultiplier = 1 + (rowEnhances.get(color) * configurations.getNumRowEnhances(color) * 0.1);
				score = rowEnhanceMultiplier * evaluateMatch(match);
			} else {
				score = evaluateMatch(match);
			}
			if (damageReport.containsKey(color)) {
				double currentScore = damageReport.get(color).doubleValue();
				damageReport.put(color, new Double(currentScore + score));
			} else {
				damageReport.put(color, new Double(score));
			}
		}

		double totalScore = 0.0;
		double multiplier1, multiplier2;
		for (Color key : damageReport.keySet()) {
			multiplier1 = leaderSkill1.getMultiplier(matches, key);
			multiplier2 = leaderSkill2.getMultiplier(matches, key);
			double currentScore = damageReport.get(key).doubleValue();
			currentScore *= multiplier1 * multiplier2 * (1 + 0.25 * (combos - 1));
			damageReport.put(key, new Double(currentScore));
			if (!key.equals(Color.H)) totalScore += currentScore;
		}
		return totalScore;
	}
	
	public int getNumCombos(Board board) {
		boardCopy = new Board(board);
		allMatches = new HashSet<Match>();
		while ((matches = matchFinder.findMatches(boardCopy)).size() != 0) {
			for (Match match : matches) {
				allMatches.add(match);
			}
			boardCopy.clear(matches);
			boardCopy.fall();
		}
		return allMatches.size();
	}
	
	public double evaluateMatch(Match match) {
		Color color = match.getColor();		
		double score = 0.0;
		double baseScore = 0.0;
		baseScore = configurations.getAttributeValue(color);
		score = baseScore * (1 + (match.size() - 3) * 0.25);
		if (match.size() == 4) {
			double tpaMultiplier = configurations.getTPAMultiplier(color);
			score *= tpaMultiplier;
		}
		return score;
	}
	
	public String getDamageReport() {
		StringBuilder builder = new StringBuilder();
		builder.append("Matches made\n");
		for (Match match : allMatches) {
			builder.append(String.format("%s\n", match));
		}
		builder.append("\nTotal damage\n");
		for (Color color : damageReport.keySet()) {
			builder.append(String.format("%s: %.2f\n", color, damageReport.get(color)));
		}
		return builder.toString();
	}
	
	public boolean canProcLeaderSkills(Board board, int numRemainingMoves) {
		return canProcLeaderSkill(board, numRemainingMoves, leaderSkill1) 
				&& canProcLeaderSkill(board, numRemainingMoves, leaderSkill2);
	}
	
	private boolean canProcLeaderSkill(Board board, int numRemainingMoves, LeaderSkill leaderSkill) {
		Set<Color> requiredColors = leaderSkill.getRequiredColors();
		if (requiredColors == null || requiredColors.isEmpty()) {
			return true;
		}
		
		Map<Color, List<Point>> coordinatesByColor = getCoordinatesByColor(board);
		for (Color color : requiredColors) {
			if (!coordinatesByColor.containsKey(color)) {
				return false;
			} else {
				int minDistance = getMinDistance(coordinatesByColor.get(color));
				if (minDistance - 1 > numRemainingMoves) {
					return false;
				}
			}
		}
		return true;
	}
	
	private Map<Color, List<Point>> getCoordinatesByColor(Board board) {
		Map<Color, List<Point>> coordinatesByColor = new HashMap<Color, List<Point>>();
		
		Color color;
		for (int r = 0; r < Board.NUM_ROWS; r++) {
			for (int c = 0; c < Board.NUM_COLS; c++) {
				color = board.get(r, c).getColor();
				if (coordinatesByColor.containsKey(color)) {
					coordinatesByColor.get(color).add(new Point(r, c));
				} else {
					List<Point> coordinates = new ArrayList<Point>();
					coordinates.add(new Point(r, c));
					coordinatesByColor.put(color, coordinates);
				}
			}
		}
		
		return coordinatesByColor;
	}
	
	private int getMinDistance(List<Point> points) {
		int minDistance = Integer.MAX_VALUE;
		
		Point p1, p2;
		int distance;
		for (int i = 0; i < points.size() - 1; i ++) {
			for (int j = i + 1; j < points.size(); j++) {
				p1 = points.get(i);
				p2 = points.get(j);
				distance = getEuclideanDistance(p1, p2);
				if (distance == 1) {
					return 1;
				} else if (distance < minDistance) {
					minDistance = distance;
				}
			}
		}
		
		return minDistance;
	}
	
	private int getEuclideanDistance(Point p1, Point p2) {
		return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
	}
}
