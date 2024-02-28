package me.jysh.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class CronConstants {

  public static final int MINUTE_INDEX = 0;
  public static final int MINUTE_MIN_VALUE = 0;
  public static final int MINUTE_MAX_VALUE = 59;

  public static final int HOUR_INDEX = 1;
  public static final int HOUR_MIN_VALUE = 0;
  public static final int HOUR_MAX_VALUE = 23;

  public static final int DAY_OF_MONTH_INDEX = 2;
  public static final int DAY_OF_MONTH_MIN_VALUE = 1;
  public static final int DAY_OF_MONTH_MAX_VALUE = 31;

  public static final int MONTH_INDEX = 3;
  public static final int MONTH_MIN_VALUE = 1;
  public static final int MONTH_MAX_VALUE = 12;

  public static final int DAY_OF_WEEK_INDEX = 4;
  public static final int DAY_OF_WEEK_MIN_VALUE = 0;
  public static final int DAY_OF_WEEK_MAX_VALUE = 6;

  public static final int COMMAND_INDEX = 5;
}
