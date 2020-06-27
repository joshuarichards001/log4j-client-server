package nz.ac.vuw.swen301.a2.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class StatsCSVServlet extends HttpServlet {

  public StatsCSVServlet() { }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    resp.setContentType("text/csv");
    resp.setHeader("Content-Disposition", "attachment; filename=statcsv.csv");
    tableToCSVString(out);
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  public void tableToCSVString(PrintWriter out) {
    LogStatsTable table = new LogStatsTable();
    for (ArrayList<String> a : table.table) {
      for (int i = 0; i < a.size(); i++) {
        if (i == a.size()-1) {
          out.print(a.get(i));
        } else {
          out.print(a.get(i) + "\t");
        }
      }
      out.print("\n");
    }
  }
}
