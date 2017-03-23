package webcrawler;

public class WebCrawler {
    
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        
        int mxThreads=20;
        String[]seeds={"https://en.wikipedia.org/wiki/Main_Page","https://yahoo.tumblr.com/"};
        ThreadController Controller=new ThreadController(mxThreads,seeds);
     
    }
    
    
}
