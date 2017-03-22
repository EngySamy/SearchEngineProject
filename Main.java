package stemmer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) 
    {
        Database DB=new Database();
        DB.connect();
        //DB.createDatabase();
        //DB.createTables();
        
        Map <String, Word> WordsInfo = new HashMap();
        // populate hash map
        String s[]={"Hello","Why","Are","You","Playing?!"};
        
        for(int i=0;i<5;i++)
        {
            Word w=new Word();
            w.Name="X"+Integer.toString(i+1);
            WordsInfo.put(s[i],w);
        }
        
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
    }
}