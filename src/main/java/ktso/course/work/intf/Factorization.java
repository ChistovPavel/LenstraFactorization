package ktso.course.work.intf;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;

/**
 * Описание интерфейса с основными методами для реализации факторизации
 */
public interface Factorization<FactorizationContext> {

  /**
   * Факторизация числа.
   * @param targetNumber - число, которое будет разложено на множители.
   * @param factorizationContext - контекст, содержащий информацию, необходимую для факторизации
   * @throws ktso.course.work.exception.PrimeNumberException если targetNumber оказалось простым числом
   * @throws ktso.course.work.exception.FactorizationException если не удалось разложить число на множители за отведенное число шагов.
   * @return объект класс {@link Pair}, содержащий множители, перемножение которых даст исходное число
   */
  Pair<BigInteger, BigInteger> process(BigInteger targetNumber, FactorizationContext factorizationContext);
}
