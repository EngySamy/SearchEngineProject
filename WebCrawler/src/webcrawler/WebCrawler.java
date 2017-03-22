package webcrawler;

public class WebCrawler {
    
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        
        int mxThreads=2;
        String[]seeds={"https://en.wikipedia.org/wiki/Main_Page","https://yahoo.tumblr.com/"};
        ThreadController Controller=new ThreadController(mxThreads,seeds);
     
    }
    
    
}
