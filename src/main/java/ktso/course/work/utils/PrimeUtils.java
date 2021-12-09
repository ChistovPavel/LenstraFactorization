package ktso.course.work.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrimeUtils {

  private static final int FIRST_PRIME_NUMBER = 2;

  private PrimeUtils() {
    throw new UnsupportedOperationException();
  }

  public static List<Integer> getPrimeNumberList(int upperLimit) {
    IntStream resultIntStream = IntStream.range(FIRST_PRIME_NUMBER, upperLimit);
    for (int i = FIRST_PRIME_NUMBER; i < upperLimit; i++) {
      resultIntStream = filterIntStream(resultIntStream, i);
    }
    return resultIntStream.boxed().collect(Collectors.toList());
  }

  private static IntStream filterIntStream(IntStream intStream, final int value) {
    return intStream.filter(i -> i == value || i % value != 0);
  }
}
