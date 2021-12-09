package ktso.course.work;

import ktso.course.work.exception.FactorizationException;
import ktso.course.work.exception.InverseException;
import ktso.course.work.intf.Factorization;
import ktso.course.work.utils.PrimeUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

public class LenstraFactorization implements Factorization {

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  private static final int DEFAULT_ITERATION_COUNT = 100_000;

  public LenstraFactorization() {
  }

  @Override
  public Pair<BigInteger, BigInteger> process(BigInteger targetNumber, int base) {
    return process(targetNumber, base, DEFAULT_ITERATION_COUNT);
  }

  @Override
  public Pair<BigInteger, BigInteger> process(BigInteger targetNumber, int base, int iterationCount) {
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
        ellipticCurvePoint = ellipticCurve.multiply(ellipticCurvePoint,
                                                    getPointMultiplicity(primeNumber, base));
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

  private int getPointMultiplicity(int primeNumber, int base) {
    int returnValue = primeNumber;
    while (returnValue < base) {
      returnValue *= primeNumber;
    }
    return returnValue / primeNumber;
  }
}
