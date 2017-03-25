package webcrawler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import projectapt.Database;

public class ThreadController {
    //maximum number of parallel threads
    public static final int MAX_Links = 6000;
    static final long NUM_PERIOD =10;
    int savedStates;
    int maxThreads;
    AtomicInteger finishedThreads;

    /**
     * the task queue
     */
    ObjCrawlerQueue urlQueues;

    /**
     * A unique synchronized counter
     */
    AtomicInteger counter;

    Map<Integer, ControllableThread> threads;

    Database DB;

    AtomicInteger linksTotal;
    
    Backup bc;


    public ThreadController(int _maxThreads,String[] seeds,Database _db)
            throws InstantiationException, IllegalAccessException {
            maxThreads = _maxThreads;
            counter=new AtomicInteger(0);
            linksTotal=new AtomicInteger(0);
            urlQueues=new ObjCrawlerQueue(maxThreads);
            finishedThreads=new AtomicInteger(0);
            threads=new HashMap<>();
            savedStates=1;
            DB=_db;
            bc=new Backup(DB,this,urlQueues,maxThreads);
            if(seeds==null)
                this.loadSavedUrls();
            else                
                for (String seed : seeds) {
                    urlQueues.addNewGatheredURL(seed);
                }
            createThreads();      
    }
        
    private synchronized void createThreads() //call it just one time in the start of the program
	throws InstantiationException, IllegalAccessException {
        for (int n = 0; n < maxThreads; n++) {
            ControllableThread thread =new ControllableThread();
            thread.setThreadController(this);
            thread.setId(n);
            thread.setDB(DB);
            threads.put(n, thread);
        }      
        for (Map.Entry<Integer, ControllableThread> entry : threads.entrySet())
            entry.getValue().start();
        bc.start();
    }
    public int updateTotalLinks(int add){
        return linksTotal.addAndGet(add);
    }
    
    public int incTotalLinks(){
        return linksTotal.incrementAndGet();
    }
    
    public int getTotalLinks(){
        return linksTotal.get();
    }
    
    public int incFinishedThreads(){
        return finishedThreads.incrementAndGet();
    }
    
    public int getFinishedThreads(){
        return finishedThreads.get();
    }
    
    public synchronized String TakeURLToFetch(int threadId)
    {
        Iterator<String> iter=urlQueues.gatheredURLs.iterator();
        if(iter.hasNext()){
            String st=iter.next();
            iter.remove();
            return st;
        } 
        return "";
    }
    
    public void CheckState(){
        int temp=this.getTotalLinks();
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
        
    public int getUniqueNumber() {
        return counter.incrementAndGet();
    }

    public int getMaxThreads() {
        return maxThreads;
    }
    
    public synchronized void addNewUrl(String Url)
    {
        //search for the Url in the processed URLs (in database) 
        //and in the gatherd Urls (in the set)--> this is handeled automatically when adding to the set
        //if not exist add it to the gathered
        boolean unique=DB.SearchURL(Url);
        if(unique)
        {
            urlQueues.addNewGatheredURL(Url); 
        }       
    }

}
