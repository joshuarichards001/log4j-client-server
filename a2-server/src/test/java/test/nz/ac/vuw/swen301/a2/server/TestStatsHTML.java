package test.nz.ac.vuw.swen301.a2.server;

import com.google.gson.JsonObject;
import nz.ac.vuw.swen301.a2.server.LogsServlet;
import nz.ac.vuw.swen301.a2.server.StatsServlet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class TestStatsHTML {
  @Test
  public void test_1() throws IOException {
    LogsServlet servlet = new LogsServlet();
    StatsServlet servletHTML = new StatsServlet();

    JsonObject miscObj = generateJson("Test");
    MockHttpServletRequest postRequest = new MockHttpServletRequest();
    MockHttpServletResponse postResponse = new MockHttpServletResponse();
    postRequest.setContent(miscObj.toString().getBytes(StandardCharsets.UTF_8));
    servlet.doPost(postRequest, postResponse);

    MockHttpServletRequest getRequest = new MockHttpServletRequest();
    MockHttpServletResponse getResponse = new MockHttpServletResponse();
    servletHTML.doGet(getRequest,getResponse);

    String result = getResponse.getContentAsString();

    Element tb = Jsoup.parse(result).select("table").get(0);
    Elements r = tb.select("tr");

    String[][] values = new String[4][2];

    for (int i = 0; i < r.size(); i++) {
      Element row = r.get(i);
      Elements cols = row.select("td");
      for (int j = 0; j < cols.size(); j++) {
        values[i][j] = cols.get(j).text();
      }
    }

    assert values[1][0].equals("com.example.Foo");
    assert values[1][1].equals("1");
    assert values[2][0].equals("DEBUG");
    assert values[2][1].equals("1");
    assert values[3][0].equals("main");
    assert values[3][1].equals("1");
    assert Objects.equals(getResponse.getContentType(), "text/html");
  }

  public JsonObject generateJson(String message) {
    JsonObject obj = new JsonObject();
    obj.addProperty("id", String.valueOf(UUID.randomUUID()));
    obj.addProperty("message", message);
    obj.addProperty("timestamp", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(LocalDateTime.now()));
    obj.addProperty("thread", "main");
    obj.addProperty("logger", "com.example.Foo");
    obj.addProperty("level", "DEBUG");
    obj.addProperty("errorDetails", "string");
    return obj;
  }
}
