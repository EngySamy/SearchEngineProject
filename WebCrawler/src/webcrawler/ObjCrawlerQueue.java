package webcrawler;
import java.net.URL;
import java.util.*;

/**
 * Simple "reference" implementation of the queue interface
 * In addition to the interface, an object "data" is supported
 * for carrying additional data over threads.
 * By default, the queue supports 4 levels and an unlimited
 * number of elements. The number of elements is unlimited when
 * set to -1. A limited number of elements and a different
 * number of levels can be specified in the appropriate constructors.
 *
 * This code is in the public domain.
 *
 * @author Andreas Hess <andreas.hess at ucd.ie>, 11/02/2003
 * 
 */

public class ObjCrawlerQueue implements CrawlerQueue {

	Object data;
	Set gatheredURLs;
	Set processedURLs;
	Map<Integer, LinkedList> queues; ///these each queue is for one thread ==> take from the set for this queue
	int mx; //number of the max queues or threads
	int nQ; //number of the current queues 

	public synchronized void setData(Object o) {
		data = o;
	}

	public synchronized Object getData() {
		return data;
	}

	public ObjCrawlerQueue(int _mx) {
		//nq = _nq;
		mx = _mx;
                nQ=0;////////////////////////////////////////////
		queues = new HashMap<Integer, LinkedList>();
		for (int n = 0; n < mx; n++) {
			queues.put(n,new LinkedList()) ;
		}
	}

	public ObjCrawlerQueue() { //defualt constructor with max num of threads and queues=50
		//nq = _nq;
		mx = 50;
                nQ=0;////////////////////////////////////////////
		queues = new HashMap<Integer, LinkedList>();
		for (int n = 0; n < mx; n++) {
			queues.put(n,new LinkedList()) ;
		}
	}


        @Override
	public Set getGatheredURLs() {
		return gatheredURLs;
	}

	/*public Set getProcessedElements() {
		return processedElements;
	}*/

        @Override
	public int getQueueSize(Integer threadId) {
		if (threadId < 0 || threadId >= nQ)
			return 0;
		else
			return queues.get(threadId).size();
	}

	/*public int getProcessedSize() {
		return processedElements.size();
	}*/

        @Override
	public int getGatheredSize() {
		return gatheredURLs.size();
	}

        /*@Override
	public void setMaxElements(int elements) {
		mx = elements;
	}*/

        @Override
	public synchronized Object pop(Integer threadId) {   //pop from a queue of certain thread       
		if (queues.get(threadId)==null)
			return null;
		else
			return queues.get(threadId).removeFirst();
	}

        @Override
	public synchronized boolean push(Object URL, Integer threadId) {
		if (queues.get(threadId)==null)
			return false;
		queues.get(threadId).addLast(URL);
		return true;
	}

        @Override
	public synchronized void clear() {
		for (int n = 0; n < nQ; n++)
			queues.get(n).clear();
	}
}
