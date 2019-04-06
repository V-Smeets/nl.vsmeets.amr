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
    when(random.nextInt()).thenReturn(randomValue);
    final int expectedValue = randomValue;

    assertEquals(expectedValue, randomIntGenerator.randomInt());
  }

  @Test
  void testRandomIntInvalidRange() {
    final int startInclusive = 123;
    final int endExclusive = startInclusive;

    assertThrows(IllegalArgumentException.class, () -> randomIntGenerator.randomInt(startInclusive, endExclusive));
  }

  @Test
  void testRandomIntLargeRange() {
    final int randomValue = 123;
    when(random.nextInt()).thenReturn(Integer.MIN_VALUE, Integer.MAX_VALUE, randomValue);
    final int startInclusive = Integer.MIN_VALUE + 10;
    final int endExclusive = Integer.MAX_VALUE - 10;
    final int expectedValue = randomValue;

    assertEquals(expectedValue, randomIntGenerator.randomInt(startInclusive, endExclusive));
  }

  @ParameterizedTest
  @ValueSource(ints = { -15, -7, 1, 9, 17, 25 })
  void testRandomIntSmallRange(final int randomValue) {
    when(random.nextInt()).thenReturn(randomValue);
    final int startInclusive = 8;
    final int endExclusive = startInclusive + 8;
    final int expectedValue = 9;

    assertEquals(expectedValue, randomIntGenerator.randomInt(startInclusive, endExclusive));
  }

}
