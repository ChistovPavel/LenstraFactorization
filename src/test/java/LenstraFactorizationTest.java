import ktso.course.work.LenstraFactorization;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.stream.IntStream;

public class LenstraFactorizationTest {

  private static final String DEFAULT_TEST_SUCCESS_MESSAGE = "complete test number %d";
  private static final String START_FACTORIZATION_MESSAGE = "Factorization start. Target number: %s, base: %s";

  private static final int DEFAULT_POSITIVE_TEST_ITERATION_COUNT = 1_000;
  private static final int DEFAULT_BASE_VALUE = 1_0;

  private static final SecureRandom DEFAULT_SECURE_RANDOM = new SecureRandom();
  private static final LenstraFactorization LENSTRA_FACTORIZATION = new LenstraFactorization();

  @Test
  @Ignore
  public void positiveTest() {
    IntStream.range(1, DEFAULT_POSITIVE_TEST_ITERATION_COUNT).forEach(i -> {
      test();
      System.out.println(String.format(DEFAULT_TEST_SUCCESS_MESSAGE, i));
    });
  }

  private void test() {
    final BigInteger bigInteger = new BigInteger(String.valueOf(Math.abs(DEFAULT_SECURE_RANDOM.nextLong())));

    System.out.println(String.format(START_FACTORIZATION_MESSAGE, bigInteger.toString(10), DEFAULT_BASE_VALUE));

    Pair<BigInteger, BigInteger> factorizationResult = LENSTRA_FACTORIZATION.process(bigInteger, DEFAULT_BASE_VALUE);
    Assert.assertEquals(bigInteger, factorizationResult.getLeft().multiply(factorizationResult.getRight()));
  }
}
