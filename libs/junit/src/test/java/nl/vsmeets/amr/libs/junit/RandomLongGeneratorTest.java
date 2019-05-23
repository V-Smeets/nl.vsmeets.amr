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
    final long expectedValue = randomValue;
    when(random.nextLong()).thenReturn(randomValue);

    assertEquals(expectedValue, randomLongGenerator.randomLong());
  }

  @Test
  void testRandomLongRangeInRange() {
    final long randomValue = 123;
    final long startInclusive = Long.MIN_VALUE + 10;
    final long endExclusive = Long.MAX_VALUE - 10;
    final long expectedValue = randomValue;
    when(random.nextLong()).thenReturn(randomValue);

    assertEquals(expectedValue, randomLongGenerator.randomLongRange(startInclusive, endExclusive));
  }

  @Test
  void testRandomLongRangeInvalid() {
    final long startInclusive = 123;
    final long endExclusive = startInclusive;

    assertThrows(IllegalArgumentException.class,
        () -> randomLongGenerator.randomLongRange(startInclusive, endExclusive));
  }

  @Test
  void testRandomLongRangeLargeOutOfRange() {
    final long randomValue = 123;
    final long startInclusive = Long.MIN_VALUE + 10;
    final long endExclusive = Long.MAX_VALUE - 10;
    final long expectedValue = randomValue;
    when(random.nextLong()).thenReturn(startInclusive - 1, endExclusive + 1, randomValue);

    assertEquals(expectedValue, randomLongGenerator.randomLongRange(startInclusive, endExclusive));
  }

  @ParameterizedTest
  @ValueSource(longs = { -7, 1, 17 })
  void testRandomLongRangeSmallOutOfRange(final long randomValue) {
    final long startInclusive = 8;
    final long endExclusive = startInclusive + 8;
    final long expectedValue = 9;
    when(random.nextLong()).thenReturn(randomValue);

    assertEquals(expectedValue, randomLongGenerator.randomLongRange(startInclusive, endExclusive));
  }

  @ParameterizedTest
  @ValueSource(longs = { -7, 1, 17 })
  void testRandomLongRangeSmallOutOfRangeUnique(final long randomValue) {
    final long startInclusive = 8;
    final long endExclusive = startInclusive + 8;
    final long notEqualTo1 = endExclusive - 1;
    final long notEqualTo2 = endExclusive - 2;
    final long expectedValue = 9;
    when(random.nextLong()).thenReturn(notEqualTo1 - (endExclusive - startInclusive),
        notEqualTo2 + endExclusive - startInclusive, randomValue);

    assertEquals(expectedValue,
        randomLongGenerator.randomLongRange(startInclusive, endExclusive, notEqualTo1, notEqualTo2));
  }

  @ParameterizedTest
  @ValueSource(longs = { Long.MIN_VALUE, Long.MIN_VALUE + 1, -1, 0, 1, Long.MAX_VALUE - 1, Long.MAX_VALUE })
  void testRandomLongUnique(final long randomValue) {
    final long notEqualTo1 = -1234;
    final long notEqualTo2 = 1234;
    when(random.nextLong()).thenReturn(notEqualTo1, notEqualTo2, randomValue);
    final long expectedValue = randomValue;

    assertEquals(expectedValue, randomLongGenerator.randomLong(notEqualTo1, notEqualTo2));
  }

}
