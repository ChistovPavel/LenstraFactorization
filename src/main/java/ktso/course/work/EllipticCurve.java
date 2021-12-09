package ktso.course.work;

import ktso.course.work.exception.InverseException;

import java.math.BigInteger;
import java.util.Objects;
import java.util.function.BiFunction;

public class EllipticCurve {

  private final BigInteger a;
  private final BigInteger b;
  private final BigInteger p;

  public EllipticCurve(BigInteger a, BigInteger b, BigInteger p) {
    this.a = a;
    this.b = b;
    this.p = p;
  }

  public BigInteger getA() {
    return a;
  }

  public BigInteger getB() {
    return b;
  }

  public BigInteger getP() {
    return p;
  }

  public EllipticCurvePoint add(EllipticCurvePoint p1, EllipticCurvePoint p2) {
    if (EllipticCurvePoint.ZERO.equals(p1)) {
      return p2;
    }
    if (EllipticCurvePoint.ZERO.equals(p2)) {
      return p1;
    }
    BigInteger lambda = getLambda(p1,
                                  p2,
                                  (targetNumber, mod) -> {
                                    try {
                                      return targetNumber.modInverse(mod);
                                    } catch (ArithmeticException ex) {
                                      throw new InverseException(targetNumber, mod);
                                    }
                                  });
    return getNewPoint(p1, p2, lambda);
  }

  public EllipticCurvePoint multiply(EllipticCurvePoint ellipticCurvePoint, long k) {

    EllipticCurvePoint result = ellipticCurvePoint;
    EllipticCurvePoint lastAdditionMember = (k & 1) == 1 ?
        ellipticCurvePoint :
        EllipticCurvePoint.ZERO;

    while (k > 0) {
      k >>= 1;
      result = add(result, result);
      if ((k & 1) == 1) {
        result = add(result, ellipticCurvePoint);
      }
    }
    return add(result, lastAdditionMember);
  }

  private BigInteger getLambda(EllipticCurvePoint p1, EllipticCurvePoint p2, BiFunction<BigInteger, BigInteger, BigInteger> inverseFunction) {
    return (Objects.equals(p1, p2) ?
        p1.getX()
          .pow(2)
          .multiply(BigInteger.valueOf(3))
          .add(a)
          .multiply(inverseFunction.apply(p2.getY()
                                            .multiply(BigInteger.valueOf(2)),
                                          p)) :
        p2.getY()
          .subtract(p1.getY())
          .multiply(inverseFunction.apply(p2.getX()
                                            .subtract(p1.getX()),
                                          p)))
        .mod(p);
  }

  private EllipticCurvePoint getNewPoint(EllipticCurvePoint p1, EllipticCurvePoint p2, BigInteger lambda) {
    BigInteger x = lambda.pow(2)
                         .subtract(p1.getX())
                         .subtract(p2.getY())
                         .mod(p);

    BigInteger y = lambda.multiply(p1.getX()
                                     .subtract(x))
                         .subtract(p1.getY())
                         .mod(p);

    return new EllipticCurvePoint(x, y);
  }
}
