package com.league.stats.gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;

public class SearchForm {
	private String name;
	private HtmlPage profilePage;
	private HtmlPage livePage;
	private HtmlPage championPage;

	public SearchForm(String searchQuery) {
		name = searchQuery;
	}

	public void summonerSearch(WebClient client) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
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

		//Updates profile with the Update button for latest stats
		HtmlButton updateProfile = (HtmlButton)resultPage.getElementById("SummonerRefreshButton");
		updateProfile.click();
		profilePage = resultPage;

		//Fixes capitalization issues if there are any
		DomElement summonerName = resultPage.getFirstByXPath("//span[@class='Name']");
		name = summonerName.asText();

		//Gets level of summoner
		DomElement levelElement = resultPage.getFirstByXPath("//div[@class='ProfileIcon']");
		String level = levelElement.getLastElementChild().asText();

		//Gets league of summoner
		DomElement leagueElement = resultPage.getFirstByXPath("//span[@class='tierRank']");
		DomElement leagueLP = resultPage.getFirstByXPath("//span[@class='LeaguePoints']");
		String league = leagueElement.getTextContent();
		String lp = "";
		if(!league.equals("Unranked")) {
			lp = " (" + leagueLP.getTextContent().replaceAll("[\\n\\t]+", "") + ")";
		}
		//Gets league of summoner last time they placed in a season
		DomElement pastLeagueElement = resultPage.getFirstByXPath("//ul[@class='PastRankList']");
		String lastSeasonRank = "";
		if(pastLeagueElement != null) {
			lastSeasonRank = pastLeagueElement.getLastElementChild().asText();
		}

		//Prints content of resultant page
		System.out.println("Name: " + name);
		System.out.println("Level: " + level);
		System.out.println("Current Rank: " + league + lp);
		System.out.println("Previous Placement: " + lastSeasonRank + "\n");
	}

	public void liveGameSearch(WebClient client) throws IOException {
		try {
			//Clicks the "Live Game" button
			HtmlAnchor liveGameAnchor = profilePage.getFirstByXPath("//a[@class='SpectateTabButton']");
			HtmlPage liveGame = liveGameAnchor.click();
			livePage = liveGame;

			//Wait for javascript to load on page
			Thread.sleep(10000);

			//Write result to a file for easier manipulation, will probably store in a data structure later on
			FileWriter fstream = new FileWriter("index.txt");
			BufferedWriter out = new BufferedWriter(fstream);

			//Gets names, ranks, W/L ratio of all players in the game
			List<HtmlElement> names = liveGame.getByXPath("//a[@class='SummonerName']");
			List<HtmlElement> ranks = liveGame.getByXPath("//td[@class='CurrentSeasonTierRank Cell']/div[@class='TierRank']");
			List<HtmlElement> ratio = liveGame.getByXPath("//td[@class='RankedWinRatio Cell']");


			//Prints result of above lists
			for(int i = 0; i < 10; i++) {
				System.out.println("Name: " + names.get(i).asText());
				if(ranks.get(i).asText().contains("Level")) {
					System.out.println("Rank: Unranked (" + ranks.get(i).asText() + ")");
					out.write("Unranked (" + ranks.get(i).asText() + ")");
				}
				else {
					System.out.println("Rank: " + ranks.get(i).asText());
					out.write(ranks.get(i).asText()+"\n");
				}
				System.out.println("Ranked W/L Ratio: " + ratio.get(i).asText() + "\n");
				out.write(names.get(i).asText()+"\n");
				out.write(ratio.get(i).asText()+"\n");
			}
			out.close();


		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void championAutoSearch(WebClient client) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		try {
		//Detects the champion you are using and looks up its op.gg page
		HtmlTableRow select = livePage.getFirstByXPath("//tr[td/a[text()='"+name+"']]");
		HtmlAnchor championSelected= (HtmlAnchor) select.getFirstElementChild().getFirstElementChild();
		championPage = client.getPage("http://na.op.gg"+championSelected.getAttribute("href"));
		System.out.println(championPage.asText());
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}