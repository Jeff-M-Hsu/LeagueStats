package com.league.stats.gui;


import java.io.IOException;
import java.net.MalformedURLException;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;

public class Main {
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
			WebClient client = new WebClient(BrowserVersion.CHROME);
			client.getOptions().setJavaScriptEnabled(true);
			SearchForm search = new SearchForm("rainbrain");
			search.SummonerSearch(client);
	}
}
