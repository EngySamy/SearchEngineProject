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
        System.out.println("Connected");
        //DB.createDatabase();
        //DB.createTables();
        int mxThreads=5;
        String[]seeds={"https://en.wikipedia.org/wiki/Main_Page","https://yahoo.tumblr.com/" };
        //seeds=null;
        ThreadController Controller=new ThreadController(mxThreads,seeds,DB);
        ///boolean done=false;
        while(Controller.getTotalLinks()<ThreadController.MAX_Links)
        {
            Controller.CheckState();
        }
      
        
     
        
    }    
}
