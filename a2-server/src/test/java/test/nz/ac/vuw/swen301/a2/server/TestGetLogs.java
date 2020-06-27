package test.nz.ac.vuw.swen301.a2.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import nz.ac.vuw.swen301.a2.server.Log;
import nz.ac.vuw.swen301.a2.server.LogsServlet;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.*;

public class TestGetLogs {

  @Test
  public void test_400_code_1() throws IOException {

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    LogsServlet servlet = new LogsServlet();
    servlet.doGet(request,response);

    assertEquals(400,response.getStatus());
  }

  @Test
  public void test_400_code_2() throws IOException {

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.setParameter("limit", "0");
    request.setParameter("level", "DEBUG");
    LogsServlet servlet = new LogsServlet();
    servlet.doGet(request,response);

    assertEquals(400,response.getStatus());
  }

  @Test
  public void test_400_code_3() throws IOException {

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.setParameter("limit", "-1");
    request.setParameter("level", "DEBUG");
    LogsServlet servlet = new LogsServlet();
    servlet.doGet(request,response);

    assertEquals(400,response.getStatus());
  }

  @Test
  public void test_400_code_4() throws IOException {

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.setParameter("limit", "10");
    request.setParameter("level", "HI");
    LogsServlet servlet = new LogsServlet();
    servlet.doGet(request,response);

    assertEquals(400,response.getStatus());
  }

  @Test
  public void test_200_code() throws IOException {

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.setParameter("limit", "10");
    request.setParameter("level", "DEBUG");
    LogsServlet servlet = new LogsServlet();
    servlet.doGet(request,response);

    assertEquals(200,response.getStatus());
  }

  @Test
  public void test_limit_not_reached() throws IOException {
    LogsServlet servlet = new LogsServlet();
    JsonObject obj = generateJson("debug eg", "DEBUG");
    MockHttpServletRequest postRequest = new MockHttpServletRequest();
    MockHttpServletResponse postResponse = new MockHttpServletResponse();
    postRequest.setContent(obj.toString().getBytes(StandardCharsets.UTF_8));
    servlet.doPost(postRequest, postResponse);

    for(int i = 0; i < 5; i++) {
      JsonObject miscObj = generateJson(i+"::debug eg", "DEBUG");
      MockHttpServletRequest postRequestLoop = new MockHttpServletRequest();
      MockHttpServletResponse postResponseLoop = new MockHttpServletResponse();
      postRequestLoop.setContent(miscObj.toString().getBytes(StandardCharsets.UTF_8));
      servlet.doPost(postRequestLoop, postResponseLoop);
    }

    MockHttpServletRequest getRequest = new MockHttpServletRequest();
    MockHttpServletResponse getResponse = new MockHttpServletResponse();
    getRequest.setParameter("limit", "10");
    getRequest.setParameter("level", "DEBUG");

    servlet.doGet(getRequest,getResponse);

    String result = getResponse.getContentAsString();
    Type listType = new TypeToken<ArrayList<Log>>() {}.getType();
    ArrayList<Log> logList = new Gson().fromJson(result, listType);
    Collections.reverse(logList);
    Log objAsLog = new Gson().fromJson(obj, Log.class);

    // just for testing to see what is received by the get
    System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(logList));

