package nz.ac.vuw.swen301.a2.server;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class StatsXLSServlet extends HttpServlet {
  public StatsXLSServlet() { }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    OutputStream out = resp.getOutputStream();
    resp.setContentType("application/vnd.ms-excel");
    tableToXLS(out);
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  public void tableToXLS(OutputStream out) {
    try {
      LogStatsTable table = new LogStatsTable();
      HSSFWorkbook workbook = new HSSFWorkbook();
      Sheet sheet = workbook.createSheet();
      for (int i = 0; i < table.table.size(); i++) {
        Row row = sheet.createRow((short)i);
        for (int j = 0; j < table.table.get(i).size(); j++) {
          row.createCell(j).setCellValue(table.table.get(i).get(j));
        }
      }
      workbook.write(out);
      workbook.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
