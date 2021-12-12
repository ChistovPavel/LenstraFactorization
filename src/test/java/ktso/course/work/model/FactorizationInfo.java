package ktso.course.work.model;

import java.math.BigInteger;

public class FactorizationInfo {

  private BigInteger number;

  private int numBits;

  private int base;

  private boolean prime;

  private BigInteger q;

  private BigInteger p;

  public BigInteger getNumber() {
    return number;
  }

  public void setNumber(BigInteger number) {
    this.number = number;
  }

  public int getNumBits() {
    return numBits;
  }

  public void setNumBits(int numBits) {
    this.numBits = numBits;
  }

  public int getBase() {
    return base;
  }

  public void setBase(int base) {
    this.base = base;
  }

  public boolean isPrime() {
    return prime;
  }

  public void setPrime(boolean prime) {
    this.prime = prime;
  }

  public BigInteger getQ() {
    return q;
  }

  public void setQ(BigInteger q) {
    this.q = q;
  }

  public BigInteger getP() {
    return p;
  }

  public void setP(BigInteger p) {
    this.p = p;
  }
}
