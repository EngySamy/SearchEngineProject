package projectapt;

import org.tartarus.snowball.ext.PorterStemmer;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Stemmer {
    
    Map <String, Word> stemTerm (Map <String, Word> WordsInfo) 
    {
        String in,s1;
        Word val;
        Map <String, Word> newMap=new HashMap();
        //for (Iterator<Map.Entry<String, Word>> it = WordsInfo.entrySet().iterator(); it.hasNext();)
        for (Map.Entry<String, Word> entry : WordsInfo.entrySet())
        {
            //Map.Entry<String, Word> entry = it.next();
            in=entry.getKey();
            val=entry.getValue();
            
            s1=in.toLowerCase();
            //s1=stemTerm(s1);

            PorterStemmer stemmer = new PorterStemmer();
            stemmer.setCurrent(s1);
            stemmer.stem();
            s1=stemmer.getCurrent();

            newMap.put(s1, val);
            //WordsInfo.put(s1, WordsInfo.remove(in));

            //System.out.println(s1);
                
            //PrintStream p=new PrintStream(out);
            //p.printf(s1);
        }
        
        return newMap;
    }
    
    
}