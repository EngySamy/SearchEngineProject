package webcrawler;

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
        savedStates=1;
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
            tc.saveState();
        }   
    }
    


    
}
