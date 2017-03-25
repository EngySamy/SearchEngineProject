package webcrawler;
import java.util.*;


public class ObjCrawlerQueue {

	protected Set<String> gatheredURLs;
	protected Map<Integer, String> threadUrl; ///these each queue is for one thread ==> take from the set for this queue
	protected int mx; //number of the max queues or threads
	protected int nQ; //number of the current queues 

	public ObjCrawlerQueue(int _mx) {
		mx = _mx;
                nQ=0;////////////////////////////////////////////
		threadUrl = new HashMap<>();
		for (int n = 0; n < mx; n++) {
			threadUrl.put(n,new String()) ;
		}
                gatheredURLs=new LinkedHashSet<String>();
	}

	public ObjCrawlerQueue() { //defualt constructor with max num of threads and queues=50
            mx = 50;
            nQ=0;////////////////////////////////////////////
            threadUrl = new HashMap<>();
            for (int n = 0; n < mx; n++) {
                    threadUrl.put(n,"") ;
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

	public synchronized int getGatheredSize() {
		return gatheredURLs.size();
	}


	public void clear() {
		for (int n = 0; n < nQ; n++)
			threadUrl.clear();
	}
}
