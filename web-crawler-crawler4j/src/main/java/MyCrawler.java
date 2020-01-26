import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

  private static final Logger LOG = LoggerFactory.getLogger(MyCrawler.class);
  private final static Pattern FILTERS = Pattern.compile(
      ".*(\\.(css|js|gif|jpg|png|mp3|mp4|zip|gz))$");

  /**
   * This method receives two parameters. The first parameter is the page
   * in which we have discovered this new url and the second parameter is
   * the new url. You should implement this function to specify whether
   * the given url should be crawled or not (based on your crawling logic).
   * In this example, we are instructing the crawler to ignore urls that
   * have css, js, git, ... extensions and to only accept urls that start
   * with "https://www.ics.uci.edu/". In this case, we didn't need the
   * referringPage parameter to make the decision.
   */
  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {
//    String href = url.getURL().toLowerCase();
//    return !FILTERS.matcher(href).matches()
//        && href.startsWith("https://www.ics.uci.edu/");
    return true;
  }

  /**
   * This function is called when a page is fetched and ready
   * to be processed by your program.
   */
  @Override
  public void visit(Page page) {
    final var storageDir = this.myController.getConfig().getCrawlStorageFolder();
//    final var parseData = page.getParseData();
//    System.out.println(parseData.getClass().getSimpleName());
    try {
//      Files.write(Paths.get("/tmp/log.txt"), page.getWebURL().toString().concat("\n").getBytes(),
//          StandardOpenOption.APPEND, StandardOpenOption.CREATE);
//      Runtime.getRuntime().exec("echo '" +  + "' >> /tmp/links.txt").waitFor();
      LOG.info("crawling={}", page.getWebURL().toString());
      final var pageStoragePath = Paths.get(storageDir, resolveFilename(page));
      Files.createDirectories(pageStoragePath.getParent());
//      Files.createDirectories(Paths.get(storageDir, page.getWebURL().getPath()).getParent());
      Files.write(pageStoragePath, page.getContentData());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
//    System.out.println(parseData);
//    String url = page.getWebURL().getURL();
//    System.out.println("URL: " + url);
//
//    if (page.getParseData() instanceof HtmlParseData) {
//      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//      String text = htmlParseData.getText();
//      String html = htmlParseData.getHtml();
//      Set<WebURL> links = htmlParseData.getOutgoingUrls();
//
//      System.out.println("Text length: " + text.length());
//      System.out.println("Html length: " + html.length());
//      System.out.println("Number of outgoing links: " + links.size());
//    }
  }

  private static String resolveFilename(Page page) {
    if(Pattern.compile("\\.\\w{2,}$").matcher(page.getWebURL().getPath()).find()){
      return page.getWebURL().getPath();
    }
    return page.getWebURL().getPath() + ".html";
  }

}
