package ktso.course.work.exception;

import java.math.BigInteger;

public class InverseException extends RuntimeException {

  private final BigInteger targetNumber;
  private final BigInteger mod;

  public InverseException(BigInteger targetNumber, BigInteger mod) {
    this.targetNumber = targetNumber;
    this.mod = mod;
  }

  public BigInteger getTargetNumber() {
    return targetNumber;
  }

  public BigInteger getMod() {
    return mod;
  }
}
