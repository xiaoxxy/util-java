package tools.jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import tools.log.Log;

public class JsoupHelper {
	
	public static String getBody(String url){
		Document doc =null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			Log.info("url is not working... ");
		}
		if (null == doc)
			return null;
		return doc.body().toString();
	}
	
	public static String getHtml(String url) {
		StringBuffer sb = new StringBuffer("");
		URL Url;
		try {
			Url = new URL(url);
			BufferedReader br = new BufferedReader(new InputStreamReader(Url.openStream(), "utf-8"));
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s + "\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
