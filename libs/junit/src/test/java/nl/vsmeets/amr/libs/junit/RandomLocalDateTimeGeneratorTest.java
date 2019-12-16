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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link RandomLocalDateTimeGenerator}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class RandomLocalDateTimeGeneratorTest {

  static Stream<LocalDateTime> validLocalDateTimeValues() {
    final ZoneOffset offset = ZoneOffset.UTC;
    return Stream.of(LocalDateTime.MIN, LocalDateTime.MIN.plusSeconds(1L), LocalDateTime.ofEpochSecond(-1L, 0, offset),
        LocalDateTime.ofEpochSecond(0L, 0, offset), LocalDateTime.ofEpochSecond(0L, 1, offset),
        LocalDateTime.ofEpochSecond(0L, 999_999_999 - 1, offset), LocalDateTime.ofEpochSecond(0L, 999_999_999, offset),
        LocalDateTime.ofEpochSecond(1L, 0, offset), LocalDateTime.MAX.minusSeconds(1L), LocalDateTime.MAX);
  }

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
  @MethodSource(value = "validLocalDateTimeValues")
  void testRandomLocalDateTime(final LocalDateTime randomValue) {
    final ZoneOffset offset = ZoneOffset.UTC;
    final long randomEpochSecond = randomValue.toEpochSecond(offset);
    final int nanoOfSecond = randomValue.getNano();
    final LocalDateTime expectedValue = LocalDateTime.ofEpochSecond(randomEpochSecond, nanoOfSecond, offset);
    when(random.nextLong()).thenReturn(randomEpochSecond);
    when(random.nextInt()).thenReturn(nanoOfSecond);

    assertEquals(expectedValue, randomLocalDateTimeGenerator.randomLocalDateTime());
  }

  @ParameterizedTest
  @MethodSource(value = "validLocalDateTimeValues")
  void testRandomLocalDateTimeUnique(final LocalDateTime randomValue) {
    final ZoneOffset offset = ZoneOffset.UTC;
    final LocalDateTime notEqualTo1 = LocalDateTime.now();
    final LocalDateTime notEqualTo2 = notEqualTo1.plusSeconds(12345L);
    final LocalDateTime notEqualTo3 = notEqualTo2.plusSeconds(67890L);
    final long randomEpochSecond = randomValue.toEpochSecond(offset);
    final int nanoOfSecond = randomValue.getNano();
    final LocalDateTime expectedValue = LocalDateTime.ofEpochSecond(randomEpochSecond, nanoOfSecond, offset);
    when(random.nextLong()).thenReturn(notEqualTo1.toEpochSecond(offset), notEqualTo2.toEpochSecond(offset),
        notEqualTo3.toEpochSecond(offset), randomEpochSecond);
    when(random.nextInt()).thenReturn(notEqualTo1.getNano(), notEqualTo2.getNano(), notEqualTo3.getNano(),
        nanoOfSecond);

    assertEquals(expectedValue,
        randomLocalDateTimeGenerator.randomLocalDateTime(notEqualTo1, notEqualTo2, notEqualTo3));
  }

  @Test
  void testRandomLocalDateTimeZeroPrecisionRange() {
    final ZoneOffset offset = ZoneOffset.UTC;
    final LocalDateTime randomValue = LocalDateTime.now().withNano(0);
    final LocalDateTime startInclusive = randomValue.minusHours(8);
    final LocalDateTime endExclusive = randomValue.plusHours(8);
    final LocalDateTime expectedValue = randomValue;
    when(random.nextLong()).thenReturn(randomValue.toEpochSecond(offset));

    assertEquals(expectedValue,
        randomLocalDateTimeGenerator.randomLocalDateTimeZeroPrecisionRange(startInclusive, endExclusive));
  }

  @Test
  void testRandomLocalDateTimeZeroPrecisionRangeInvalid() {
    final LocalDateTime randomValue = LocalDateTime.now().withNano(0);
    final LocalDateTime startInclusive = randomValue;
    final LocalDateTime endExclusive = randomValue;

    assertThrows(IllegalArgumentException.class,
        () -> randomLocalDateTimeGenerator.randomLocalDateTimeZeroPrecisionRange(startInclusive, endExclusive));
  }

  @Test
  void testRandomLocalDateTimeZeroPrecisionRangeUnique() {
    final ZoneOffset offset = ZoneOffset.UTC;
    final LocalDateTime randomValue = LocalDateTime.now().withNano(0);
    final LocalDateTime startInclusive = randomValue.minusHours(8);
    final LocalDateTime endExclusive = randomValue.plusHours(8);
    final LocalDateTime notEqualTo1 = randomValue.plusHours(1);
    final LocalDateTime notEqualTo2 = randomValue.plusHours(2);
    final LocalDateTime expectedValue = randomValue;
    when(random.nextLong()).thenReturn(notEqualTo1.toEpochSecond(offset), notEqualTo2.toEpochSecond(offset),
        randomValue.toEpochSecond(offset));

    assertEquals(expectedValue, randomLocalDateTimeGenerator.randomLocalDateTimeZeroPrecisionRange(startInclusive,
        endExclusive, notEqualTo1, notEqualTo2));
  }

  @ParameterizedTest
  @MethodSource(value = "validLocalDateTimeValues")
  void testRandomLocalDateTimeZeroPrecisionSeconds(final LocalDateTime randomValue) {
    final ZoneOffset offset = ZoneOffset.UTC;
    final long randomEpochSecond = randomValue.toEpochSecond(offset);
    final LocalDateTime expectedValue = LocalDateTime.ofEpochSecond(randomEpochSecond, 0, offset);
    when(random.nextLong()).thenReturn(randomEpochSecond);

    assertEquals(expectedValue, randomLocalDateTimeGenerator.randomLocalDateTimeZeroPrecision());
  }

  @ParameterizedTest
  @MethodSource(value = "validLocalDateTimeValues")
  void testRandomLocalDateTimeZeroPrecisionUnique(final LocalDateTime randomValue) {
    final ZoneOffset offset = ZoneOffset.UTC;
    final LocalDateTime notEqualTo1 = LocalDateTime.now(offset).withNano(0);
    final LocalDateTime notEqualTo2 = notEqualTo1.plusSeconds(12345L);
    final LocalDateTime notEqualTo3 = notEqualTo2.plusSeconds(67890L);
    final long randomEpochSecond = randomValue.toEpochSecond(offset);
    final LocalDateTime expectedValue = LocalDateTime.ofEpochSecond(randomEpochSecond, 0, offset);
    when(random.nextLong()).thenReturn(notEqualTo1.toEpochSecond(offset), notEqualTo2.toEpochSecond(offset),
        notEqualTo3.toEpochSecond(offset), randomEpochSecond);

    assertEquals(expectedValue,
        randomLocalDateTimeGenerator.randomLocalDateTimeZeroPrecision(notEqualTo1, notEqualTo2, notEqualTo3));
  }

}
