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
 * Unit tests for the class {@link RandomIntGenerator}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class RandomIntGeneratorTest {

  @Mock
  private Random random;

  private RandomIntGenerator randomIntGenerator;

  @BeforeEach
  void setup() {
    randomIntGenerator = new RandomIntGenerator() {

      @Override
      public Random getRandom() {
        return random;
      }
    };
  }

  @ParameterizedTest
  @ValueSource(ints = { Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -1, 0, 1, Integer.MAX_VALUE - 1, Integer.MAX_VALUE })
  void testRandomInt(final int randomValue) {
    final int expectedValue = randomValue;
    when(random.nextInt()).thenReturn(randomValue);

    assertEquals(expectedValue, randomIntGenerator.randomInt());
  }

  @Test
  void testRandomIntRangeInRange() {
    final int randomValue = 123;
    final int startInclusive = Integer.MIN_VALUE + 10;
    final int endExclusive = Integer.MAX_VALUE - 10;
    final int expectedValue = randomValue;
    when(random.nextInt()).thenReturn(randomValue);

    assertEquals(expectedValue, randomIntGenerator.randomIntRange(startInclusive, endExclusive));
  }

  @Test
  void testRandomIntRangeInvalid() {
    final int startInclusive = 123;
    final int endExclusive = startInclusive;

    assertThrows(IllegalArgumentException.class, () -> randomIntGenerator.randomIntRange(startInclusive, endExclusive));
  }

  @Test
  void testRandomIntRangeLargeOutOfRange() {
    final int randomValue = 123;
    final int startInclusive = Integer.MIN_VALUE + 10;
    final int endExclusive = Integer.MAX_VALUE - 10;
    final int expectedValue = randomValue;
    when(random.nextInt()).thenReturn(startInclusive - 1, endExclusive + 1, randomValue);

    assertEquals(expectedValue, randomIntGenerator.randomIntRange(startInclusive, endExclusive));
  }

  @ParameterizedTest
  @ValueSource(ints = { -7, 1, 17 })
  void testRandomIntRangeSmallOutOfRange(final int randomValue) {
    final int startInclusive = 8;
    final int endExclusive = startInclusive + 8;
    final int expectedValue = 9;
    when(random.nextInt()).thenReturn(randomValue);

    assertEquals(expectedValue, randomIntGenerator.randomIntRange(startInclusive, endExclusive));
  }

  @ParameterizedTest
  @ValueSource(ints = { -7, 1, 17 })
  void testRandomIntRangeSmallOutOfRangeUnique(final int randomValue) {
    final int startInclusive = 8;
    final int endExclusive = startInclusive + 8;
    final int notEqualTo1 = endExclusive - 1;
    final int notEqualTo2 = endExclusive - 2;
    final int expectedValue = 9;
    when(random.nextInt()).thenReturn(notEqualTo1 - (endExclusive - startInclusive),
        notEqualTo2 + endExclusive - startInclusive, randomValue);

    assertEquals(expectedValue,
        randomIntGenerator.randomIntRange(startInclusive, endExclusive, notEqualTo1, notEqualTo2));
  }

  @ParameterizedTest
  @ValueSource(ints = { Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -1, 0, 1, Integer.MAX_VALUE - 1, Integer.MAX_VALUE })
  void testRandomIntUnique(final int randomValue) {
    final int notEqualTo1 = -1234;
    final int notEqualTo2 = 1234;
    when(random.nextInt()).thenReturn(notEqualTo1, notEqualTo2, randomValue);
    final int expectedValue = randomValue;

    assertEquals(expectedValue, randomIntGenerator.randomInt(notEqualTo1, notEqualTo2));
  }

}
