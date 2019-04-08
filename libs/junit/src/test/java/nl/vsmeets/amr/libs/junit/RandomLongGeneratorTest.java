package nl.vsmeets.amr.libs.junit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link RandomLongGenerator}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class RandomLongGeneratorTest {

  @Mock
  private Random random;

  private RandomLongGenerator randomLongGenerator;

  @BeforeEach
  void setup() {
    randomLongGenerator = new RandomLongGenerator() {

      @Override
      public Random getRandom() {
        return random;
      }
    };
  }

  @ParameterizedTest
  @ValueSource(longs = { Long.MIN_VALUE, Long.MIN_VALUE + 1, -1, 0, 1, Long.MAX_VALUE - 1, Long.MAX_VALUE })
  void testRandomLong(final long randomValue) {
    when(random.nextLong()).thenReturn(randomValue);
    final long expectedValue = randomValue;

    assertEquals(expectedValue, randomLongGenerator.randomLong());
  }

  @Test
  void testRandomLongInvalidRange() {
    final long startInclusive = 123;
    final long endExclusive = startInclusive;

    assertThrows(IllegalArgumentException.class, () -> randomLongGenerator.randomLong(startInclusive, endExclusive));
  }

  @Test
  void testRandomLongLargeRange() {
    final long randomValue = 123;
    when(random.nextLong()).thenReturn(Long.MIN_VALUE, Long.MAX_VALUE, randomValue);
    final long startInclusive = Long.MIN_VALUE + 10;
    final long endExclusive = Long.MAX_VALUE - 10;
    final long expectedValue = randomValue;

    assertEquals(expectedValue, randomLongGenerator.randomLong(startInclusive, endExclusive));
  }

  @ParameterizedTest
  @ValueSource(longs = { -15, -7, 1, 9, 17, 25 })
  void testRandomLongSmallRange(final long randomValue) {
    when(random.nextLong()).thenReturn(randomValue);
    final long startInclusive = 8;
    final long endExclusive = startInclusive + 8;
    final long expectedValue = 9;

    assertEquals(expectedValue, randomLongGenerator.randomLong(startInclusive, endExclusive));
  }

}
