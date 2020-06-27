package test.nz.ac.vuw.swen301.a2.server;

import com.google.gson.JsonObject;
import nz.ac.vuw.swen301.a2.server.LogsServlet;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TestPostLogs {

  @Test
  public void test_400_code() throws IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    new LogsServlet().doPost(request,response);

    assertEquals(400,response.getStatus());
  }

  @Test
  public void test_409_code() throws IOException {
    JsonObject obj = generateJson("debug eg", "DEBUG");

    MockHttpServletRequest request1 = new MockHttpServletRequest();
    MockHttpServletResponse response1 = new MockHttpServletResponse();
    request1.setContent(obj.toString().getBytes(StandardCharsets.UTF_8));
    LogsServlet servlet = new LogsServlet();
    servlet.doPost(request1,response1);

    MockHttpServletRequest request2 = new MockHttpServletRequest();
    MockHttpServletResponse response2 = new MockHttpServletResponse();
    request2.setContent(obj.toString().getBytes(StandardCharsets.UTF_8));
    servlet.doPost(request2,response2);

    assertEquals(409,response2.getStatus());
  }

  @Test
  public void test_201_code() throws IOException {
    JsonObject obj = generateJson("debug eg", "DEBUG");

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.setContent(obj.toString().getBytes(StandardCharsets.UTF_8));
    new LogsServlet().doPost(request,response);

    assertEquals(201,response.getStatus());
  }

  @Test
  public void test_post_response() throws IOException {
    JsonObject obj = generateJson("debug eg", "DEBUG");

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.setContent(obj.toString().getBytes(StandardCharsets.UTF_8));
    new LogsServlet().doPost(request,response);

    String result = response.getContentAsString();
    Assert.assertEquals(obj.toString(), result);
  }

  public JsonObject generateJson(String message, String level) {
    JsonObject obj = new JsonObject();
    obj.addProperty("id", String.valueOf(UUID.randomUUID()));
    obj.addProperty("message", message);
    obj.addProperty("timestamp", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(LocalDateTime.now()));
    obj.addProperty("thread", "main");
    obj.addProperty("logger", "com.example.Foo");
    obj.addProperty("level", level);
    obj.addProperty("errorDetails", "string");
    return obj;
  }
}
