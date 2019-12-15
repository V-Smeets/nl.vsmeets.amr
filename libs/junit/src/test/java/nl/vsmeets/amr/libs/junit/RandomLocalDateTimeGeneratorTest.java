/**
 * Copyright (C) 2019 Vincent Smeets
 * <p>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
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

import nl.vsmeets.amr.libs.junit.impl.LocalDateTimeGeneratorConstants;

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
  @ValueSource(ints = { 0, 1, 999_999_999 - 1, 999_999_999 })
  void testRandomLocalDateTimeNanos(final int randomValue) {
    final long randomEpochSecond = 2;
    final int nanoOfSecond = randomValue;
    final int offsetSeconds = 3600;
    final ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetSeconds);
    final LocalDateTime expectedValue = LocalDateTime.ofEpochSecond(randomEpochSecond, nanoOfSecond, offset);
    when(random.nextLong()).thenReturn(randomEpochSecond);
    when(random.nextInt()).thenReturn(nanoOfSecond, offsetSeconds);

    assertEquals(expectedValue, randomLocalDateTimeGenerator.randomLocalDateTime());
  }

  @ParameterizedTest
  @ValueSource(longs = { LocalDateTimeGeneratorConstants.MIN_SECONDS, LocalDateTimeGeneratorConstants.MIN_SECONDS + 1,
      -1L, 0L, 1L, LocalDateTimeGeneratorConstants.MAX_SECONDS - 1, LocalDateTimeGeneratorConstants.MAX_SECONDS })
  void testRandomLocalDateTimeSeconds(final long randomValue) {
    final long randomEpochSecond = randomValue;
    final int nanoOfSecond = 0;
    final int offsetSeconds = 0;
    final ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetSeconds);
    final LocalDateTime expectedValue = LocalDateTime.ofEpochSecond(randomEpochSecond, nanoOfSecond, offset);
    when(random.nextLong()).thenReturn(randomEpochSecond);
    when(random.nextInt()).thenReturn(nanoOfSecond, offsetSeconds);

    assertEquals(expectedValue, randomLocalDateTimeGenerator.randomLocalDateTime());
  }

  @ParameterizedTest
  @ValueSource(longs = { LocalDateTimeGeneratorConstants.MIN_SECONDS, LocalDateTimeGeneratorConstants.MIN_SECONDS + 1,
      -1L, 0L, 1L, LocalDateTimeGeneratorConstants.MAX_SECONDS - 1, LocalDateTimeGeneratorConstants.MAX_SECONDS })
  void testRandomLocalDateTimeUnique(final long randomValue) {
    final LocalDateTime notEqualTo1 = LocalDateTime.now();
    final LocalDateTime notEqualTo2 = notEqualTo1.plusSeconds(12345L);
    final LocalDateTime notEqualTo3 = notEqualTo2.plusSeconds(67890L);
    final long randomEpochSecond = randomValue;
    final int nanoOfSecond = 2;
    final int offsetSeconds = 3600;
    final ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetSeconds);
    final LocalDateTime expectedValue = LocalDateTime.ofEpochSecond(randomEpochSecond, nanoOfSecond, offset);
    when(random.nextLong()).thenReturn(notEqualTo1.toEpochSecond(ZoneOffset.UTC),
        notEqualTo2.toEpochSecond(ZoneOffset.UTC), notEqualTo3.toEpochSecond(ZoneOffset.UTC), randomEpochSecond);
    when(random.nextInt()).thenReturn(notEqualTo1.toLocalTime().getNano(), 0, notEqualTo2.toLocalTime().getNano(), 0,
        notEqualTo3.toLocalTime().getNano(), 0, nanoOfSecond, offsetSeconds);

    assertEquals(expectedValue,
        randomLocalDateTimeGenerator.randomLocalDateTime(notEqualTo1, notEqualTo2, notEqualTo3));
  }

  @ParameterizedTest
  @ValueSource(longs = { LocalDateTimeGeneratorConstants.MIN_SECONDS, LocalDateTimeGeneratorConstants.MIN_SECONDS + 1,
      -1L, 0L, 1L, LocalDateTimeGeneratorConstants.MAX_SECONDS - 1, LocalDateTimeGeneratorConstants.MAX_SECONDS })
  void testRandomLocalDateTimeZeroPrecisionSeconds(final long randomValue) {
    final long randomEpochSecond = randomValue;
    final int offsetSeconds = 3600;
    final ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetSeconds);
    final LocalDateTime expectedValue = LocalDateTime.ofEpochSecond(randomEpochSecond, 0, offset);
    when(random.nextLong()).thenReturn(randomEpochSecond);
    when(random.nextInt()).thenReturn(offsetSeconds);

    assertEquals(expectedValue, randomLocalDateTimeGenerator.randomLocalDateTimeZeroPrecision());
  }

  @ParameterizedTest
  @ValueSource(longs = { LocalDateTimeGeneratorConstants.MIN_SECONDS, LocalDateTimeGeneratorConstants.MIN_SECONDS + 1,
      -1L, 0L, 1L, LocalDateTimeGeneratorConstants.MAX_SECONDS - 1, LocalDateTimeGeneratorConstants.MAX_SECONDS })
  void testRandomLocalDateTimeZeroPrecisionUnique(final long randomValue) {
    final ZoneOffset zoneOffset = ZoneOffset.UTC;
    final LocalDateTime notEqualTo1 = LocalDateTime.now(zoneOffset).withNano(0);
    final LocalDateTime notEqualTo2 = notEqualTo1.plusSeconds(12345L);
    final LocalDateTime notEqualTo3 = notEqualTo2.plusSeconds(67890L);
    final long randomEpochSecond = randomValue;
    final int offsetSeconds = 3600;
    final ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetSeconds);
    final LocalDateTime expectedValue = LocalDateTime.ofEpochSecond(randomEpochSecond, 0, offset);
    when(random.nextLong()).thenReturn(notEqualTo1.toEpochSecond(zoneOffset), notEqualTo2.toEpochSecond(zoneOffset),
        notEqualTo3.toEpochSecond(zoneOffset), randomEpochSecond);
    when(random.nextInt()).thenReturn(zoneOffset.getTotalSeconds(), zoneOffset.getTotalSeconds(),
        zoneOffset.getTotalSeconds(), offsetSeconds);

    assertEquals(expectedValue,
        randomLocalDateTimeGenerator.randomLocalDateTimeZeroPrecision(notEqualTo1, notEqualTo2, notEqualTo3));
  }

}
