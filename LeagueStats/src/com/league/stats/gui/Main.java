package com.league.stats.gui;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

public class Main {
	public static void main(String[] args){
			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
			WebClient client = new WebClient(BrowserVersion.CHROME);
			client.getOptions().setJavaScriptEnabled(true);
			SearchForm search = new SearchForm("roflmfaolol");
			search.summonerSearch(client);
			search.getProfileInfo();
			search.liveGameSearch();
			search.championAutoSearch(client);
			client.close();
	}
}
