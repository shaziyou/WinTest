package com.win.test.pc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TestMain {

	public static void main(String[] args) throws IOException {
		String baseDir = "D:/img/";
		WebClient wc = new WebClient();
		wc.getOptions().setJavaScriptEnabled(true);
		wc.getOptions().setCssEnabled(false);
		wc.getOptions().setThrowExceptionOnScriptError(false);
		HtmlPage page = wc.getPage("https://xingyan.panda.tv/?channel=web_xy-cate_xingyan-tj_allcate");
		String html = page.asXml();
		Document document = Jsoup.parse(html, "https://xingyan.panda.tv/?channel=web_xy-cate_xingyan-tj_allcate");
		Elements elements = document.select("li.list-item img");
		for (Element element : elements) {
			String urlStr = element.attr("src");
			String name = urlStr.substring(29, 66);
			URL url = new URL("http:"+urlStr);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(baseDir+name);
			byte[] buffer = new byte[502];
			int num = -1;
			while(-1!=(num = is.read(buffer))){
				os.write(buffer, 0, num);
				os.flush();
			}
			os.close();
			is.close();
		}
	}

}
