package webcrawler;

import com.google.common.hash.BloomFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Abstract class that denotes a thread that can cooperate with a
 * ThreadController and has a Queue, a depth level and a MessageReceiver.
 */

public class ControllableThread extends Thread {
	protected Integer id;
	protected LinkedList queue;    /////////////////////
	protected ThreadController tc;
	protected MessageReceiver mr;
	public void setId(Integer _id) {
		id = _id;
	}
	
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
            //for testing
            int c=0;
            while(again&&c<50)
            {
                // pop new urls from the queue until queue is empty
		for (String newURL = tc.pop(id);
			 newURL != null;
			 newURL = tc.pop(id)) {
			// Tell the message receiver what we're doing now
			//mr.receiveMessage(newURL, id);
			// Process the newTask
			process(newURL);
		}
		// Notify the ThreadController that we're done
		again=tc.finished_AddnewUrls(id);
                c++;
            }
		
	}

	/**
	 * The thread invokes the process method for each object in the queue
	 */
	public void process(Object o){
            Document doc;
            try {

                // need http protocol                
                String newUrl=o.toString();
                if(newUrl == null || newUrl.length() == 0) {
                    doc = Jsoup.connect(newUrl).get();
                    //TODO : save doc in the database
                
                    // get page title
                    String title = doc.title();
                    System.out.println("title : " + title);

                    // get all links
                    Elements links = doc.select("a[href]");
                    ///processURL ///remove bookmarks and check them between each others
                    PrintWriter outt=new PrintWriter("thread"+id+".txt");
                    int y=0;
                    for (Element link : links) {
                        y++;

                        String abs=link.absUrl("href");
                        if (abs == null || abs.length() != 0) {
                            tc.addNewUrl(abs);

                            //for debugging
                            outt.println(y);
                            // get the value from href attribute
                            outt.println("\nlink : "  + link.attr("href"));
                            outt.println("absolute link : " + abs );
                            outt.println("text : " + link.text());
                        }  
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    
        }
        
         protected String processURL(String Url) { //to remove bookmark from the link ==>  www.eee.com/rr/ = www.eee.com/rr/#e1
            int endPos;
            if (Url.indexOf("?") > 0) {
                endPos = Url.indexOf("?");
            } else if (Url.indexOf("#") > 0) {
                endPos = Url.indexOf("#");
            } else {
                endPos = Url.length();
            }

            return Url.substring(0, endPos);
        }
        
        
}
