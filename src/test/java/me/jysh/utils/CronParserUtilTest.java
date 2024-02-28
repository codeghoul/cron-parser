package me.jysh.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import me.jysh.exception.InvalidExpressionException;
import me.jysh.models.CronExpression;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CronParserUtilTest {

  private static Stream<Arguments> getValidTests() {
    final Arguments successCase0 = Arguments.of("*/15 0 1,15 * 1-5 /usr/bin/find",
        CronExpression.builder().minute(List.of(0, 15, 30, 45)).hour(singleValue(0))
            .dayOfMonth(List.of(1, 15))
            .month(range(1, 12)).dayOfWeek(range(1, 5)).command("/usr/bin/find").build());

    final Arguments successCase1 = Arguments.of("* * * * * command1",
        CronExpression.builder().minute(range(0, 59)).hour(range(0, 23)).dayOfMonth(range(1, 31))
            .month(range(1, 12)).dayOfWeek(range(0, 6)).command("command1").build());

    final Arguments successCase2 = Arguments.of("30 8 * * 1-5 command2",
        CronExpression.builder().minute(singleValue(30)).hour(singleValue(8))
            .dayOfMonth(range(1, 31)).month(range(1, 12)).dayOfWeek(range(1, 5)).command("command2")
            .build());

    final Arguments successCase3 = Arguments.of("0 * * * * command3",
        CronExpression.builder().minute(singleValue(0)).hour(range(0, 23)).dayOfMonth(range(1, 31))
            .month(range(1, 12)).dayOfWeek(range(0, 6)).command("command3").build());

    final Arguments successCase4 = Arguments.of("15 * * * * command4",
        CronExpression.builder().minute(singleValue(15)).hour(range(0, 23)).dayOfMonth(range(1, 31))
            .month(range(1, 12)).dayOfWeek(range(0, 6)).command("command4").build());

    final Arguments successCase5 = Arguments.of("0 0 * * * command5",
        CronExpression.builder().minute(singleValue(0)).hour(singleValue(0))
            .dayOfMonth(range(1, 31)).month(range(1, 12)).dayOfWeek(range(0, 6)).command("command5")
            .build());

    return Stream.of(successCase0, successCase1, successCase2, successCase3, successCase4,
        successCase5);
  }

  private static Stream<Arguments> getInvalidTests() {
    final Arguments invalidMinuteCase = Arguments.of("54/15 * * * * command1",
        "Invalid minute value.");
    final Arguments invalidHourCase = Arguments.of("* 24 * * * command2", "Invalid hour value.");
    final Arguments invalidDayOfMonthCase = Arguments.of("* * 32 * * command3",
        "Invalid day_of_month value.");
    final Arguments invalidMonthCase = Arguments.of("* * * 13 * command4", "Invalid month value.");
    final Arguments invalidDayOfWeekCase = Arguments.of("* * * * 7 command5",
        "Invalid day_of_week value.");
    final Arguments invalidArgumentsCase = Arguments.of("* * * * *",
        "Usage: cron-parser <expression> <command>.");

    return Stream.of(invalidMinuteCase, invalidHourCase, invalidDayOfMonthCase, invalidMonthCase,
        invalidDayOfWeekCase, invalidArgumentsCase);
  }


  private static List<Integer> range(int min, int max) {
    return IntStream.rangeClosed(min, max).boxed().collect(Collectors.toList());
  }

  private static List<Integer> singleValue(int value) {
    return Collections.singletonList(value);
  }


  @ParameterizedTest(name = "{0}")
  @MethodSource("getValidTests")
  void parse_ShouldPass(String cronExpression, CronExpression expectedParsedCronExpression) {
    CronExpression parsedCronExpression = CronParserUtil.parse(cronExpression);
    assertEquals(expectedParsedCronExpression.toString(), parsedCronExpression.toString());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("getInvalidTests")
  void parse_ShouldFail(String cronExpression, String errorMessage) {
    final InvalidExpressionException exception = assertThrows(
        InvalidExpressionException.class, () -> CronParserUtil.parse(cronExpression));
    assertEquals(errorMessage, exception.getMessage());
  }
}