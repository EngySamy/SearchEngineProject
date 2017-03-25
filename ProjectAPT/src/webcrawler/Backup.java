package webcrawler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import static webcrawler.ThreadController.NUM_PERIOD;
import projectapt.Database;

/**
 *
 * @author DeLL
 */
public class Backup extends Thread {
    
    int savedStates;
    ObjCrawlerQueue urlQueues;
    ThreadController tc;
    Database DB;
    int mxThreads;
    public Backup(Database _db,ThreadController _tc,ObjCrawlerQueue _urlQueues,int _mxThreads){
        DB=_db;
        tc=_tc;
        urlQueues=_urlQueues;
        savedStates=0;
        mxThreads=_mxThreads;
    }
    
    @Override
	public void run() {
            while(tc.getFinishedThreads()!=mxThreads)
            {
                CheckState();

            }
        }
    
    public void CheckState(){
        int temp=tc.getTotalLinks();
        if(temp>=savedStates*NUM_PERIOD) {
            System.out.println("Saving the state now after reaching "+temp+" Links");
            saveState();
        }   
    }
    
    public synchronized void saveState(){
        
        try {
            DB.clearBackup();
            int num=this.urlQueues.getGatheredSize();
            Iterator<String> iter=urlQueues.gatheredURLs.iterator();
            int n;
            for ( n = 0; n < num && iter.hasNext()==true; n++) {
                DB.newBackup(iter.next());
            } 
        } catch(Exception e)
        {
            System.out.println("Escape this link");
        }
        
        savedStates++;        
    }
    public synchronized boolean loadSavedUrls(){
        ResultSet res=DB.loadBackup();
        int count=0;
        if(res!=null)
        {
            try {
                while(res.next())
                {
                    urlQueues.gatheredURLs.add(res.getString("Link"));
                    count++;
                }
                    
            } catch (SQLException ex) {
                Logger.getLogger(ThreadController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (count>0)
                return true;
        }
        return false;
    }
    
}
