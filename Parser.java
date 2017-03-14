import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.*;


public class Parser {
	
	String webPageString;
	File webPage;
	Word [] WordsArray;
	
	 Parser (String s, File f) throws IOException
	 {
		 webPage = f;
		 webPageString = readFile (s);
	 }
	 
	 void removeTagsAndSetImportance () throws IOException
	 {
		 int i=0;
		 WordsArray = new Word [webPageString.length()];
		 Document doc2 = Jsoup.parse(webPageString);
		 

		/* NodeTraversor traversor  = new NodeTraversor(new NodeVisitor() {	 
		   @Override
		   public void tail(Node node, int depth) {
		     
		   }

		   @Override
		   public void head(Node node, int depth) {        
		   }
		 });

		 traversor.traverse(doc2.body());
		 String modifiedHtml = doc2.toString();
		 
		 */
		 
		 
		 if (webPageString.contains("<title>"))
		 {
			String title = StringUtils.substringBetween(webPageString, "<title", "</title>");
			title = title.trim();
			Document doc = Jsoup.parse(title);
			title = doc.text();
			String[] arr = title.split(" ");
			
			for (String s : arr)
			{
				Word W = new Word();
				W.Name = s;
				W.Importance = 'T';
				WordsArray[i] = W;
				i++;
			}
			
			webPageString = StringUtils.remove(webPageString, title);
		 }
		 
		 while (true)
		 {
			 if (webPageString.contains("<header>"))
			 {
				String header = StringUtils.substringBetween(webPageString, "<header>", "</header>");
				header = header.trim();
				Document doc = Jsoup.parse(header);
				header = doc.text();
				String[] arr = header.split(" ");
				
				for (String s : arr)
				{
					Word W = new Word();
					W.Name = s;
					W.Importance = 'H';
					WordsArray[i] = W;
					i++;
				}
				webPageString = StringUtils.remove(webPageString, header);
				webPageString = StringUtils.remove(webPageString, "<header>");
				webPageString = StringUtils.remove(webPageString, "</header>");
				
			 }
			 if (!webPageString.contains("<header>"))
					break;
		 }
		
		 while (true)
		 {
			 if (webPageString.contains("<h1>"))
			 {
				String h1 = StringUtils.substringBetween(webPageString, "<h1>", "</h1>");
				h1 = h1.trim();
				Document doc = Jsoup.parse(h1);
				h1 = doc.text();
				String[] arr = h1.split(" ");
				
				for (String s : arr)
				{
					Word W = new Word();
					W.Name = s;
					W.Importance = 'H';
					WordsArray[i] = W;
					i++;
				}
				
				webPageString = StringUtils.remove(webPageString, h1);
				webPageString = webPageString.replaceFirst(Pattern.quote("<h1>"), "");
				webPageString = webPageString.replaceFirst(Pattern.quote("</h1>"), "");
				
			 }
			 if (!webPageString.contains("<h1>"))
					break;
		 }
		 
		 while (true)
		 {
			 if (webPageString.contains("<h2>"))
			 {
				String h2 = StringUtils.substringBetween(webPageString, "<h2>", "</h2>");
				h2 = h2.trim();
				Document doc = Jsoup.parse(h2);
				h2 = doc.text();
				String[] arr = h2.split(" ");
				
				for (String s : arr)
				{
					Word W = new Word();
					W.Name = s;
					W.Importance = 'H';
					WordsArray[i] = W;
					i++;
				}
				
				webPageString = StringUtils.remove(webPageString, h2);
				webPageString = webPageString.replaceFirst(Pattern.quote("<h2>"), "");
				webPageString = webPageString.replaceFirst(Pattern.quote("</h2>"), "");
				
			 }
			 if (!webPageString.contains("<h2>"))
				  break;
		 }
		 
		 while (true)
		 {
			 if (webPageString.contains("<h3>"))
			 {
				String h3 = StringUtils.substringBetween(webPageString, "<h3>", "</h3>");
				h3 = h3.trim();
				Document doc = Jsoup.parse(h3);
				h3 = doc.text();
				String[] arr = h3.split(" ");
				
				for (String s : arr)
				{
					Word W = new Word();
					W.Name = s;
					W.Importance = 'H';
					WordsArray[i] = W;
					i++;
				}
				
				webPageString = StringUtils.remove(webPageString, h3);
				webPageString = webPageString.replaceFirst(Pattern.quote("<h3>"), "");
				webPageString = webPageString.replaceFirst(Pattern.quote("</h3>"), "");
			 }
			 if (!webPageString.contains("<h3>"))
				  break;
		 }
		 
		 while (true)
		 {
			 if (webPageString.contains("<h4>"))
			 {
				String h4 = StringUtils.substringBetween(webPageString, "<h4>", "</h4>");
				h4 = h4.trim();
				Document doc = Jsoup.parse(h4);
				h4 = doc.text();
				String[] arr = h4.split(" ");
				
				for (String s : arr)
				{
					Word W = new Word();
					W.Name = s;
					W.Importance = 'H';
					WordsArray[i] = W;
					i++;
				}
				
				webPageString = StringUtils.remove(webPageString, h4);
				webPageString = webPageString.replaceFirst(Pattern.quote("<h4>"), "");
				webPageString = webPageString.replaceFirst(Pattern.quote("</h4>"), "");
			 }
			 if (!webPageString.contains("<h4>"))
				  break;
		 }
		 
		 while (true)
		 {
			 if (webPageString.contains("<h5>"))
			 {
				String h5 = StringUtils.substringBetween(webPageString, "<h5>", "</h5>");
				h5 = h5.trim();
				Document doc = Jsoup.parse(h5);
				h5 = doc.text();
				String[] arr = h5.split(" ");
				
				for (String s : arr)
				{
					Word W = new Word();
					W.Name = s;
					W.Importance = 'H';
					WordsArray[i] = W;
					i++;
				}
				
				webPageString = StringUtils.remove(webPageString, h5);
				webPageString = webPageString.replaceFirst(Pattern.quote("<h5>"), "");
				webPageString = webPageString.replaceFirst(Pattern.quote("</h5>"), "");
			 }
			 if (!webPageString.contains("<h5>"))
				  break;
		 }
		 
		 while (true)
		 {
			 if (webPageString.contains("<h6>"))
			 {
				String h6 = StringUtils.substringBetween(webPageString, "<h6>", "</h6>");
				h6 = h6.trim();
				Document doc = Jsoup.parse(h6);
				h6 = doc.text();
				String[] arr = h6.split(" ");
				
				for (String s : arr)
				{
					Word W = new Word();
					W.Name = s;
					W.Importance = 'H';
					WordsArray[i] = W;
					i++;
				}
				
				webPageString = StringUtils.remove(webPageString, h6);
				webPageString = webPageString.replaceFirst(Pattern.quote("<h6>"), "");
				webPageString = webPageString.replaceFirst(Pattern.quote("</h6>"), "");
			 }
			 if (!webPageString.contains("<h6>"))
				  break;
		 }
		 
		 
		 Document doc = Jsoup.parse(webPageString);
		 doc.select("script, style, .hidden, label").remove();
		 webPageString = doc.text();
		 
		
		String S = doc.text();
		String[] arr = S.split(" ");
		 
		 for (String s : arr)
		 {
			Word W = new Word();
			W.Name = s;
			W.Importance = 'X';
			WordsArray[i] = W;
			i++;
		 }
		 
		 
		 for (int m=0; m<i; m++)
		 {
			 System.out.println (WordsArray[m].Name + "  " + WordsArray[m].Importance);
		 }
		 
	 }
	 
	 private String readFile(String file) throws IOException {
		    BufferedReader reader = new BufferedReader(new FileReader (file));
		    String         line = null;
		    StringBuilder  stringBuilder = new StringBuilder();
		    String         ls = System.getProperty("line.separator");

		    try {
		        while((line = reader.readLine()) != null) {
		            stringBuilder.append(line);
		            stringBuilder.append(ls);
		        }

		        return stringBuilder.toString();
		    } finally {
		        reader.close();
		    }
		}
}
