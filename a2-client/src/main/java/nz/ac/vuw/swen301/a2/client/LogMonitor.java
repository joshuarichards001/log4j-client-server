package nz.ac.vuw.swen301.a2.client;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LogMonitor {
  JFrame frame;

  public LogMonitor()
  {
    // Frame of application
    frame = new JFrame();
    frame.setTitle("LOG Monitor");
    frame.setSize(500, 200);

    // Panel to hold content
    JPanel panel = new JPanel();
    panel.setBounds(0,0, 500, 300);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    frame.add(panel);

    // Panel to hold inputs/filters
    JPanel inputPanel = new JPanel();
    panel.add(inputPanel);

    // Level label
    JLabel levelLbl = new JLabel("min level:");
    inputPanel.add(levelLbl);

    // Level selector
    String[] choices = { "ALL", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "TRACE", "OFF"};
    final JComboBox<String> cb = new JComboBox<>(choices);
    inputPanel.add(cb);

    // Limit label
    JLabel limitLbl = new JLabel("limit:");
    inputPanel.add(limitLbl);

    // Limit text box
    JTextField tf = new JTextField("100", 10);
    inputPanel.add(tf);

    // Fetch data button (get request)
    JButton btn = new JButton("FETCH DATA");
    inputPanel.add(btn);

    // Table showing logs
    LogTableModel model = new LogTableModel();
    model.refreshTableData(Integer.parseInt(tf.getText()), Objects.requireNonNull(cb.getSelectedItem()).toString());
    JTable table = new JTable(model) {
      @Override
      public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(300, 100);
      }
    };
    panel.add(new JScrollPane(table));

    // Refresh table when button is pressed
    btn.addActionListener(e -> {
      model.refreshTableData(Integer.parseInt(tf.getText()), cb.getSelectedItem().toString());
      model.fireTableDataChanged();
    });

    // Make frame visible
    frame.setVisible(true);
  }

  public static void main(String[] args)
  {
    new LogMonitor();
  }
}
