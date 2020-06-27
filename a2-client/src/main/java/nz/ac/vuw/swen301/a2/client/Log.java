package nz.ac.vuw.swen301.a2.client;

import org.apache.log4j.spi.LoggingEvent;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Log {
  public String id;
  public String message;
  public String timestamp;
  public String thread;
  public String logger;
  public String level;
  public String errorDetails;

  public Log(LoggingEvent loggingEvent) {
    this.id = UUID.randomUUID().toString();
    this.message = loggingEvent.getMessage().toString();
    this.timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .format(new Timestamp(loggingEvent.getTimeStamp()).toLocalDateTime());
    this.thread = loggingEvent.getThreadName();
    this.logger = loggingEvent.getLoggerName();
    this.level = loggingEvent.getLevel().toString();
    this.errorDetails = "string";
  }
}
