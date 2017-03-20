package webcrawler;

import java.util.Iterator;
import java.util.Map;

public class ThreadController {
	//maximum number of parallel threads
	int maxThreads;

	/**
	 * the task queue
	 */
	ObjCrawlerQueue urlQueues;

	/**
	 * An object that is notified about what a thread does
	 * See comments for interface MessageReceiver for details.
	 */
	MessageReceiver receiver;

	/**
	 * A unique synchronized counter
	 */
	int counter;

	// Number of currently running threads
	int nThreads;
        
        //
        Map<Integer, ControllableThread> threads;

	
	public ThreadController(int _maxThreads,String[] seeds)//, MessageReceiver _receiver)
		throws InstantiationException, IllegalAccessException {
		//threadClass = _threadClass;
		maxThreads = _maxThreads;
		//receiver = _receiver;
		counter = 0;
		nThreads = 0;
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
			thread.setMessageReceiver(receiver);   ////////////////////////////////??????
			thread.setQueue(urlQueues.queues.get(n));
			thread.setId(nThreads++);
                        threads.put(n, thread);
		}
                nThreads= this.divideSeeds();
                for (int n = 0; n < maxThreads; n++) 
                {
                    threads.get(n).start();
                }
                    
	}
        
        //divide the first seeds among the threads
        private int divideSeeds(){ 
            int filled=0,avg=urlQueues.getGatheredSize()/maxThreads;
            Iterator<String> iter = urlQueues.gatheredURLs.iterator();
            if(avg<1) { //number of threads greater than number of seeds 
                for(int i=0;i<urlQueues.getGatheredSize();i++){
                    moveUrlFromControllerToThread(iter,i);
                    filled++;
                }
            }
            else { //put avg number of urls in the first (maxThreads-1) and the rest of them in the last one
                int i;
                for(i=0;i<maxThreads-1;i++){
                    for(int j=0;j<avg;j++){
                        moveUrlFromControllerToThread(iter,i);
                    }         
                }
                for(int j=0;j<urlQueues.getGatheredSize();j++){
                       moveUrlFromControllerToThread(iter,i);
                }     
                filled=maxThreads;
            }
            return filled;
        }
        
        public synchronized boolean finished_AddnewUrls(int threadId)
        {
            return fillThreadQueue(threadId);
        }
        
        private boolean fillThreadQueue(int threadId)
        {
            int avgToHaveInThrd=urlQueues.gatheredURLs.size()/maxThreads;
            Iterator<String> iter = urlQueues.gatheredURLs.iterator();
            for (int n = 0; n < avgToHaveInThrd && iter.hasNext()==true; n++) {
                moveUrlFromControllerToThread(iter,threadId);
            } 
            return true;
        }
        private void moveUrlFromControllerToThread(Iterator<String> iter,int threadId)
        {
            String temp=iter.toString();
            urlQueues.queues.get(threadId).addLast(temp);
            iter.next();
            iter.remove();
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
            return (String)urlQueues.pop(threadID);
        }
        
        public void addNewUrl(String Url)
        {
            //search for the Url in the processed URLs (in database) and in the gatherd Urls (in the set) 
            //if not exist add it to the gathered
            boolean found; //after searching in the database
            found=urlQueues.searchGatheredURL(Url);
            if(!found) {
                urlQueues.addNewGatheredURL(Url);
            }
            
        }
    
}
