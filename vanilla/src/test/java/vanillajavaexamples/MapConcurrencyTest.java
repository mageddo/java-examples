package vanillajavaexamples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapConcurrencyTest {

  @Test
  void mustBeConcurrencyFree() throws Exception {

    // act
    final var sum = MapConcurrency.concurrentSumNumbers();

    // assert
    assertEquals(200, sum);

  }

  @Test
  void mustSufferWithConcurrencyIssues() throws Exception {

    // act
    final var sum = MapConcurrency.nonConcurrentSumNumbers();

    // assert
    assertNotEquals(200, sum);

  }

}
