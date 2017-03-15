package webcrawler;

import java.util.LinkedList;


/**
 * Abstract class that denotes a thread that can cooperate with a
 * ThreadController and has a Queue, a depth level and a MessageReceiver.
 */

public class ControllableThread extends Thread {
	//protected int level;
	protected Integer id;
	protected LinkedList queue;    /////////////////////
	protected ThreadController tc;
	protected MessageReceiver mr;
	public void setId(Integer _id) {
		id = _id;
	}
	/*public void setLevel(int _level) {
		level = _level;
	}*/
	public void setQueue(LinkedList _queue) {  /////////////////////////////
		queue = _queue;
	}
	public void setThreadController(ThreadController _tc) {
		tc = _tc;
	}
	public void setMessageReceiver(MessageReceiver _mr) {
		mr = _mr;
	}
	public ControllableThread() {
	}
        
        @Override
	public void run() {
            boolean again=true;
            while(again)
            {
                // pop new urls from the queue until queue is empty
		for (Object newURL = tc.pop(id);
			 newURL != null;
			 newURL = tc.pop(id)) {
			// Tell the message receiver what we're doing now
			mr.receiveMessage(newURL, id);
			// Process the newTask
			process(newURL);
			
			//if (tc.getMaxThreads() > tc.getRunningThreads()) {
                        try {
                                tc.startThreads();
                        } catch (Exception e) {
                                System.err.println("[" + id + "] " + e.toString());
                        }
			//}
		}
		// Notify the ThreadController that we're done
		again=tc.finished_AddnewUrls(id);
            }
		
	}

	/**
	 * The thread invokes the process method for each object in the queue
	 */
	public void process(Object o){
            //To implement // 
        }
}
