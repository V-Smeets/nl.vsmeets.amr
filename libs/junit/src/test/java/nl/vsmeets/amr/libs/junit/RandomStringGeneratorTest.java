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

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    final String expectedValue = "12345678";
    final Integer[] randomInts = expectedValue.chars().boxed().toArray(Integer[]::new);

    when(random.nextInt()).thenReturn(randomInts[0], Arrays.copyOfRange(randomInts, 1, randomInts.length));

    final String randomString = randomStringGenerator.randomString();
    assertAll( //
        () -> assertNotNull(randomString), //
        () -> assertEquals(expectedValue, randomString) //
    );
  }

  @Test
  void testRandomStringOfCharacters() {
    final String expectedValue = new String(new char[] { 0x0000, 0x0001, 0x7FFF, 0x8000, 0xFFFF });
    final Integer[] randomInts = expectedValue.chars().boxed().toArray(Integer[]::new);

    when(random.nextInt()).thenReturn(randomInts[0], Arrays.copyOfRange(randomInts, 1, randomInts.length));

    final String randomString = randomStringGenerator.randomStringOfCharacters(expectedValue.length());
    assertAll( //
        () -> assertNotNull(randomString), //
        () -> assertEquals(expectedValue, randomString) //
    );
  }

  @Test
  void testRandomStringOfCharactersNegativeLength() {
    final int length = -1;

    assertThrows(IllegalArgumentException.class, () -> randomStringGenerator.randomStringOfCharacters(length));
  }

  @Test
  void testRandomStringOfCharactersUnique() {
    final String notEqualTo1 = "Ab";
    final String notEqualTo2 = "XY";
    final String expectedValue = "Ok";
    final Integer[] randomInts =
        notEqualTo1.concat(notEqualTo2).concat(expectedValue).chars().boxed().toArray(Integer[]::new);

    when(random.nextInt()).thenReturn(randomInts[0], Arrays.copyOfRange(randomInts, 1, randomInts.length));

    final String randomString =
        randomStringGenerator.randomStringOfCharacters(expectedValue.length(), notEqualTo1, notEqualTo2);
    assertAll( //
        () -> assertNotNull(randomString), //
        () -> assertEquals(expectedValue, randomString) //
    );
  }

  @Test
  void testRandomStringOfCodePoints() {
    final String expectedValue = "A↹℘✋🎯🏁";
    final Integer[] randomInts = expectedValue.codePoints().boxed().toArray(Integer[]::new);
    when(random.nextInt()).thenReturn(randomInts[0], Arrays.copyOfRange(randomInts, 1, randomInts.length));

    final String randomString =
        randomStringGenerator.randomStringOfCodePoints(expectedValue.codePointCount(0, expectedValue.length()));
    assertAll( //
        () -> assertNotNull(randomString), //
        () -> assertEquals(expectedValue, randomString) //
    );
  }

  @Test
  void testRandomStringOfCodePointsNegativeLength() {
    final int length = -1;

    assertThrows(IllegalArgumentException.class, () -> randomStringGenerator.randomStringOfCodePoints(length));
  }

  @Test
  void testRandomStringOfCodePointsUnique() {
    final String notEqualTo1 = "123456";
    final String notEqualTo2 = "ABCDEF";
    final String expectedValue = "A↹℘✋🎯🏁";
    final Integer[] randomInts =
        notEqualTo1.concat(notEqualTo2).concat(expectedValue).codePoints().boxed().toArray(Integer[]::new);
    when(random.nextInt()).thenReturn(randomInts[0], Arrays.copyOfRange(randomInts, 1, randomInts.length));

    final String randomString = randomStringGenerator
        .randomStringOfCodePoints(expectedValue.codePointCount(0, expectedValue.length()), notEqualTo1, notEqualTo2);
    assertAll( //
        () -> assertNotNull(randomString), //
        () -> assertEquals(expectedValue, randomString) //
    );
  }

}
