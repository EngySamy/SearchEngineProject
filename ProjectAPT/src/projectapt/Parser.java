/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectapt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.apache.commons.lang3.*;


public class Parser {
	
	String webPageString;
	String webPageString_Temp;

	Map <String, Word> WordsInfo = new HashMap();

	
	 Parser (String s, Map <String, Word> WI) throws IOException
	 {
                 WordsInfo = WI;
		 webPageString = s;
	 }
	 
	 void removeTagsAndSetImportance () throws IOException
	 {
                webPageString = webPageString.replaceAll("[^\\x00-\\x7F]", "");
                webPageString_Temp = webPageString;

	
                Document doc = Jsoup.parse(webPageString);

                String title = doc.select("title").html();
                title = title.trim();
                if (!title.equals(""))
                {
                        Document docT = Jsoup.parse(title);
                        title = docT.text();
                        String[] arrT = title.split(" ");

                   for (String s : arrT)
                   {
                        s = s.trim();
                        if (WordsInfo.containsKey(s))
                        {
                                Word W = (Word) WordsInfo.get(s);
                                W.Count++;
                                W.Importance = 'T';
                        }
                        else
                        {
                                Word W = new Word();
                                W.Importance = 'T';
                                W.Count++; 
                                WordsInfo.put(s, W);
                        }
                   }
                   doc.select("title").remove();
                }


                String header = doc.select("header").html();
                header = header.trim();
                if (!header.equals(""))
                {
                    Document docH = Jsoup.parse(header);
                    header = docH.text();
                    String[] arrH = header.split(" ");

                    for (String s : arrH)
                    {	
                            s = s.trim();
                            if (WordsInfo.containsKey(s))
                            {
                                    Word W = (Word) WordsInfo.get(s);
                                    W.Count++;
                                    if (W.Importance != 'T')
                                      W.Importance = 'H';
                            }
                            else
                            {
                                    Word W = new Word();
                                    W.Importance = 'H';
                                    W.Count++; 
                                    WordsInfo.put(s, W);
                            }
                    }
                    doc.select("header").remove();
                }

                String hTags = doc.select("h1, h2, h3, h4, h5, h6").html();
                hTags = hTags.trim();
                if (!hTags.equals(""))
                {
                    Document dochTags = Jsoup.parse(hTags);
                    hTags = dochTags.text();
                    String[] arrhTags = hTags.split(" ");

                    for (String s : arrhTags)
                    {
                            s = s.trim();
                            if (WordsInfo.containsKey(s))
                            {
                                    Word W = (Word) WordsInfo.get(s);
                                    W.Count++;
                                    if (W.Importance != 'T')
                                       W.Importance = 'H';
                            }
                            else
                            {
                                    Word W = new Word();
                                    W.Importance = 'H';
                                    W.Count++; 
                                    WordsInfo.put(s, W);
                            }
                    }

                    doc.select("h1, h2, h3, h4, h5, h6").remove();
                }

                String ImgAlt = doc.select("img").attr("alt");
                ImgAlt = ImgAlt.trim();
                if (!ImgAlt.equals(""))
                {
                    Document docI = Jsoup.parse(ImgAlt);
                    ImgAlt = docI.text();
                    String[] arrI = ImgAlt.split(" ");

                    for (String s : arrI)
                    {
                            s = s.trim();
                            if (WordsInfo.containsKey(s))
                            {
                                    Word W = (Word) WordsInfo.get(s);
                                    W.Count++;
                                    if (W.Importance != 'T' && W.Importance != 'H')
                                       W.Importance = 'I';
                            }
                            else
                            {
                                    Word W = new Word();
                                    W.Importance = 'I';
                                    W.Count++; 
                                    WordsInfo.put(s, W);
                            }
                    }

                    doc.select("img").remove();
                }

                doc.select("script, style, .hidden, label").remove();
                String S = doc.text();
                String[] arr = S.split(" ");

                for (String s : arr)
                {
                    if (WordsInfo.containsKey(s))
                    {
                            Word W = (Word) WordsInfo.get(s);
                            W.Count++;
                            if (W.Importance != 'T' && W.Importance != 'H')
                                      W.Importance = 'X';
                    }
                    else
                    {
                            Word W = new Word();
                            W.Importance = 'X';
                            W.Count++; 
                            WordsInfo.put(s, W);
                    }
                }
	 }
	 
         void setWordsLocations ()
         {
           webPageString_Temp = Jsoup.parse(webPageString_Temp).text();
            for (Map.Entry<String, Word> entry : WordsInfo.entrySet()) 
            { 
                entry.getValue().Location = ""; 
                for (int i = -1; (i = webPageString_Temp.indexOf(entry.getKey(), i + 1)) != -1; ) 
                {
                     entry.getValue().Location += Integer.toString(i);
                     entry.getValue().Location += ",";
                }
           }
         }
         
}
