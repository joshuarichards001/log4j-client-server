package nz.ac.vuw.swen301.a2.client;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

public class Resthome4LogsAppender extends AppenderSkeleton {

  static List<Log> logs;
  private static URI logServiceURL;

  public Resthome4LogsAppender() {
    this.setName("Resthome4LogsAppender");
    this.activateOptions();
    logs = new LinkedList<>();
  }

  public static String generateJson(LoggingEvent log) {
    return new Gson().toJson(new Log(log));
  }

  @Override
  protected void append(LoggingEvent loggingEvent) {
    try {
      URIBuilder builder = new URIBuilder();
      builder.setScheme("http").setHost("localhost").setPort(8080).setPath("resthome4logs/logs");
      logServiceURL = builder.build();

      HttpClient httpClient = HttpClientBuilder.create().build();
      HttpPost post = new HttpPost(logServiceURL);
      post.setEntity(new StringEntity(generateJson(loggingEvent)));
      HttpResponse response = httpClient.execute(post);

      if(response.getStatusLine().getStatusCode()!=201) throw new Exception();

      String content = EntityUtils.toString(response.getEntity());
      System.out.println("Content = "+content);
    } catch (Exception e) {
      System.out.println("Append Error = "+e.getMessage());
    }
  }

  @Override
  public void close() {

  }

  @Override
  public boolean requiresLayout() {
    return false;
  }

  public static URI getLogServiceURL() {
    return logServiceURL;
  }

  public static void setLogServiceURL(URI logServiceURL) {
    Resthome4LogsAppender.logServiceURL = logServiceURL;
  }
}
