package ktso.course.work;

import ktso.course.work.exception.FactorizationException;
import ktso.course.work.exception.InverseException;
import ktso.course.work.intf.Factorization;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * Класс, реализующий механизм факторизации чисел
 */
public class LenstraFactorization implements Factorization {

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  private static final int DEFAULT_ITERATION_COUNT = 100;

  public LenstraFactorization() {
  }

  /**
   * @param targetNumber
   * @param factorialBase
   * @return
   */
  @Override
  public Pair<BigInteger, BigInteger> process(BigInteger targetNumber, int factorialBase) {
    return process(targetNumber, factorialBase, DEFAULT_ITERATION_COUNT);
  }

  /**
   * @param targetNumber
   * @param factorialBase
   * @param iterationCount
   * @return
   */
  @Override
  public Pair<BigInteger, BigInteger> process(BigInteger targetNumber, int factorialBase, int iterationCount) {
    Pair<BigInteger, BigInteger> result;

    if (BigInteger.ONE.equals(targetNumber)) {
      return Pair.of(targetNumber, BigInteger.ONE);
    }

    for (int i = 0; i < iterationCount; i++) {
      result = startFactorization(targetNumber, factorialBase);
      if (!(Objects.equals(result.getKey(), targetNumber) ||
            Objects.equals(result.getKey(), BigInteger.ONE))) {
        return result;
      }
    }
    return Pair.of(targetNumber, BigInteger.ONE);
  }

  private Pair<BigInteger, BigInteger> startFactorization(BigInteger targetNumber,
                                                          int factorialBase) {
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

    for (int factorialStep = 2; factorialStep < factorialBase; factorialStep++) {
      try {
        ellipticCurvePoint = ellipticCurve.multiply(ellipticCurvePoint, factorialStep);
      } catch (InverseException ex) {
        BigInteger gcd = ex.getTargetNumber().gcd(ex.getMod());
        return Pair.of(gcd, targetNumber.divide(gcd));
      }
    }
    return Pair.of(targetNumber, BigInteger.ONE);
  }

  private BigInteger getRandomNumberWithMod(int bitLength, BigInteger mod) {
    return new BigInteger(bitLength, SECURE_RANDOM).mod(mod);
  }
}
