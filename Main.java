package stemmer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) 
    {
        Database DB=new Database();
        DB.connect();
        DB.createTables();
        
        try
        {
            String In1=DB.SelectURL();
            FileOutputStream Out1=new FileOutputStream("removedstopwords.html");

            stopWordsRemover swr=new stopWordsRemover();
            swr.remove(In1, Out1);
        }
        catch(Exception e)
        {
            System.err.println("cannot read file");
        }
        try
        {   
            Scanner In2=new Scanner(new File("removedstopwords.html")); //file here from which you want to remove the stop words
            FileOutputStream Out2=new FileOutputStream("stemmedtext.html");
            
            Stemmer stm=new Stemmer();
            stm.stemTerm(In2, Out2);
        }
        catch(Exception e)
        {
            System.err.println("cannot read file");
        }
    }
}
