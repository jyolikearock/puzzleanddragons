package util;

import ai.Agent;

public class Report {
	
	private int numReports;
	
	private double score;
	private int numMoves;
	private long time;

	public Report() {
		numReports = 0;
	}
	
	public void aggregate(Agent agent) {
		numReports++;
		score += agent.getBestBoardData().getScore();
		numMoves += agent.getBestBoardData().getMoveset().size();
		time += agent.getTime();
	}

	public double getScore() {
		return score / numReports;
	}

	public double getNumMoves() {
		return numMoves / numReports;
	}

	public double getTime() {
		return time / numReports;
	}
	
}
