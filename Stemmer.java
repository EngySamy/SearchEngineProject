package stemmer;

import org.tartarus.snowball.ext.PorterStemmer;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Stemmer {
    
    Stemmer()
    {
        
    }
    
    void stemTerm (Scanner in,FileOutputStream out) 
    {
        while(in.hasNext())
            {
                String s1=in.next();
                s1=s1.toLowerCase();
                //s1=stemTerm(s1);
                
                PorterStemmer stemmer = new PorterStemmer();
                stemmer.setCurrent(s1);
                stemmer.stem();
                s1=stemmer.getCurrent();
        
                //System.out.println(s1);
                PrintStream p=new PrintStream(out);
                p.println(s1);
            }
        
    }
    
    
}
