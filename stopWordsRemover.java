package stemmer;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class stopWordsRemover {
    String[] stopWrds={ "+","=","^","$","a","able","about","across","after","all","almost","also","am","among","an","and","any","are","as","at","be","because","been","but","by","can","cannot","could","dear","did","do","does","either","else","ever","every","for","from","get","got","had","has","have","he","her","hers","him","his","how","however","i","if","in","into","is","it","its","just","least","let","like","likely","may","me","might","most","must","my","neither","no","nor","not","of","off","often","on","only","or","other","our","own","rather","said","say","says","she","should","since","so","some","than","that","the","their","them","then","there","these","they","this","tis","to","too","us","wants","was","we","were","what","when","where","which","while","who","whom","why","will","with","would","yet","you","your","ain't","aren't","can't","could've","couldn't","didn't","doesn't","don't","hasn't","he'd","he'll","he's","how'd","how'll","how's","i'd","i'll","i'm","i've","isn't","it's","might've","mightn't","must've","mustn't","shan't","she'd","she'll","she's","should've","shouldn't","that'll","that's","there's","they'd","they'll","they're","they've","wasn't","we'd","we'll","we're","weren't","what'd","what's","when'd","when'll","when's","where'd","where'll","where's","who'd","who'll","who's","why'd","why'll","why's","won't","would've","wouldn't","you'd","you'll","you're","you've" };

    stopWordsRemover()
    {
        
    }
    
    void remove(Scanner in,FileOutputStream out)
    {
        while(in.hasNext())
            {
                int flag=1;
                String s1=in.next();
                s1=s1.toLowerCase();
                for(int i=0;i<stopWrds.length;i++)
                {
                    if(s1.equals(stopWrds[i]))
                    {
                        flag=0;
                    }
                }
                if(flag!=0)
                {
                    //remove punctuation
                    s1 = s1.replaceAll("\\p{P}","");
                    if(!"".equals(s1))
                        s1+=" ";
                    
                    PrintStream p=new PrintStream(out);
                    p.printf(s1);
                }
            }
    }
}
