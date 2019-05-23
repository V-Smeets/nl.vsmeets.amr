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

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link RandomShortGenerator}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class RandomShortGeneratorTest {

  @Mock
  private Random random;

  private RandomShortGenerator randomShortGenerator;

  @BeforeEach
  void setup() {
    randomShortGenerator = new RandomShortGenerator() {

      @Override
      public Random getRandom() {
        return random;
      }
    };
  }

  @ParameterizedTest
  @ValueSource(shorts = { Short.MIN_VALUE, Short.MIN_VALUE + 1, -1, 0, 1, Short.MAX_VALUE - 1, Short.MAX_VALUE })
  void testRandomShort(final short randomValue) {
    final short expectedValue = randomValue;
    when(random.nextInt()).thenReturn((int) randomValue);

    assertEquals(expectedValue, randomShortGenerator.randomShort());
  }

  @Test
  void testRandomShortRangeInRange() {
    final short randomValue = 123;
    final short startInclusive = Short.MIN_VALUE + 10;
    final short endExclusive = Short.MAX_VALUE - 10;
    final short expectedValue = randomValue;
    when(random.nextInt()).thenReturn((int) randomValue);

    assertEquals(expectedValue, randomShortGenerator.randomShortRange(startInclusive, endExclusive));
  }

  @ParameterizedTest
  @ValueSource(shorts = { -7, 1, 17 })
  void testRandomShortRangeSmallOutOfRangeUnique(final short randomValue) {
    final short startInclusive = 8;
    final short endExclusive = startInclusive + 8;
    final short notEqualTo1 = endExclusive - 1;
    final short notEqualTo2 = endExclusive - 2;
    final short expectedValue = 9;
    when(random.nextInt()).thenReturn(notEqualTo1 - (endExclusive - startInclusive),
        notEqualTo2 + (endExclusive - startInclusive), (int) randomValue);

    assertEquals(expectedValue,
        randomShortGenerator.randomShortRange(startInclusive, endExclusive, notEqualTo1, notEqualTo2));
  }

  @ParameterizedTest
  @ValueSource(shorts = { Short.MIN_VALUE, Short.MIN_VALUE + 1, -1, 0, 1, Short.MAX_VALUE - 1, Short.MAX_VALUE })
  void testRandomShortUnique(final short randomValue) {
    final short notEqualTo1 = -1234;
    final short notEqualTo2 = 1234;
    when(random.nextInt()).thenReturn((int) notEqualTo1, (int) notEqualTo2, (int) randomValue);
    final int expectedValue = randomValue;

    assertEquals(expectedValue, randomShortGenerator.randomShort(notEqualTo1, notEqualTo2));
  }

}
