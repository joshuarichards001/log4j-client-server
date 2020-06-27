package nz.ac.vuw.swen301.a2.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class StatsServlet extends HttpServlet {

  public StatsServlet() { }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    resp.setContentType("text/html");
    tableToHTMLString(out);
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  public void tableToHTMLString(PrintWriter out) {
    LogStatsTable table = new LogStatsTable();

    out.println("<html>");
    out.println("<body>");
    out.println("<table>");
    for (ArrayList<String> a : table.table) {
      out.println("<tr>");
      for (String s : a) {
        out.print("<td>");
        out.print(s);
        out.println("</td>");
      }
      out.println("</tr>");
    }
    out.println("</table>");
    out.println("</body>");
    out.println("</html>");
    out.close();
  }
}
