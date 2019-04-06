/**
 *
 */
package nl.vsmeets.amr.libs.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link RandomLocalDateTimeGenerator}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class RandomLocalDateTimeGeneratorTest {

  @Mock
  private Random random;

  private RandomLocalDateTimeGenerator randomLocalDateTimeGenerator;

  @BeforeEach
  void setup() {
    randomLocalDateTimeGenerator = new RandomLocalDateTimeGenerator() {

      @Override
      public Random getRandom() {
        return random;
      }
    };
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 999_999_999 })
  void testRandomLocalDateTimeNanos(final int nanos) {
    final long seconds = 0L;
    when(random.nextLong()).thenReturn(seconds);
    when(random.nextInt()).thenReturn(nanos);
    final LocalDateTime expectedValue = LocalDateTime.ofEpochSecond(seconds - 999_999_999L, nanos - 0, ZoneOffset.UTC);

    assertEquals(expectedValue, randomLocalDateTimeGenerator.randomLocalDateTime());
  }

  @ParameterizedTest
  @ValueSource(longs = { 0L, 1L, 999_999_999L })
  void testRandomLocalDateTimeSeconds(final long seconds) {
    final int nanos = 0;
    when(random.nextLong()).thenReturn(seconds);
    when(random.nextInt()).thenReturn(nanos);
    final LocalDateTime expectedValue = LocalDateTime.ofEpochSecond(seconds - 999_999_999L, nanos - 0, ZoneOffset.UTC);

    assertEquals(expectedValue, randomLocalDateTimeGenerator.randomLocalDateTime());
  }

}
