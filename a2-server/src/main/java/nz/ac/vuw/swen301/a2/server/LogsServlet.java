package nz.ac.vuw.swen301.a2.server;

import com.google.gson.Gson;
import org.apache.log4j.Level;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class LogsServlet extends HttpServlet {

  static List<Log> logs;

  public enum LEVELS { OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL }

  public LogsServlet() {
    logs = new LinkedList<>();
  }

  /**
   * Gets the logs from logs that equal to or below the set level.
   * It also returns upto the amount defined in the limit
   */
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    resp.setContentType("application/json");

    // checks parameters are valid
    int limit; String level;
    try {
      limit = Integer.parseInt(req.getParameter("limit"));
      level = req.getParameter("level");
      if(limit <= 0 || !Level.toLevel(level).toString().equals(level)) throw new Exception();
    } catch (Exception e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    // gets a list of the desired logs
    List<Log> requestedLogs = new LinkedList<>();
    int count = 0;
    for (int i = logs.size()-1; i >= 0; i--) {
      if (LEVELS.valueOf(logs.get(i).level).ordinal() <= LEVELS.valueOf(level).ordinal()) {
        requestedLogs.add(logs.get(i));
        count++;
        if (count == limit) break;
      }
    }

    // responds with requested logs
    out.print(new Gson().toJson(requestedLogs));
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Puts the given log event into the logs list in memory
   */
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    resp.setContentType("application/json");
    Log logInput;

    // checks that input is valid
    try {
      logInput = new Gson().fromJson(req.getReader(), Log.class);
      if(logInput == null) throw new Exception();
    } catch(Exception e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    // checks no duplicate id's
    for (Log log : logs) {
      if(log.id.equals(logInput.id)) {
        resp.sendError(HttpServletResponse.SC_CONFLICT);
        return;
      }
    }

    // add to logs and responds with what was created
    logs.add(logInput);
    out.print(new Gson().toJson(logInput));
    resp.setStatus(HttpServletResponse.SC_CREATED);
  }
}


