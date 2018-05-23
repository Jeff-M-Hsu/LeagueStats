package com.league.stats.gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableFooter;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeader;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
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
		
		HtmlAnchor liveGameAnchor = resultPage.getFirstByXPath("//a[@class='SpectateTabButton']");
		HtmlPage liveGame = liveGameAnchor.click();
		try {
			Thread.sleep(10000);
		
		FileWriter fstream = new FileWriter("index.txt");
		BufferedWriter out = new BufferedWriter(fstream);
		List<HtmlElement> names = liveGame.getByXPath("//a[@class='SummonerName']");
		List<HtmlElement> ranks = liveGame.getByXPath("//td[@class='CurrentSeasonTierRank Cell']/div[@class='TierRank']");
		List<HtmlElement> ratio = liveGame.getByXPath("//td[@class='RankedWinRatio Cell']/span");
		for(int i = 0; i < 10; i++) {
			System.out.println(names.get(i).asText());
			System.out.println(ranks.get(i).asText());
			System.out.println(ratio.get(i).asText()+"\n");
			out.write(names.get(i).asText()+"\n");
			out.write(ranks.get(i).asText()+"\n");
			out.write(ratio.get(i).asText()+"\n\n");
		}
		out.close();


		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Prints content of resultant page
		/*System.out.println(level);
		System.out.println(league);
		System.out.println(lastSeasonRank);*/
		client.close();
	}
	
}
