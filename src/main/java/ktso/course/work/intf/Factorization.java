package ktso.course.work.intf;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;

public interface Factorization<FactorizationContext> {

  Pair<BigInteger, BigInteger> process(BigInteger targetNumber, FactorizationContext factorizationContext);
}
