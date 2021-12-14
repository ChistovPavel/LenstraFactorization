package ktso.course.work;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import ktso.course.work.exception.FactorizationException;
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
import java.util.OptionalDouble;

public class LenstraFactorizationTest {

  private static final String FACTORIZATION_REPORT_OUTPUT_FILE = "src/test/resources/factorizationReport.json";

  private static final int DEFAULT_POSITIVE_TEST_ITERATION_COUNT = 100_000;

  private static final List<Triple<Integer, Integer, Integer>> META_DATA = new ArrayList<>();
  private static final Triple<Integer, Integer, Integer> META_DATA_8_BIT = Triple.of(8, 10, 100);
  private static final Triple<Integer, Integer, Integer> META_DATA_16_BIT = Triple.of(16, 10, 100);
  private static final Triple<Integer, Integer, Integer> META_DATA_24_BIT = Triple.of(24, 10, 100);
  private static final Triple<Integer, Integer, Integer> META_DATA_32_BIT = Triple.of(32, 10, 100);
  private static final Triple<Integer, Integer, Integer> META_DATA_64_BIT = Triple.of(64, 10, 100);
  private static final Triple<Integer, Integer, Integer> META_DATA_128_BIT = Triple.of(128, 10, 100);

  static {
    META_DATA.add(META_DATA_8_BIT);
    META_DATA.add(META_DATA_16_BIT);
    META_DATA.add(META_DATA_24_BIT);
    META_DATA.add(META_DATA_32_BIT);
    META_DATA.add(META_DATA_64_BIT);
    META_DATA.add(META_DATA_128_BIT);
  }


  private static final SecureRandom DEFAULT_SECURE_RANDOM = new SecureRandom();
  private static final LenstraFactorization LENSTRA_FACTORIZATION = new LenstraFactorization();

  @Test
  public void generateFactorizationReport() throws IOException {
    final List<FactorizationInfo> factorizationInfoList = new ArrayList<>();

    for (Triple<Integer, Integer, Integer> metaData : META_DATA) {
      test(metaData, factorizationInfoList);
    }

    String factorizationReport = new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(factorizationInfoList);

    try (FileWriter fileWriter = new FileWriter(FACTORIZATION_REPORT_OUTPUT_FILE)) {
      IOUtils.write(factorizationReport, fileWriter);
    }

    for (Triple<Integer, Integer, Integer> metaData : META_DATA) {
      int numBits = metaData.getLeft();
      OptionalDouble averageExecutionTime = getAverageExecutionTime(factorizationInfoList, numBits);
      System.out.println(String.format("%s bits. Average nanos time: %s", numBits, averageExecutionTime));
    }
  }

  private void test(Triple<Integer, Integer, Integer> testMetaData,
                    List<FactorizationInfo> factorizationInfoList) {

    StopWatch stopWatch = new StopWatch();

    final int numBits = testMetaData.getLeft();
    final int base = testMetaData.getMiddle();
    final int iterationCount = testMetaData.getRight();

    for (int i = 0; i < DEFAULT_POSITIVE_TEST_ITERATION_COUNT; i++) {

      final FactorizationInfo factorizationInfo = new FactorizationInfo();

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

        factorizationInfo.setExecutionTime(stopWatch.getNanoTime());
        factorizationInfo.setP(factorizationResult.getLeft());
        factorizationInfo.setQ(factorizationResult.getRight());
      } catch (FactorizationException | PrimeNumberException | IllegalArgumentException ex) {
        i--;
        continue;
      }

      factorizationInfoList.add(factorizationInfo);
      System.out.println(numBits + "bits. Test complete #" + i + " of " + DEFAULT_POSITIVE_TEST_ITERATION_COUNT);
    }
  }

  private OptionalDouble getAverageExecutionTime(List<FactorizationInfo> factorizationInfoList, int numBits) {
    return factorizationInfoList.parallelStream()
                                .filter(factorizationInfo -> factorizationInfo.getNumBits() == numBits)
                                .mapToLong(FactorizationInfo::getExecutionTime)
                                .average();
  }
}
