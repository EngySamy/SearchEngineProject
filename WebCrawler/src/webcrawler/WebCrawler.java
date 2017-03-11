/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

/**
 *
 * @author DeLL
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
public class WebCrawler {

    
    protected String processURL(String theURL) {
        int endPos;
        if (theURL.indexOf("?") > 0) {
            endPos = theURL.indexOf("?");
        } else if (theURL.indexOf("#") > 0) {
            endPos = theURL.indexOf("#");
        } else {
            endPos = theURL.length();
        }

        return theURL.substring(0, endPos);
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
     Document doc;
        try {

            // need http protocol
            BloomFilter<String> bf;
            String mainPath="https://www.w3schools.com/html/html_links.asp";
            URL mainUrl=new URL(mainPath);
            doc = Jsoup.connect(mainPath).get();

            // get page title
            String title = doc.title();
            System.out.println("title : " + title);

            // get all links
            Elements links = doc.select("a[href]");
            Set<String> v;
            v = new HashSet<>();
            //URL[] urls;
            int y=0;
            for (Element link : links) {
                y++;
                
                String abs=link.absUrl("href");
                //URL temp=new URL(abs);
                //v.add(abs);
                /*if(abs == null || abs.length() == 0)
                    System.out.println("NOOO");
                else
                    Jsoup.connect(abs).get();*/
                System.out.println(y);
                // get the value from href attribute
                System.out.println("\nlink : "  + link.attr("href"));
                System.out.println("absolute link : " + abs );
                System.out.println("text : " + link.text());

            }
            

        } catch (IOException e) {
            e.printStackTrace();
        }

    
    }
    
    public static void notEmpty(String string) {
        if (string == null || string.length() == 0)
            throw new IllegalArgumentException("String must not be empty");
    }
    
}
