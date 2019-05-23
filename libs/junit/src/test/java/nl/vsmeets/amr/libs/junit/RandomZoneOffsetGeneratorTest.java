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

import java.time.ZoneOffset;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link RandomZoneOffsetGenerator}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class RandomZoneOffsetGeneratorTest {

  @Mock
  private Random random;

  private RandomZoneOffsetGenerator randomZoneOffsetGenerator;

  @BeforeEach
  void setup() {
    randomZoneOffsetGenerator = new RandomZoneOffsetGenerator() {

      @Override
      public Random getRandom() {
        return random;
      }
    };
  }

  @ParameterizedTest
  @ValueSource(ints = { -64800, -1, 0, 1, 64800 })
  void testRandomZoneOffset(final int randomValue) {
    final ZoneOffset expectedValue = ZoneOffset.ofTotalSeconds(randomValue);
    when(random.nextInt()).thenReturn(randomValue);

    assertEquals(expectedValue, randomZoneOffsetGenerator.randomZoneOffset());
  }

  @ParameterizedTest
  @ValueSource(ints = { -64800, -1, 0, 1, 64800 })
  void testRandomZoneOffsetUnique(final int randomValue) {
    final ZoneOffset notEqualTo1 = ZoneOffset.ofTotalSeconds(-123);
    final ZoneOffset notEqualTo2 = ZoneOffset.ofTotalSeconds(123);
    final ZoneOffset expectedValue = ZoneOffset.ofTotalSeconds(randomValue);
    when(random.nextInt()).thenReturn(notEqualTo1.getTotalSeconds(), notEqualTo2.getTotalSeconds(), randomValue);

    assertEquals(expectedValue, randomZoneOffsetGenerator.randomZoneOffset(notEqualTo1, notEqualTo2));
  }

}
