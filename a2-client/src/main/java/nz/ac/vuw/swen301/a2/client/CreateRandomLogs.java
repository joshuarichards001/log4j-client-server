package nz.ac.vuw.swen301.a2.client;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CreateRandomLogs {

  public enum LEVELS { FATAL, ERROR, WARN, INFO, DEBUG, TRACE }

  public static void main(String[] args) {
    Logger logger = Logger.getLogger(CreateRandomLogs.class.getName());
    Resthome4LogsAppender appender = new Resthome4LogsAppender();
    logger.addAppender(appender);
    logger.setLevel(Level.ALL);

    while (true) {
      try {
        logger.log(generateRandomLevel(), generateRandomString());
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        System.out.println("Random Logs Error = "+e.getMessage());
      }
    }
  }

  public static Priority generateRandomLevel() {
    return Level.toLevel(LEVELS.values()[new Random().nextInt(LEVELS.values().length)].toString());
  }

  public static String generateRandomString() {
    return Math.random() > 0.7 ? "All good" : Math.random() > 0.3 ? "Not good" : "Disaster";
  }
}
