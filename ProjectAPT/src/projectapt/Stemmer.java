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
        String original,stemmed;
        Word val;
        Map <String, Word> newMap=new HashMap();
        for (Map.Entry<String, Word> entry : WordsInfo.entrySet())
        {
            original=entry.getKey();
            val=entry.getValue();

            PorterStemmer stemmer = new PorterStemmer();
            stemmer.setCurrent(original);
            stemmer.stem();
            stemmed=stemmer.getCurrent();

            Word w=newMap.get(stemmed);
            if(w!=null)
            {
                val.Count+=w.Count;
                val.Location+=w.Location;
                if(w.Importance=='T')
                    val.Importance='T';
                else if(w.Importance=='H' && (val.Importance=='I' || val.Importance=='X'))
                    val.Importance='H';
                else if(w.Importance=='I' && val.Importance=='X')
                    val.Importance='I';
                
                newMap.replace(stemmed, val);
            }
            else
            {
                newMap.put(stemmed, val);
            }
        }
        
        return newMap;
    }
    
    
}
