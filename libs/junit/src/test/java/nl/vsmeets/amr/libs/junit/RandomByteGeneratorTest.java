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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link RandomByteGenerator}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class RandomByteGeneratorTest {

  @Mock
  private Random random;

  private RandomByteGenerator randomByteGenerator;

  @BeforeEach
  void setup() {
    randomByteGenerator = new RandomByteGenerator() {

      @Override
      public Random getRandom() {
        return random;
      }
    };
  }

  @ParameterizedTest
  @ValueSource(ints = { Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -1, 0, 1, Integer.MAX_VALUE - 1, Integer.MAX_VALUE })
  void testRandomByte(final int randomValue) {
    when(random.nextInt()).thenReturn(randomValue);
    final byte expectedValue = (byte) randomValue;

    assertEquals(expectedValue, randomByteGenerator.randomByte());
  }

  @ParameterizedTest
  @ValueSource(ints = { -389, -133, 123, 379, 635 })
  void testRandomByteStaticResult(final int randomValue) {
    when(random.nextInt()).thenReturn(randomValue);
    final byte expectedValue = 123;

    assertEquals(expectedValue, randomByteGenerator.randomByte());
  }

}
