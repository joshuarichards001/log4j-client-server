package nz.ac.vuw.swen301.a2.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LogStatsTable {

  ArrayList<ArrayList<String>> table = new ArrayList<>();
  List<Log> logs;

  public LogStatsTable() {
    try {
      logs = LogsServlet.logs;
      makeColumnLabels();
      makeRows();
      fillTableBlanks();
      for (Log l : logs) {
        for (ArrayList<String> a : table) {
          if (l.logger.equals(a.get(0)) || l.level.equals(a.get(0)) || l.thread.equals(a.get(0))) {
            int index = dateToIndex(l.timestamp);
            a.set(index, Integer.toString(Integer.parseInt(a.get(index)) + 1));
          }
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void makeColumnLabels() throws ParseException {
    ArrayList<String> columnLabels = new ArrayList<>();
    columnLabels.add("");
    for (int i = 0; i <= logs.size()-1; i++) {
      String date = new SimpleDateFormat("yyyy-MM-dd")
          .format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(logs.get(i).timestamp));
      if (!columnLabels.contains(date)) {
        columnLabels.add(date);
      }
    }
    table.add(columnLabels);
  }

  public void makeRows() {
    List<String> content = new ArrayList<>();
    for (int i = 0; i <= logs.size()-1; i++) {
      Log log = logs.get(i);
      if (!content.contains(log.logger)) {
        content.add(log.logger);
        table.add(new ArrayList<String>(){{add(log.logger);}});
      }
      if (!content.contains(log.level)) {
        content.add(log.level);
        table.add(new ArrayList<String>(){{add(log.level);}});
      }
      if (!content.contains(log.thread)) {
        content.add(log.thread);
        table.add(new ArrayList<String>(){{add(log.thread);}});
      }
    }
  }

  public void fillTableBlanks() {
    for (ArrayList<String> a : table.subList( 1, table.size())) {
      for (int i = 0; i < table.get(0).size()-1; i++) {
        a.add("0");
      }
    }
  }

  public int dateToIndex(String date) throws ParseException {
    return table.get(0).indexOf(new SimpleDateFormat("yyyy-MM-dd")
        .format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(date)));
  }
}
