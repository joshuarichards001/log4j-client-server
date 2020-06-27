package test.nz.ac.vuw.swen301.a2.server;

import com.google.gson.JsonObject;
import nz.ac.vuw.swen301.a2.server.LogsServlet;
import nz.ac.vuw.swen301.a2.server.StatsXLSServlet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class TestStatsXLS {
  @Test
  public void test_1() throws IOException {
    LogsServlet servlet = new LogsServlet();
    StatsXLSServlet servletXLS = new StatsXLSServlet();

    JsonObject miscObj = generateJson("Test");
    MockHttpServletRequest postRequest = new MockHttpServletRequest();
    MockHttpServletResponse postResponse = new MockHttpServletResponse();
    postRequest.setContent(miscObj.toString().getBytes(StandardCharsets.UTF_8));
    servlet.doPost(postRequest, postResponse);

    MockHttpServletRequest getRequest = new MockHttpServletRequest();
    MockHttpServletResponse getResponse = new MockHttpServletResponse();
    servletXLS.doGet(getRequest,getResponse);

    InputStream result = new ByteArrayInputStream(getResponse.getContentAsByteArray());

    String[][] values = new String[4][2];
    HSSFWorkbook workbook = (HSSFWorkbook)WorkbookFactory.create(result);
    DataFormatter dataFormatter = new DataFormatter();
    for (Row row: workbook.getSheetAt(0)) {
      for(Cell cell: row) {
        values[row.getRowNum()][cell.getColumnIndex()]=dataFormatter.formatCellValue(cell);
      }
    }

    assert values[1][0].equals("com.example.Foo");
    assert values[1][1].equals("1");
    assert values[2][0].equals("DEBUG");
    assert values[2][1].equals("1");
    assert values[3][0].equals("main");
    assert values[3][1].equals("1");
    assert Objects.equals(getResponse.getContentType(), "application/vnd.ms-excel");
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
