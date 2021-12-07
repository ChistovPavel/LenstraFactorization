package ktso.course.work.intf;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;

public interface Factorization {

  Pair<BigInteger, BigInteger> process(BigInteger targetNumber, int base);

  Pair<BigInteger, BigInteger> process(BigInteger targetNumber, int base, int iterationCount);
}
