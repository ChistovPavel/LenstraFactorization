package ktso.course.work.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Класс релазиует вспомогательные функции для работы с простыми числами
 */
public class PrimeUtils {

  private static final int FIRST_PRIME_NUMBER = 2;

  private PrimeUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * Поиск всех простых чисел на интервале от нуля до upperLimit
   * @param upperLimit - верхний предел поиска простых чисел
   * @return список простых чисел
   */
  public static List<Integer> getPrimeNumberList(int upperLimit) {
    IntStream resultIntStream = IntStream.range(FIRST_PRIME_NUMBER, upperLimit);
    for (int i = FIRST_PRIME_NUMBER; i < upperLimit; i++) {
      resultIntStream = filterIntStream(resultIntStream, i);
    }
    return resultIntStream.boxed().collect(Collectors.toList());
  }

  /**
   * фильтрация потока данных по критерию делимости без остатка
   * @param intStream - поток данных для фильтрации
   * @param value - делитель
   * @return отфильтрованный поток
   */
  private static IntStream filterIntStream(IntStream intStream, final int value) {
    return intStream.filter(i -> i == value || i % value != 0);
  }
}
