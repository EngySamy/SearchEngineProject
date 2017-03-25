package webcrawler;

//import com.google.common.hash.BloomFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
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
            int c=0;
            URL u;
            while(true)
            {
                // pop new urls from the queue until queue is empty
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
                if(c>=ThreadController.MAX_Links )
                    break;
                else
                    tc.fillThreadQueue(id);   
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
                                    tc.addNewUrl(abs);  
                                    //for debugging
                                    //System.out.println(y);
                                    //System.out.println("absolute link : " + abs );                                 
                                }

                                if(DB.SearchURL(newUrl)){ /////////////CHANGE DOC   ////// JUST FOR TESTING
                                    DB.InsertURL(newUrl, "Hii");
                                    tc.incTotalLinks();
                                }
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
            System.out.println("!!!!!! ");
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

