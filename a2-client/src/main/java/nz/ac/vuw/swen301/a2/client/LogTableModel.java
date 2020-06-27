package nz.ac.vuw.swen301.a2.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class LogTableModel extends AbstractTableModel {

  private List<Log> logData;
  private final String[] columnNames = { "time", "level", "logger", "thread", "message" };

  public LogTableModel() {

  }

  @Override
  public String getColumnName(int column) {
    return columnNames[column];
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public int getRowCount() {
    return logData.size();
  }

  @Override
  public Object getValueAt(int row, int column) {
    Object userAttribute = null;
    Log logObject = logData.get(row);
    switch(column) {
      case 0: userAttribute = logObject.timestamp; break;
      case 1: userAttribute = logObject.level; break;
      case 2: userAttribute = logObject.logger; break;
      case 3: userAttribute = logObject.thread; break;
      case 4: userAttribute = logObject.message; break;
      default: break;
    }
    return userAttribute;
  }

  public void refreshTableData(int limit, String level) {
    try {
      URIBuilder builder = new URIBuilder();
      builder.setScheme("http")
          .setHost("localhost")
          .setPort(8080)
          .setPath("resthome4logs/logs")
          .setParameter("limit", Integer.toString(limit))
          .setParameter("level", level);

      URI logServiceURL = builder.build();
      HttpClient httpClient = HttpClientBuilder.create().build();
      HttpGet get = new HttpGet(logServiceURL);
      HttpResponse response = httpClient.execute(get);

      String result = new BasicResponseHandler().handleResponse(response);
      Type listType = new TypeToken<ArrayList<Log>>() {}.getType();
      logData = new Gson().fromJson(result, listType);

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}