package com.league.stats.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;

public class SearchForm {
	private String name;
	
	public SearchForm(String searchQuery) {
		name = searchQuery;
	}
	

	public void SummonerSearch(WebClient client) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		//Opens home page
		HtmlPage page = client.getPage("http://na.op.gg/");
		
		//Gets element for search bar
		HtmlForm searchBar = page.getFirstByXPath("//form[contains(@class, 'summoner-search-form')]");
		
		//Enters specified name
		searchBar.getInputByName("userName").setValueAttribute(name);
		
		//Create virtual button and click
		HtmlButton submitButton = (HtmlButton)page.createElement("button");
		submitButton.setAttribute("type", "submit");
		searchBar.appendChild(submitButton);
		HtmlPage resultPage = submitButton.click();
		
		HtmlButton updateProfile = (HtmlButton)resultPage.getElementById("SummonerRefreshButton");
		updateProfile.click();
		
		//Gets level of summoner
		DomElement levelElement = resultPage.getFirstByXPath("//div[@class='ProfileIcon']");
		String level = levelElement.getLastElementChild().asText();
		
		//Gets league of summoner
		DomElement leagueElement = resultPage.getFirstByXPath("//span[@class='tierRank']");
		String league = leagueElement.getTextContent();
		
		//Gets league of summoner last season
		DomElement pastLeagueElement = resultPage.getFirstByXPath("//ul[@class='PastRankList']");
		String lastSeasonRank = "";
		if(pastLeagueElement != null) {
			lastSeasonRank = pastLeagueElement.getLastElementChild().asText();
		}
		
		//Prints content of resultant page
		System.out.println(level);
		System.out.println(league);
		System.out.println(lastSeasonRank);
		client.close();
	}
	
}
