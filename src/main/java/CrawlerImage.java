import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liuhu on 18/1/8.
 */
public class CrawlerImage extends BreadthCrawler {

    static AtomicInteger count = new AtomicInteger();

    public CrawlerImage(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);

        //爬虫才会自动解析图片链接
        getConf().setAutoDetectImg(true);


        //添加种子URL
        addSeed("https://fabiaoqing.com/?c=search&a=search&keyword=蘑菇头");
        //限定爬取范围
        addRegex("https://fabiaoqing.com/.*");
        addRegex("-.*#.*");
        addRegex("-.*\\?.*");

        //设置为断点爬取，否则每次开启爬虫都会重新爬取
//        demoImageCrawler.setResumable(true);
        setThreads(30);
    }

    public void visit(Page page, CrawlDatums next) {


        Elements elements = page.select("img");

        //可以用函数表达式过滤
        for (Element e : elements) {
            if (!e.hasAttr("title")) {
                continue;
            }

            String titleStr = e.getElementsByAttribute("title").attr("title");
            if (titleStr == null) {
                return;
            }
            if (!titleStr.matches(".*蘑菇头.*")) {
                continue;
            }
            if (e.hasAttr("data-original")) {
                String url = e.getElementsByAttribute("data-original").attr("data-original");
                try {
                    writeFile(url);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }


    }

    //写文件
    private void writeFile(String urlArg) throws IOException {

        String extensionName = "";

        if (urlArg.endsWith("jpg")) {
            extensionName = "jpg";
        }

        if (urlArg.endsWith("gif")) {
            extensionName = "gif";
        }

        if (urlArg.endsWith("png")) {
            extensionName = "png";
        }

        // 构造URL
        URL url = new URL(urlArg);
        // 打开连接
        URLConnection con = url.openConnection();
        // 输入流
        InputStream is = con.getInputStream();

        String fileName = String.format("%s.%s", count.incrementAndGet(), extensionName);

        File file = new File("image", fileName);

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        OutputStream os = new FileOutputStream(file);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();

    }
}
