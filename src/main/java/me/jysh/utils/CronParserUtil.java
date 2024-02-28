package me.jysh.utils;

import static me.jysh.constant.CronConstants.COMMAND_INDEX;
import static me.jysh.constant.CronConstants.DAY_OF_MONTH_INDEX;
import static me.jysh.constant.CronConstants.DAY_OF_MONTH_MAX_VALUE;
import static me.jysh.constant.CronConstants.DAY_OF_MONTH_MIN_VALUE;
import static me.jysh.constant.CronConstants.DAY_OF_WEEK_INDEX;
import static me.jysh.constant.CronConstants.DAY_OF_WEEK_MAX_VALUE;
import static me.jysh.constant.CronConstants.DAY_OF_WEEK_MIN_VALUE;
import static me.jysh.constant.CronConstants.HOUR_INDEX;
import static me.jysh.constant.CronConstants.HOUR_MAX_VALUE;
import static me.jysh.constant.CronConstants.HOUR_MIN_VALUE;
import static me.jysh.constant.CronConstants.MINUTE_INDEX;
import static me.jysh.constant.CronConstants.MINUTE_MAX_VALUE;
import static me.jysh.constant.CronConstants.MINUTE_MIN_VALUE;
import static me.jysh.constant.CronConstants.MONTH_INDEX;
import static me.jysh.constant.CronConstants.MONTH_MAX_VALUE;
import static me.jysh.constant.CronConstants.MONTH_MIN_VALUE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import me.jysh.exception.InvalidExpressionException;
import me.jysh.models.CronExpression;
import me.jysh.models.CronExpression.CronExpressionBuilder;

/**
 * Utility class for parsing cron expressions and building {@link CronExpression} objects.
 */
public class CronParserUtil {

  /**
   * Parses the given cron expression and returns a {@link CronExpression} object.
   *
   * @param expressions The cron expression to parse.
   * @return A {@link CronExpression} object representing the parsed expression.
   * @throws InvalidExpressionException If the expression is invalid.
   */
  public static CronExpression parse(final String expressions) {
    final String[] splitExpressions = expressions.split(" ");

    if (splitExpressions.length != 6) {
      throw new InvalidExpressionException("Usage: cron-parser <expression> <command>.");
    }

    final CronExpressionBuilder cronExpressionBuilder = CronExpression.builder();

    final List<Integer> minute = parseComponent("minute", splitExpressions[MINUTE_INDEX],
        MINUTE_MIN_VALUE, MINUTE_MAX_VALUE);
    cronExpressionBuilder.minute(minute);

    final List<Integer> hour = parseComponent("hour", splitExpressions[HOUR_INDEX], HOUR_MIN_VALUE,
        HOUR_MAX_VALUE);
    cronExpressionBuilder.hour(hour);

    final List<Integer> dayOfMonth = parseComponent("day_of_month",
        splitExpressions[DAY_OF_MONTH_INDEX], DAY_OF_MONTH_MIN_VALUE, DAY_OF_MONTH_MAX_VALUE);
    cronExpressionBuilder.dayOfMonth(dayOfMonth);

    final List<Integer> month = parseComponent("month", splitExpressions[MONTH_INDEX],
        MONTH_MIN_VALUE, MONTH_MAX_VALUE);
    cronExpressionBuilder.month(month);

    final List<Integer> dayOfWeek = parseComponent("day_of_week",
        splitExpressions[DAY_OF_WEEK_INDEX], DAY_OF_WEEK_MIN_VALUE, DAY_OF_WEEK_MAX_VALUE);
    cronExpressionBuilder.dayOfWeek(dayOfWeek);

    final String command = splitExpressions[COMMAND_INDEX];
    if (Objects.equals(command, "")) {
      throw new InvalidExpressionException("command cannot be empty");
    }
    cronExpressionBuilder.command(command);

    return cronExpressionBuilder.build();
  }

  /**
   * Parses a specific component of the cron expression.
   *
   * @param component  The name of the component (e.g., "minute", "hour").
   * @param expression The expression for the component.
   * @param minValue   The minimum allowed value for the component.
   * @param maxValue   The maximum allowed value for the component.
   * @return A list of integers representing the parsed values for the component.
   * @throws InvalidExpressionException If the expression is invalid.
   */
  private static List<Integer> parseComponent(String component, String expression, int minValue,
      int maxValue) {
    final List<Integer> result;
    try {
      result = parseExpression(expression, minValue, maxValue);
    } catch (Exception e) {
      throw new InvalidExpressionException("Invalid " + component + " value.");
    }
    if (result == null || result.isEmpty()) {
      throw new InvalidExpressionException("Invalid " + component + " value.");
    }
    return result;
  }

