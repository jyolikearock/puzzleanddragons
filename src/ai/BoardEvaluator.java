package ai;

import game.Board;
import game.Color;
import game.Match;
import game.MatchFinder;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import leaderskill.LeaderSkill;

public class BoardEvaluator {
	
	private LeaderSkill leaderSkill1;
	private LeaderSkill leaderSkill2;
	
	private Set<Match> matches;
	private Set<Match> allMatches;
	private Map<Color, Double> damageReport;
	private MatchFinder matchFinder;
	
	public BoardEvaluator() {
		leaderSkill1 = Configurations.getLeaderSkill(1);
		leaderSkill2 = Configurations.getLeaderSkill(2);
		matchFinder = new MatchFinder();
	}
	
	public double evaluate(Board board) {
		allMatches = getAllMatches(board);
		return evaluateMatches(allMatches);
	}
	
	private Set<Match> getAllMatches(Board board) {
		Set<Match> allMatches = new LinkedHashSet<Match>();
		
		Board boardCopy = new Board(board);
		while ((matches = matchFinder.findMatches(boardCopy)).size() != 0) {
			for (Match match : matches) {
				allMatches.add(match);
			}
			boardCopy.clear(matches);
			boardCopy.fall();
		}
		
		return allMatches;
	}
	
	private double evaluateMatches(Set<Match> matches) {
		damageReport = new HashMap<Color, Double>();
		if (matches.size() < Configurations.getMinCombos()) return 0.0;
		
		Map<Color, Integer> rowEnhances = new HashMap<Color, Integer>();
		Color color;
		for (Match match : matches) {
			color = match.getColor();
			if (match.isRow() && Configurations.containsKey(color.toString() + Configurations.ROW_ENHANCE_KEY)) {
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
				double rowEnhanceMultiplier = 
						1 + (rowEnhances.get(color) * Configurations.getNumRowEnhances(color) * 0.1);
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
		allMatches = getAllMatches(board);
		return allMatches.size();
	}
	
	public double evaluateMatch(Match match) {
		Color color = match.getColor();		
		double score = 0.0;
		double baseScore = 0.0;
		baseScore = Configurations.getAttributeValue(color);
		score = baseScore * (1 + (match.size() - 3) * 0.25);
		if (match.size() == 4) {
			double tpaMultiplier = Configurations.getTPAMultiplier(color);
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
	
	public boolean procsLeaderSkills(Board board) {
		return procsLeaderSkill(board, 1) && procsLeaderSkill(board, 2);
	}
	
	private boolean procsLeaderSkill(Board board, int leaderSkillIndex) {
		allMatches = getAllMatches(board);
		LeaderSkill leaderSkill;
		if (leaderSkillIndex == 1)
			leaderSkill = leaderSkill1;
		else if (leaderSkillIndex == 2)
			leaderSkill = leaderSkill2;
		else
			return false;
		
		if (leaderSkill.getRequiredColors().isEmpty())
			return true;
		
		for (Color color : leaderSkill.getRequiredColors()) {
			return leaderSkill.getMultiplier(allMatches, color) > 1.0;
		}
		return false;
	}
	
	public List<Color> predictBestCursorColors(Board board) {
		LinkedList<Color> bestColors = new LinkedList<Color>();
		
		int maxDist = -1;
		Set<Color> colorsToMatch = leaderSkill1.getRequiredColors();
		colorsToMatch.addAll(leaderSkill2.getRequiredColors());
		if (colorsToMatch == null || colorsToMatch.isEmpty()) {
			colorsToMatch.add(Color.R);
			colorsToMatch.add(Color.G);
			colorsToMatch.add(Color.B);
			colorsToMatch.add(Color.L);
			colorsToMatch.add(Color.D);
			colorsToMatch.add(Color.H);
			colorsToMatch.add(Color.J);
			colorsToMatch.add(Color.P);
		}
		for (Color color : colorsToMatch) {
			int dist = findThreeClosestOrbs(board, color);
			if (bestColors.size() < 2) {
				bestColors.add(color);
				if (dist >= maxDist)
					maxDist = dist;
			} else if (dist >= maxDist) {
				bestColors.add(color);
				bestColors.poll();
				maxDist = dist;
			}
		}
		
		return bestColors;
	}
	
	public int findThreeClosestOrbs(Board board, Color color) {
		int secondClosestDist = Board.NUM_COLS;
		
		int firstClosestCol = -1;
		int secondClosestCol = -1;
		for (int col = 0; col < Board.NUM_COLS; col++) {
			for (int row = 0; row < Board.NUM_ROWS; row++) {
				Color c = board.get(row, col).getColor();
				if (!c.equals(color))
					continue;
				if (firstClosestCol == -1) {
					firstClosestCol = col;
				} else if (secondClosestCol == -1) {
					secondClosestCol = col;
				} else {
					int dist1 = col - firstClosestCol;
					int dist2 = firstClosestCol - secondClosestCol;
					int longer = Math.max(dist1, dist2);
					if (longer < secondClosestDist)
						secondClosestDist = longer;
					secondClosestCol = firstClosestCol;
					firstClosestCol = col;
				}
				
			}
		}
		
		return secondClosestDist == Board.NUM_COLS ? -1 : secondClosestDist;
	}
}
