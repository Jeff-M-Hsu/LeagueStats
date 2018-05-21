package com.league.stats.gui;


import java.io.IOException;
import java.net.MalformedURLException;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;;

public class Main {
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		try {
			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
			WebClient client = new WebClient(BrowserVersion.CHROME);
			client.getOptions().setJavaScriptEnabled(false);
			HtmlPage page = client.getPage("http://na.op.gg/");
			String content = page.asText();
			System.out.println(content);
			client.close();
		}
		catch (FailingHttpStatusCodeException e){
			System.out.println("Server returned a failing status code, please try again");
		}
		catch (MalformedURLException e) {
			System.out.println("Malformed URL, please enter a valid URL");
		}
		catch (IOException e) {
			System.out.println("IOException occurred, please try again");
		}
	}
}
