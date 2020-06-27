package test.nz.ac.vuw.swen301.a2.server;

import com.google.gson.JsonObject;
import nz.ac.vuw.swen301.a2.server.LogsServlet;
import nz.ac.vuw.swen301.a2.server.StatsPNGServlet;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class TestStatsPNG {
  @Test
  public void test_1() throws IOException {
    LogsServlet servlet = new LogsServlet();
    StatsPNGServlet servletPNG = new StatsPNGServlet();

    JsonObject miscObj = generateJson("Test");
    MockHttpServletRequest postRequest = new MockHttpServletRequest();
    MockHttpServletResponse postResponse = new MockHttpServletResponse();
    postRequest.setContent(miscObj.toString().getBytes(StandardCharsets.UTF_8));
    servlet.doPost(postRequest, postResponse);

    MockHttpServletRequest getRequest = new MockHttpServletRequest();
    MockHttpServletResponse getResponse = new MockHttpServletResponse();
    servletPNG.doGet(getRequest,getResponse);

    String result = getResponse.getContentAsString();

    assert Objects.equals(getResponse.getContentType(), "image/png");
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
