package webcrawler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadController {
	//maximum number of parallel threads
        public static final int MAX_Links = 50;
	int maxThreads;

	/**
	 * the task queue
	 */
	ObjCrawlerQueue urlQueues;

	/**
	 * A unique synchronized counter
	 */
	int counter;

	// Number of currently running threads
	int nThreads;
        
        //
        Map<Integer, ControllableThread> threads;
        
        Database DB;
        
        AtomicInteger linksTotal;

	
	public ThreadController(int _maxThreads,String[] seeds)//, MessageReceiver _receiver)
		throws InstantiationException, IllegalAccessException {
		//threadClass = _threadClass;
		maxThreads = _maxThreads;
		//receiver = _receiver;
		counter = 0;
		nThreads = 0;
                linksTotal=new AtomicInteger(0);
                urlQueues=new ObjCrawlerQueue(maxThreads);
                threads=new HashMap<>();
                for (String seed : seeds) {
                    urlQueues.addNewGatheredURL(seed);
                }
                DB=new Database();
                DB.connect();
                //DB.createTables();
		createThreads();      
	}
        
    private synchronized void createThreads() //call it just one time in the start of the program
	throws InstantiationException, IllegalAccessException {
		for (int n = 0; n < maxThreads; n++) {
                    ControllableThread thread =new ControllableThread();
                    thread.setThreadController(this);
                    thread.setQueue(urlQueues.queues.get(n));
                    thread.setId(n);
                    thread.setDB(DB);
                    threads.put(n, thread);
		}
        nThreads= this.divideSeeds();
        for (int n = 0; n < maxThreads; n++) 
            threads.get(n).start();                
    }
        
    //divide the first seeds among the threads
    private int divideSeeds(){ 
        int filled=0,avg=urlQueues.getGatheredSize()/maxThreads;
        if(avg<1) { //number of threads greater than number of seeds 
            for(String s:urlQueues.gatheredURLs){
                urlQueues.queues.get(filled).addLast(s);
                filled++;    
            }
        }
        else { //put avg number of urls in the first (maxThreads-1) and the rest of them in the last one
            int i;
            for(i=0;i<maxThreads-1;i++){
            	copyAndRemoveFromSet(i,avg);   
            }
            copyAndRemoveFromSet(i,urlQueues.getGatheredSize()+5);   
            filled=maxThreads;
        }
        urlQueues.gatheredURLs.clear();
        return filled;
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
    
    public synchronized boolean fillThreadQueue(int threadId)
    {
        int avgToHaveInThrd=urlQueues.gatheredURLs.size()/maxThreads;
            return copyAndRemoveFromSet(threadId,avgToHaveInThrd);
           
    }
     
    private synchronized boolean copyAndRemoveFromSet(int threadId , int num){
    	//to copy the urls from gatheredURLs to the thread
        int n=0;
        for(String s:urlQueues.gatheredURLs){
        	if(n==num)
            	break;
            urlQueues.queues.get(threadId).addLast(s);
            n++;           
        }
        //to remove the urls from gatheredURLs
        Iterator<String> iter=urlQueues.gatheredURLs.iterator();
        for ( n = 0; n < num && iter.hasNext()==true; n++) {
            iter.next();
            iter.remove();
        } 
        return true;
    }
        
    public synchronized int getUniqueNumber() {
            return counter++;
    }

    public int getMaxThreads() {
            return maxThreads;
    }

    public int getRunningThreads() {
            return nThreads;
    }
 
    public String pop(int threadID)
    {
        return urlQueues.pop(threadID);
    }
    
    public void addNewUrl(String Url)
    {
        //search for the Url in the processed URLs (in database) 
        //and in the gatherd Urls (in the set)--> this is handeled automatically when adding to the set
        //if not exist add it to the gathered
        boolean unique=DB.SearchURL(Url);
        if(unique)
        {
            urlQueues.addNewGatheredURL(Url); 
            //linksTotal++;
        }       
    }

}