    assertTrue(logList.stream().anyMatch(o -> o.id.equals(objAsLog.id)));
  }

  @Test
  public void test_limit_reached() throws IOException {
    LogsServlet servlet = new LogsServlet();
    JsonObject obj = generateJson("debug eg", "DEBUG");
    MockHttpServletRequest postRequest = new MockHttpServletRequest();
    MockHttpServletResponse postResponse = new MockHttpServletResponse();
    postRequest.setContent(obj.toString().getBytes(StandardCharsets.UTF_8));
    servlet.doPost(postRequest, postResponse);

    for(int i = 0; i < 5; i++) {
      JsonObject miscObj = generateJson(i+"::debug eg", "DEBUG");
      MockHttpServletRequest postRequestLoop = new MockHttpServletRequest();
      MockHttpServletResponse postResponseLoop = new MockHttpServletResponse();
      postRequestLoop.setContent(miscObj.toString().getBytes(StandardCharsets.UTF_8));
      servlet.doPost(postRequestLoop, postResponseLoop);
    }

    MockHttpServletRequest getRequest = new MockHttpServletRequest();
    MockHttpServletResponse getResponse = new MockHttpServletResponse();
    getRequest.setParameter("limit", "5");
    getRequest.setParameter("level", "DEBUG");

    servlet.doGet(getRequest,getResponse);

    String result = getResponse.getContentAsString();
    Type listType = new TypeToken<ArrayList<Log>>() {}.getType();
    ArrayList<Log> logList = new Gson().fromJson(result, listType);
    Collections.reverse(logList);
    Log objAsLog = new Gson().fromJson(obj, Log.class);

    assertFalse(logList.stream().anyMatch(o -> o.id.equals(objAsLog.id)));
  }

  @Test
  public void test_above_level() throws IOException {
    LogsServlet servlet = new LogsServlet();
    JsonObject obj = generateJson("error eg", "DEBUG");
    MockHttpServletRequest postRequest = new MockHttpServletRequest();
    MockHttpServletResponse postResponse = new MockHttpServletResponse();
    postRequest.setContent(obj.toString().getBytes(StandardCharsets.UTF_8));
    servlet.doPost(postRequest, postResponse);

    for(int i = 0; i < 5; i++) {
      JsonObject miscObj = generateJson(i+"::debug eg", "ERROR");
      MockHttpServletRequest postRequestLoop = new MockHttpServletRequest();
      MockHttpServletResponse postResponseLoop = new MockHttpServletResponse();
      postRequestLoop.setContent(miscObj.toString().getBytes(StandardCharsets.UTF_8));
      servlet.doPost(postRequestLoop, postResponseLoop);
    }

    MockHttpServletRequest getRequest = new MockHttpServletRequest();
    MockHttpServletResponse getResponse = new MockHttpServletResponse();
    getRequest.setParameter("limit", "10");
    getRequest.setParameter("level", "WARN");

    servlet.doGet(getRequest,getResponse);

    String result = getResponse.getContentAsString();
    Type listType = new TypeToken<ArrayList<Log>>() {}.getType();
    ArrayList<Log> logList = new Gson().fromJson(result, listType);
    Collections.reverse(logList);
    Log objAsLog = new Gson().fromJson(obj, Log.class);

    assertFalse(logList.stream().anyMatch(o -> o.id.equals(objAsLog.id)));
  }

  @Test
  public void test_every_level() throws IOException {
    LogsServlet servlet = new LogsServlet();
    MockHttpServletRequest postRequest = new MockHttpServletRequest();
    MockHttpServletResponse postResponse = new MockHttpServletResponse();

    JsonObject debugObj = generateJson("debug eg", "DEBUG");
    postRequest.setContent(debugObj.toString().getBytes(StandardCharsets.UTF_8));
    servlet.doPost(postRequest, postResponse);
    JsonObject infoObj = generateJson("info eg", "INFO");
    postRequest.setContent(infoObj.toString().getBytes(StandardCharsets.UTF_8));
    servlet.doPost(postRequest, postResponse);
    JsonObject warnObj = generateJson("warn eg", "WARN");
    postRequest.setContent(warnObj.toString().getBytes(StandardCharsets.UTF_8));
    servlet.doPost(postRequest, postResponse);
    JsonObject errorObj = generateJson("error eg", "ERROR");
    postRequest.setContent(errorObj.toString().getBytes(StandardCharsets.UTF_8));
    servlet.doPost(postRequest, postResponse);
    JsonObject fatalObj = generateJson("fatal eg", "FATAL");
    postRequest.setContent(fatalObj.toString().getBytes(StandardCharsets.UTF_8));
    servlet.doPost(postRequest, postResponse);
    JsonObject traceObj = generateJson("trace eg", "TRACE");
    postRequest.setContent(traceObj.toString().getBytes(StandardCharsets.UTF_8));
    servlet.doPost(postRequest, postResponse);

    MockHttpServletRequest getRequest = new MockHttpServletRequest();
    MockHttpServletResponse getResponse = new MockHttpServletResponse();
    getRequest.setParameter("limit", "10");
    getRequest.setParameter("level", "DEBUG");

    servlet.doGet(getRequest, getResponse);

    String result = getResponse.getContentAsString();
    Type listType = new TypeToken<ArrayList<Log>>() {
    }.getType();
    ArrayList<Log> logList = new Gson().fromJson(result, listType);
    Collections.reverse(logList);
    Log objAsLog = new Gson().fromJson(debugObj, Log.class);

    // just for testing to see what is received by the get
    System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(logList));

    assertTrue(logList.stream().anyMatch(o -> o.id.equals(objAsLog.id)));
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
