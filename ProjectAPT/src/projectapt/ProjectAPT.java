/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectapt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import static java.lang.Thread.sleep;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import webcrawler.ThreadController;
/**
 *
 * @author Mennah Rabie
 */
public class ProjectAPT {
    public static void main(String[] args) throws IOException, SQLException, InstantiationException, IllegalAccessException, InterruptedException {
        
        Database DB=new Database();
        DB.connect();
        //DB.createDatabase();
        //DB.createTables();
        
        int mxThreads=20;
        String[]seeds={"https://en.wikipedia.org/wiki/Main_Page","https://yahoo.tumblr.com/"};
        ThreadController Controller=new ThreadController(mxThreads,seeds,DB);
       
       /* File f = new File ("arts.html");
        String D;
        BufferedReader reader = new BufferedReader(new FileReader (f));
		    String         line = null;
		    StringBuilder  stringBuilder = new StringBuilder();
		    String         ls = System.getProperty("line.separator");

		    try {
		        while((line = reader.readLine()) != null) {
		            stringBuilder.append(line);
		            stringBuilder.append(ls);
		        }

		        D = stringBuilder.toString();
		    } finally {
		        reader.close();
		    }
        */
       // DB.InsertURL("https://en.wikipedia.org/wiki/User_talk:凰兰罗罗", "kkmgmgmgm");
       //
        
       /* System.out.println("Size is: "+ links.size());
        for(int i=0;i<links.size();i++)
        {    
            System.out.println(links.get(i).ID);
            System.out.println(links.get(i).URL);
            System.out.println(links.get(i).Document);
            
            System.out.println("---------------------------------------------------------");
        }*/
         sleep (5000);
         Vector<Link> links=DB.SelectURL();
         Map <String, Word> WordsInfo = new HashMap();
         
         for (int i=0; i<links.size(); i++)
         {     
            Parser parser = new Parser (links.get(i).Document, WordsInfo);
            parser.removeTagsAndSetImportance();
         
        // populate hash map
       /* String s[]={"Hello","Why","Are","You","Playing?!"};
        
        for(int i=0;i<5;i++)
        {
            Word w=new Word();
           // w.Name="X"+Integer.toString(i+1);
            WordsInfo.put(s[i],w);
        }
        */
       
       
        
        Map <String, Word> stopWords = new HashMap();
        try
        {
            //String In1=DB.SelectURL();
            //Scanner In1=new Scanner(new File("input.txt")); //file here from which you want to remove the stop words
            //FileOutputStream Out1=new FileOutputStream("removedstopwords.txt");

            stopWordsRemover swr=new stopWordsRemover();
            stopWords=swr.remove(WordsInfo);
        }
        catch(Exception e)
        {
            System.err.println("cannot read file");
        }
        
        /*for (Map.Entry<String, Word> entry : WordsInfo.entrySet())
        {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue().Name);
            System.out.println();
        }*/
        
        Map <String, Word> stemmedWords = new HashMap();
        try
        {   
            //Scanner In2=new Scanner(new File("removedstopwords.txt")); //file here from which you want to remove the stop words
            //FileOutputStream Out2=new FileOutputStream("stemmedtext.txt");
            
            Stemmer stm=new Stemmer();
            stemmedWords=stm.stemTerm(stopWords);
        }
        catch(Exception e)
        {
            //System.err.println("cannot read file");
        }
        
        Long KeywordID = null;
        for (Map.Entry<String, Word> entry : stemmedWords.entrySet()) 
                {
                    String key = entry.getKey();
                    Word value = entry.getValue();
                    System.out.println(key + "  count : " + value.Count +  "  Importance : " + value.Importance);
                    try 
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
                    Double Freq = ((double)value.Count/(double)stemmedWords.size());
                    DB.Insertinfo(links.get(i).ID, KeywordID, value.Importance, Freq);
                }
        
        for (Map.Entry<String, Word> entry : stemmedWords.entrySet()) 
                {
                    System.out.println ("Key -->" + entry.getKey());
                    System.out.println ("Count -->" + entry.getValue().Count);
                    System.out.println ("Importance -->" + entry.getValue().Importance);
                    System.out.println ("<-------------------------------------->");
                }
                
         }
    }    
}