  /**
   * Parses a generic cron expression and returns a list of integers representing the values.
   *
   * @param expression The cron expression to parse.
   * @param minValue   The minimum allowed value for the expression.
   * @param maxValue   The maximum allowed value for the expression.
   * @return A list of integers representing the parsed values for the expression.
   */
  private static List<Integer> parseExpression(String expression, int minValue, int maxValue) {
    if (Objects.equals(expression, "*")) {
      return IntStream.rangeClosed(minValue, maxValue).boxed().collect(Collectors.toList());
    }

    List<Integer> result = parseDashPattern(expression, minValue, maxValue);
    if (!result.isEmpty()) {
      return result;
    }

    result = parseCommaPattern(expression, minValue, maxValue);
    if (!result.isEmpty()) {
      return result;
    }

    result = parseIntervalPattern(expression, minValue, maxValue);
    if (!result.isEmpty()) {
      return result;
    }

    return new ArrayList<>();
  }

  /**
   * Parses a dash pattern in the cron expression.
   *
   * @param expression The dash pattern to parse.
   * @param minValue   The minimum allowed value for the expression.
   * @param maxValue   The maximum allowed value for the expression.
   * @return A list of integers representing the parsed values for the dash pattern.
   */
  private static List<Integer> parseDashPattern(String expression, int minValue, int maxValue) {
    Pattern dashPattern = Pattern.compile("^(\\d{1,2})-(\\d{1,2})$");
    Matcher dashMatcher = dashPattern.matcher(expression);
    if (dashMatcher.find()) {
      int min = Integer.parseInt(dashMatcher.group(1));
      int max = Integer.parseInt(dashMatcher.group(2));
      final List<Integer> values = IntStream.rangeClosed(min, max).boxed()
          .collect(Collectors.toList());
      checkValueRange(values, minValue, maxValue);
      return values;
    }
    return Collections.emptyList();
  }

  /**
   * Parses a comma pattern in the cron expression.
   *
   * @param expression The comma pattern to parse.
   * @param minValue   The minimum allowed value for the expression.
   * @param maxValue   The maximum allowed value for the expression.
   * @return A list of integers representing the parsed values for the comma pattern.
   */
  private static List<Integer> parseCommaPattern(String expression, int minValue, int maxValue) {
    Pattern commaPattern = Pattern.compile("^\\d{1,2}(?:,\\d{1,2})*$");
    Matcher commaMatcher = commaPattern.matcher(expression);
    if (commaMatcher.find()) {
      List<Integer> values = Arrays.stream(commaMatcher.group().split(",")).map(Integer::parseInt)
          .collect(Collectors.toList());
      checkValueRange(values, minValue, maxValue);
      return values;
    }
    return Collections.emptyList();
  }

  /**
   * Parses an interval pattern in the cron expression.
   *
   * @param expression The interval pattern to parse.
   * @param minValue   The minimum allowed value for the expression.
   * @param maxValue   The maximum allowed value for the expression.
   * @return A list of integers representing the parsed values for the interval pattern.
   */
  private static List<Integer> parseIntervalPattern(String expression, int minValue, int maxValue) {
    Pattern intervalPattern = Pattern.compile("^(\\*|\\d{1,2}-\\d{1,2})/(\\d{1,2})$");
    Matcher intervalMatcher = intervalPattern.matcher(expression);
    if (intervalMatcher.find()) {
      List<Integer> options = parseExpression(intervalMatcher.group(1), minValue, maxValue);
      int interval = Integer.parseInt(intervalMatcher.group(2));
      List<Integer> values = options.stream().filter(i -> i % interval == 0)
          .collect(Collectors.toList());
      checkValueRange(values, minValue, maxValue);
      return values;
    }
    return Collections.emptyList();
  }

  /**
   * Checks if the values in the given list fall within the specified range.
   *
   * @param values   The list of values to check.
   * @param minValue The minimum allowed value.
   * @param maxValue The maximum allowed value.
   * @throws InvalidExpressionException If any value in the list is outside the specified range.
   */
  private static void checkValueRange(List<Integer> values, int minValue, int maxValue) {
    boolean hasInvalidValues = values.stream()
        .anyMatch(value -> value < minValue || value > maxValue);
    if (hasInvalidValues) {
      throw new InvalidExpressionException("expression has invalid values.");
    }
  }
}
