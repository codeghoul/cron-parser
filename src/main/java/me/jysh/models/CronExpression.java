package me.jysh.models;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronExpression {

  private List<Integer> minute;

  private List<Integer> hour;

  private List<Integer> dayOfMonth;

  private List<Integer> month;

  private List<Integer> dayOfWeek;

  private String command;

  @Override
  public String toString() {
    return """
          
          minute        %s
          hour          %s
          day of month  %s
          month         %s
          day of week   %s
          command       %s
        """.formatted(formatIntegers(minute), formatIntegers(hour), formatIntegers(dayOfMonth),
        formatIntegers(month), formatIntegers(dayOfWeek), command);
  }

  private String formatIntegers(List<Integer> intList) {
    return intList.stream().map(String::valueOf).collect(Collectors.joining(" "));
  }
}
