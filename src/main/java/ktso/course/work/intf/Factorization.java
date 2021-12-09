package ktso.course.work.intf;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;

/**
 * Описание интерфейса с основными методами для реализации факторизации
 */
public interface Factorization {

  /**
   * Факторизация числа без указания числа итераций.
   * @param targetNumber - число, которое будет разложено на множители.
   * @param factorialBase
   * @throws ktso.course.work.exception.FactorizationException если не удалось разложить число на множители за отведенное число шагов.
   * @return объект класс {@link Pair}, содержащий множители, перемножение которых даст исходное число
   */
  Pair<BigInteger, BigInteger> process(BigInteger targetNumber, int factorialBase);

  /**
   * Факторизация числа.
   * @param targetNumber - число, которое будет разложено на множители.
   * @param factorialBase
   * @param iterationCount - число итераций, в течении которых число должно быть разложено на множители.
   * @throws ktso.course.work.exception.FactorizationException если не удалось разложить число на множители за отведенное число шагов.
   * @return объект класс {@link Pair}, содержащий множители, перемножение которых даст исходное число
   */
  Pair<BigInteger, BigInteger> process(BigInteger targetNumber, int factorialBase, int iterationCount);
}
