package com.league.stats.gui;

public class Summoner {
	String name;
	String rank;
	String ratio;

	public Summoner(String summonerName, String summonerRank, String summonerRatio) {
		name = summonerName;
		rank = summonerRank;
		ratio = summonerRatio;
	}

	String getName() {
		return name;
	}

	String getRank() {
		return rank;
	}

	String getRatio() {
		return ratio;
	}
}
