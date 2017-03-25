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
import org.apache.log4j.BasicConfigurator;
import webcrawler.ThreadController;
/**
 *
 * @author Mennah Rabie
 */
public class ProjectAPT {
    public static void main(String[] args) throws IOException, SQLException, InstantiationException, IllegalAccessException, InterruptedException {
        
        Database DB=new Database();
        BasicConfigurator.configure(); //for robot.txt handling
        DB.connect();
        DB.createDatabase();
        DB.createTables();
        
        Indexer indexer=new Indexer();
        Vector<Link> links = new Vector<>();
        int mxThreads=20;
        String[]seeds={"https://en.wikipedia.org/wiki/Main_Page","https://yahoo.tumblr.com/","https://dev.mysql.com/doc/refman/5.7/en/charset-database.html", "http://dmoztools.net/", "http://www.imdb.com/", "https://archive.org/", "http://www.W3.org/", "http://www.ebay.com/", "https://www.cnet.com/", "http://www.ieee.org/", "https://www.bloomberg.com/"};
        ThreadController Controller=new ThreadController(mxThreads,seeds,DB);
        
        int count = 0;
        while(true)
        { 
            if(DB.SelectMaxLinkID() != null)
            {  
                    links = DB.SelectURL();
                    if (!(links.isEmpty()))
                    {
                        DB.DeleteDoc(links.lastElement().ID);
                        for (int k=0; k<links.size(); k++)
                            indexer.createIndex(DB, links.get(k).ID, links.get(k).Document);
                        links.removeAllElements();
                    }           
            }
            if (Controller.getTotalLinks()>ThreadController.MAX_Links)
            {
                if (count < 5)
                    count++;
                else
                    break;
            }
           Controller.CheckState();     
        }  
    }    
}
