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
 * Unit tests for the class {@link RandomStringGenerator}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class RandomStringGeneratorTest {

  @Mock
  private Random random;

  private RandomStringGenerator randomStringGenerator;

  @BeforeEach
  void setup() {
    randomStringGenerator = new RandomStringGenerator() {

      @Override
      public Random getRandom() {
        return random;
      }
    };
  }

  @Test
  void testRandomString() {
    when(random.nextInt()).thenReturn(0x0000, 0xD7FF, 0xDC00, 0xE000, 0xFFFF, 0x010000, 0x10FFFF);
    final char[] expectedValue = { 0x0000, 0xD7FF, 0xDC00, 0xE000, 0xFFFF, 0xD800, 0xDC00, 0xDBFF };

    final String randomString = randomStringGenerator.randomString();

    assertNotNull(randomString);
    assertEquals(8, randomString.length());
    assertArrayEquals(expectedValue, randomString.toCharArray());
  }

  @Test
  void testRandomStringNegativeLength() {
    final int length = -1;

    assertThrows(IllegalArgumentException.class, () -> randomStringGenerator.randomString(length));
  }

  @ParameterizedTest
  @ValueSource(ints = {
      // 2 bytes
      0x0000, 0XD7FF, 0xE000, 0xFFFF })
  void testRandomStringOneCharacter(final int randomValue) {
    when(random.nextInt()).thenReturn(randomValue);
    final int length = 1;
    final char[] expectedValue = Character.toChars(randomValue);

    final String randomString = randomStringGenerator.randomString(length);

    assertNotNull(randomString);
    assertEquals(length, randomString.length());
    assertArrayEquals(expectedValue, randomString.toCharArray());
  }

  @ParameterizedTest
  @ValueSource(ints = {
      // 4 bytes
      0x010000, 0x10FFFF })
  void testRandomStringOversized(final int randomValue) {
    when(random.nextInt()).thenReturn(randomValue);
    final int length = 1;
    final char[] expectedValue = Character.toChars(randomValue);

    final String randomString = randomStringGenerator.randomString(length);

    assertNotNull(randomString);
    assertEquals(length, randomString.length());
    assertEquals(expectedValue[0], randomString.charAt(0));
  }

  @ParameterizedTest
  @ValueSource(ints = {
      // 4 bytes
      0x010000, 0x10FFFF })
  void testRandomStringTwoCharacters(final int randomValue) {
    when(random.nextInt()).thenReturn(randomValue);
    final int length = 2;
    final char[] expectedValue = Character.toChars(randomValue);

    final String randomString = randomStringGenerator.randomString(length);

    assertNotNull(randomString);
    assertEquals(length, randomString.length());
    assertArrayEquals(expectedValue, randomString.toCharArray());
  }

  @Test
  void testRandomStringZeroLength() {
    final int length = 0;

    final String randomString = randomStringGenerator.randomString(length);

    assertNotNull(randomString);
    assertEquals(length, randomString.length());
  }

}
