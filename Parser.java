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
	//File webPage;
        Long DocLength;
	Map <String, Word> WordsInfo = new HashMap();
        Long DocID;
	
	 Parser (String s, Long id) throws IOException
	 {
		// webPage = f;
		 webPageString = readFile (s);
                 DocID = id;
                 DocLength = 0L;
	 }
	 
	 void removeTagsAndSetImportance () throws IOException
	 {
                Database DB=new Database();
                DB.connect();
                DB.createTables();
                DB.InsertURL("https://en.wikipedia.org/wiki/Portal:History", webPageString);
		 
		webPageString = webPageString.toLowerCase();
                Document docBeforeAttr = Jsoup.parse(webPageString);
	
                String ImgAlt = docBeforeAttr.select("img").attr("alt");
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
                                    if (W.Importance != 'T')
                                       W.Importance = 'I';
                            }
                            else
                            {
                                    Word W = new Word();
                                    W.Importance = 'I';
                                    W.Count++; 
                                    WordsInfo.put(s, W);
                            }
                            DocLength++;
                    }

                    docBeforeAttr.select("img").remove();
                }

                webPageString = webPageString.replaceAll("(<\\w+)[^>]*(>)", "$1$2");

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
                        DocLength++;
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
                            DocLength++;
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
                            DocLength++;
                    }

                    doc.select("h1, h2, h3, h4, h5, h6").remove();
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

               // Long KeywordID = null;
                for (Map.Entry<String, Word> entry : WordsInfo.entrySet()) 
                {
                    String key = entry.getKey();
                    Word value = entry.getValue();
                    System.out.println(key + "  count : " + value.Count +  "  Importance : " + value.Importance);
                  /*  try 
                    {
                        KeywordID = DB.SelectIDSimilarWord(key);
                    } 
                    catch (SQLException ex) 
                    {
                        Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (KeywordID == null)
                    {
                        DB.InsertKeyword(key);
                        try 
                        {
                          KeywordID = DB.SelectMaxKeywordID();
                        } 
                        catch (SQLException ex) 
                        {
                           Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    Double Freq = ((double)value.Count/(double)DocLength);
                    DB.Insertinfo(DocID, KeywordID, value.Importance, Freq);*/
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
