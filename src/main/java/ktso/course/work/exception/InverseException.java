package ktso.course.work.exception;

import java.math.BigInteger;

/**
 * Выбрасывается при вознекновении исключения {@link ArithmeticException}, выброшенного в процессе вызова метода {@link BigInteger#modInverse(BigInteger)}.<br/>
 * Связано это с невозможностью найти обратный элемент из-за того, что НОД(targetNumber, mod) != 1.<br/>
 * Хранит в себе информацию о числе, для которого мы хотим найти обратный элемент, и моудль, по которому мы хотим найти обратный элемент.
 */
public class InverseException extends RuntimeException {

  private final BigInteger targetNumber;
  private final BigInteger mod;

  /**
   * Конструктор
   * @param targetNumber - число, для которого мы хотим найти обратный элемент;
   * @param mod - моудль, по которому мы хотим найти обратный элемент.
   */
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
