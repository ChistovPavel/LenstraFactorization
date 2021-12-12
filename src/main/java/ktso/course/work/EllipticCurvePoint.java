package ktso.course.work;

import java.math.BigInteger;

/**
 * Точка эллиптической кривой
 */
public class EllipticCurvePoint {

  public static final EllipticCurvePoint ZERO = new EllipticCurvePoint(BigInteger.ZERO, BigInteger.ZERO);

  private final BigInteger x;
  private final BigInteger y;

  /**
   * Конструктор
   * @param x - координата по оси x;
   * @param y - координата по оси y.
   */
  public EllipticCurvePoint(BigInteger x, BigInteger y) {
    this.x = x;
    this.y = y;
  }

  public BigInteger getX() {
    return x;
  }

  public BigInteger getY() {
    return y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EllipticCurvePoint that = (EllipticCurvePoint) o;
    return getX().equals(that.getX()) && getY().equals(that.getY());
  }
}