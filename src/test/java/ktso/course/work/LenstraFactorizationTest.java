package ktso.course.work;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import ktso.course.work.exception.PrimeNumberException;
import ktso.course.work.model.FactorizationInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class LenstraFactorizationTest {

  private static final String FACTORIZATION_REPORT_OUTPUT_FILE = "src/test/resources/factorizationReport.json";

  private static final int DEFAULT_POSITIVE_TEST_ITERATION_COUNT = 10;

  private static final Triple<Integer, Integer, Integer> EIGHT_BIT_NUMBER_META_DATA = Triple.of(8, 10, 10);
  private static final Triple<Integer, Integer, Integer> SIXTEEN_BIT_NUMBER_META_DATA = Triple.of(16, 20, 100);
  private static final Triple<Integer, Integer, Integer> TWENTY_FOUR_NUMBER_META_DATA = Triple.of(24, 30, 1000);

  private static final SecureRandom DEFAULT_SECURE_RANDOM = new SecureRandom();
  private static final LenstraFactorization LENSTRA_FACTORIZATION = new LenstraFactorization();

  @Test
  public void generateFactorizationReport() throws IOException {
    final List<FactorizationInfo> factorizationInfoList = new ArrayList<>();

    test(EIGHT_BIT_NUMBER_META_DATA, factorizationInfoList);
    test(SIXTEEN_BIT_NUMBER_META_DATA, factorizationInfoList);
    test(TWENTY_FOUR_NUMBER_META_DATA, factorizationInfoList);

    String factorizationReport = new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(factorizationInfoList);

    try (FileWriter fileWriter = new FileWriter(FACTORIZATION_REPORT_OUTPUT_FILE)) {
      IOUtils.write(factorizationReport, fileWriter);
    }
  }

  private void test(Triple<Integer, Integer, Integer> testMetaData,
                    List<FactorizationInfo> factorizationInfoList) {

    StopWatch stopWatch = new StopWatch();

    IntStream.range(0, DEFAULT_POSITIVE_TEST_ITERATION_COUNT).forEach(i -> {

      final FactorizationInfo factorizationInfo = new FactorizationInfo();

      final int numBits = testMetaData.getLeft();
      final int base = testMetaData.getMiddle();
      final int iterationCount = testMetaData.getRight();

      final BigInteger bigInteger = new BigInteger(numBits, DEFAULT_SECURE_RANDOM).abs();

      factorizationInfo.setNumber(bigInteger);
      factorizationInfo.setNumBits(numBits);
      factorizationInfo.setBase(base);

      try {
        stopWatch.reset();
        stopWatch.start();
        Pair<BigInteger, BigInteger> factorizationResult = LENSTRA_FACTORIZATION.process(bigInteger,
                                                                                         new LenstraFactorizationContext(base, iterationCount));
        stopWatch.stop();

        Assert.assertEquals(bigInteger, factorizationResult.getLeft().multiply(factorizationResult.getRight()));

        factorizationInfo.setP(factorizationResult.getLeft());
        factorizationInfo.setQ(factorizationResult.getRight());
      } catch (PrimeNumberException ex) {
        factorizationInfo.setPrime(true);
      }

      factorizationInfoList.add(factorizationInfo);
    });
  }
}
