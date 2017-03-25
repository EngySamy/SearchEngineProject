package webcrawler;

//import com.google.common.hash.BloomFilter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import robottxt.RobotExclusion;
import projectapt.Database;

public class ControllableThread extends Thread {
	protected Integer id;
	protected LinkedList<String> queue;    /////////////////////
	protected ThreadController tc;
        Database DB;
        int linksCounter;
	
	public void setId(Integer _id) {
		id = _id;
	}
	
	/*public void setQueue(LinkedList<String> _queue) {  /////////////////////////////
		queue = _queue;
	}*/
        
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
            int c=0;
            URL u;
            String uu=tc.TakeURLToFetch(id);
            while(uu!=null &&!uu.equals(""))
            {
                System.out.println("I'm thread "+id);
                process(uu);
                c=tc.getTotalLinks();
                System.out.println("Total gathered links now are "+c);             
                if(c>=ThreadController.MAX_Links )
                    break;
                else
                    uu=tc.TakeURLToFetch(id);   
            }
            tc.incFinishedThreads();
	}
        
	
    public void process(String newUrl){
        Document doc;
        try {             
            if(newUrl != null &&  newUrl.length()!= 0) {
                RobotExclusion robotExclusion = new RobotExclusion();
                String userAgentString="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
                URL urll = new URL(null,newUrl,new sun.net.www.protocol.https.Handler());                       
                if(SSLTest.testSsl(urll)==200) {       
                    if (robotExclusion.allows(urll, userAgentString)) 
                    {
                        try{         
                            doc = Jsoup.connect(newUrl).get();
                            // get all links
                            Elements links = doc.select("a[href]");                    
                            //int y=0; //for debugging and testing
                            for (Element link : links) {
                                //y++;
                                String abs=link.absUrl("href");
                                if (abs == null || abs.length() != 0) {

                                    //System.out.println("absolute link(before process it) : " + abs+" from thread "+id );
                                    ///processURL ///remove bookmarks and check them between each others
                                    abs=processURL(abs);
                                    if(!abs.equals(newUrl))
                                        tc.addNewUrl(abs);  
                                    //for debugging
                                    //System.out.println(y);
                                    //System.out.println("absolute link : " + abs );                                 
                                }

                                
                            }
                            //if(DB.SearchURL(newUrl))
                            try
                            { /////////////CHANGE DOC   ////// JUST FOR TESTING
                                DB.InsertURL(newUrl, doc.toString());
                                tc.incTotalLinks();
                            }
                            catch(Exception e){
                                
                            }

                        }
                        catch(org.jsoup.HttpStatusException e )
                        {
                            System.out.println("Bad Symbole ");
                        }
                        catch(IOException e)
                        {
                            System.out.println("Ignore this link .. ");
                            //e.printStackTrace();                      
                        }      
                    }
                }

                
            }
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Ignore this link ..  ");
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
