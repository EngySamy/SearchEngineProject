package webcrawler;
import java.util.*;


public class ObjCrawlerQueue {

	protected Set<String> gatheredURLs;
	//Set processedURLs;
	protected Map<Integer, String> queues; ///these each queue is for one thread ==> take from the set for this queue
	protected int mx; //number of the max queues or threads
	protected int nQ; //number of the current queues 

	public ObjCrawlerQueue(int _mx) {
		mx = _mx;
                nQ=0;////////////////////////////////////////////
		queues = new HashMap<>();
		for (int n = 0; n < mx; n++) {
			queues.put(n,new String()) ;
		}
                gatheredURLs=new LinkedHashSet<String>();
	}

	public ObjCrawlerQueue() { //defualt constructor with max num of threads and queues=50
            mx = 50;
            nQ=0;////////////////////////////////////////////
            queues = new HashMap<>();
            for (int n = 0; n < mx; n++) {
                    queues.put(n,"") ;
            }
            gatheredURLs=new LinkedHashSet<>();
	}

	public synchronized Set<String> getGatheredURLs() {
            return gatheredURLs;
	}
        
        public synchronized boolean addNewGatheredURL(String url){
            return gatheredURLs.add(url);
        }
        
            
        public synchronized boolean searchGatheredURL(String url){
            return gatheredURLs.contains(url);
        }


	/*public int getQueueSize(Integer threadId) {
		if (threadId < 0 || threadId >= nQ)
			return 0;
		else
			return queues.get(threadId).size();
	}*/

	/*public int getProcessedSize() {
		return processedElements.size();
	}*/

	public synchronized int getGatheredSize() {
		return gatheredURLs.size();
	}

        /*@Override
	public void setMaxElements(int elements) {
		mx = elements;
	}*/

	/*public String pop(Integer threadId) {   //pop from a queue of certain thread       
		if (queues.get(threadId).isEmpty())
                    return null;
		else
                    return queues.get(threadId).removeFirst();
	}*/

	/*public boolean push(String URL, Integer threadId) {
		if (queues.get(threadId)==null)
			return false;
		queues.get(threadId).addLast(URL);
		return true;
	}*/

	public void clear() {
		for (int n = 0; n < nQ; n++)
			queues.clear();
	}
}
