package ktso.course.work;

import ktso.course.work.exception.FactorizationException;
import ktso.course.work.exception.InverseException;
import ktso.course.work.exception.PrimeNumberException;
import ktso.course.work.intf.Factorization;
import ktso.course.work.utils.PrimeUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

/**
 * Класс, реализующий механизм факторизации Ленстры с помощью эллиптических кривых
 */
public class LenstraFactorization implements Factorization<LenstraFactorizationContext> {

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  private static final BigInteger FIRST_PRIME_NUMBER = BigInteger.valueOf(2);

  public LenstraFactorization() {
  }

  /**
   * Факторизация числа.
   *
   * @param targetNumber                - число, которое будет разложено на множители.
   * @param lenstraFactorizationContext - контекст, содержащий информацию, необходимую для факторизации
   *                                    (лимит для поиска простых чисел и число итераций выполнения алгоритма)
   * @return объект класс {@link Pair}, содержащий множители, перемножение которых даст исходное число
   * @throws ktso.course.work.exception.PrimeNumberException   если targetNumber оказалось простым числом
   * @throws ktso.course.work.exception.FactorizationException если не удалось разложить число на множители за отведенное число шагов.
   */
  @Override
  public Pair<BigInteger, BigInteger> process(BigInteger targetNumber,
                                              LenstraFactorizationContext lenstraFactorizationContext) {

    if (targetNumber.compareTo(FIRST_PRIME_NUMBER) < 0) {
      throw new IllegalArgumentException("targetNumber argument must be greater that 1");
    }

    if (targetNumber.isProbablePrime(100)) {
      throw new PrimeNumberException();
    }

    int base = lenstraFactorizationContext.getBase();
    int iterationCount = lenstraFactorizationContext.getIterationCount();

    Pair<BigInteger, BigInteger> result;
    List<Integer> primeNumberList = PrimeUtils.getPrimeNumberList(base);

    for (int i = 0; i < iterationCount; i++) {
      result = startFactorization(targetNumber, base, primeNumberList);
      if (!(Objects.equals(result.getKey(), targetNumber) ||
            Objects.equals(result.getKey(), BigInteger.ONE))) {
        return result;
      }
    }
    throw new FactorizationException();
  }

  /**
   * Основная часть алгоритма факториазции
   *
   * @param targetNumber    - число, которое будет разложено на множители.
   * @param base            - лимит для поиска простых чисел
   * @param primeNumberList - список простых чисел от нуля до base
   * @return - множители targetNumber
   */
  private Pair<BigInteger, BigInteger> startFactorization(BigInteger targetNumber,
                                                          int base,
                                                          List<Integer> primeNumberList) {
    BigInteger a, b, x, y, discriminant, g;
    do {
      a = getRandomNumberWithMod(targetNumber.bitLength(), targetNumber);
      x = getRandomNumberWithMod(targetNumber.bitLength(), targetNumber);
      y = getRandomNumberWithMod(targetNumber.bitLength(), targetNumber);
      b = y.pow(2)
           .subtract(x.pow(3))
           .subtract(a.multiply(x))
           .mod(targetNumber);

      discriminant = a.pow(3)
                      .multiply(BigInteger.valueOf(4))
                      .add(b.pow(2).multiply(BigInteger.valueOf(27)))
                      .mod(targetNumber);

      g = discriminant.gcd(targetNumber);
    } while (Objects.equals(g, targetNumber));

    if (!Objects.equals(g, BigInteger.ONE)) {
      return Pair.of(g, targetNumber.divide(g));
    }

    EllipticCurve ellipticCurve = new EllipticCurve(a, b, targetNumber);
    EllipticCurvePoint ellipticCurvePoint = new EllipticCurvePoint(x, y);

    for (int primeNumber : primeNumberList) {
      try {
        for (int j = primeNumber; j < base; j *= primeNumber) {
          ellipticCurvePoint = ellipticCurve.multiply(ellipticCurvePoint, j);
        }
      } catch (InverseException ex) {
        BigInteger gcd = ex.getTargetNumber().gcd(ex.getMod());
        return Pair.of(gcd, targetNumber.divide(gcd));
      }
    }
    return Pair.of(targetNumber, BigInteger.ONE);
  }

  /**
   * Генерация случайного большого числа по модулю
   *
   * @param bitLength - длина в битах сгенерированного числа
   * @param mod       - модуль, по которому нужно взять сгенерированное число
   * @return сгенерированное случайное число
   */
  private BigInteger getRandomNumberWithMod(int bitLength, BigInteger mod) {
    return new BigInteger(bitLength, SECURE_RANDOM).abs().mod(mod);
  }
}
