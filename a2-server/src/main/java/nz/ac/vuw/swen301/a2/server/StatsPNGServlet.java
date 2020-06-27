package nz.ac.vuw.swen301.a2.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class StatsPNGServlet extends HttpServlet {

  public enum LEVELS { FATAL, ERROR, WARN, INFO, DEBUG, TRACE }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    OutputStream out = resp.getOutputStream();
    resp.setContentType("image/png");
    try {
      ChartUtils.writeChartAsPNG(out, tableToPNG(), 450, 400);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  public JFreeChart tableToPNG() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    int[] levelFreqs = getLevelFrequency();
    for (int i = 0; i < levelFreqs.length; i++) {
      dataset.setValue(levelFreqs[i], "Frequency", LEVELS.values()[i]);
    }
    return ChartFactory.createBarChart(
        "Log Levels",
        "",
        "Frequency",
        dataset,
        PlotOrientation.VERTICAL,
        false, true, false
    );
  }

  public int[] getLevelFrequency() {
    int[] levelFreq = new int[6];
    for (Log l : LogsServlet.logs) {
      switch (l.level) {
        case "FATAL":
          levelFreq[0]++;
          break;
        case "ERROR":
          levelFreq[1]++;
          break;
        case "WARN":
          levelFreq[2]++;
          break;
        case "INFO":
          levelFreq[3]++;
          break;
        case "DEBUG":
          levelFreq[4]++;
          break;
        case "TRACE":
          levelFreq[5]++;
          break;
        default:
          break;
      }
    }
    return levelFreq;
  }
}
