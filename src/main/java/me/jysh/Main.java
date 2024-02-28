package me.jysh;

import me.jysh.models.CronExpression;
import me.jysh.utils.CronParserUtil;

public class Main {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Usage: cron-parser.sh <cronExpression>");
      System.exit(1);
    }

    try {
      final CronExpression cronExpression = CronParserUtil.parse(args[0]);
      System.out.println(cronExpression);
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      System.out.println();
      System.exit(1);
    }
  }
}