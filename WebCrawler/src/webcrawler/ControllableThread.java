package webcrawler;

//import com.google.common.hash.BloomFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ControllableThread extends Thread {
	protected Integer id;
	protected LinkedList<String> queue;    /////////////////////
	protected ThreadController tc;
        Database DB;
        int linksCounter;
	
	public void setId(Integer _id) {
		id = _id;
	}
	
	public void setQueue(LinkedList<String> _queue) {  /////////////////////////////
		queue = _queue;
	}
        
	public void setThreadController(ThreadController _tc) {
		tc = _tc;
	}
        
        public void setDB(Database _db) {  
		DB= _db;
	}
        
	public ControllableThread() {
            linksCounter=0;
	}
        
    @Override
	public void run() {
            boolean again=true;
            //for testing
            int c=0,local;
            while(true)
            {
                // pop new urls from the queue until queue is empty
                local=0;
                for (String newURL = tc.pop(id);
                         newURL != null;
                         newURL = tc.pop(id)) {
                        // Process the newTask
                        System.out.println("I'm thread "+id);
                        process(newURL);
                        c=tc.getTotalLinks();
                        System.out.println("Total gathered links now are "+c);
                        if(c>=ThreadController.MAX_Links )
                            break;
                }
                // Notify the ThreadController that we're done
                //c=tc.updateTotalLinks(local);
                
                
                if(c>=ThreadController.MAX_Links )
                    break;
                else
                    again=tc.fillThreadQueue(id);
                
                
            }
		
	}

	
	public void process(String newUrl){
            Document doc;
            try {
                // need http protocol                
                //String newUrl=o.toString();
                if(newUrl != null &&  newUrl.length()!= 0) {
                    doc = Jsoup.connect(newUrl).get();
                    // get page title
                    String title = doc.title();
                    System.out.println("title : " + title);
                    // get all links
                    Elements links = doc.select("a[href]");                    
                    PrintWriter outt=new PrintWriter("thread"+id+".txt");
                    int y=0;
                    for (Element link : links) {
                        y++;
                        String abs=link.absUrl("href");
                        if (abs == null || abs.length() != 0) {
                            
                            System.out.println("absolute link(before process it) : " + abs );
                            ///processURL ///remove bookmarks and check them between each others
                            abs=processURL(abs);
                            tc.addNewUrl(abs);  
                            //for debugging
                            System.out.println(y);
                            // get the value from href attribute
                            //System.out.println("\nlink : "  + link.attr("href"));
                            System.out.println("absolute link : " + abs );
                            //System.out.println("text : " + link.text());
                        }  
                    }
                    tc.incTotalLinks();
                    if(DB.SearchURL(newUrl))
                        DB.InsertURL(newUrl, doc.toString());
                    
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
