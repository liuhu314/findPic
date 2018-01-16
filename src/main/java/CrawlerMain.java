import cn.edu.hfut.dmic.webcollector.plugin.net.OkHttpRequester;

/**
 * Created by liuhu on 18/1/8.
 */
public class CrawlerMain {

    public static void main(String[] args) {
        CrawlerImage demoImageCrawler = new CrawlerImage("crawl",true);
        demoImageCrawler.setRequester(new OkHttpRequester());
        //设置为断点爬取，否则每次开启爬虫都会重新爬取
        demoImageCrawler.setResumable(false);
        try {
            demoImageCrawler.start(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
