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

import java.time.Duration;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link RandomDurationGenerator}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class RandomDurationGeneratorTest {

  @Mock
  private Random random;

  private RandomDurationGenerator randomDurationGenerator;

  @BeforeEach
  void setup() {
    randomDurationGenerator = new RandomDurationGenerator() {

      @Override
      public Random getRandom() {
        return random;
      }
    };
  }

  @ParameterizedTest
  @ValueSource(longs = { -999_999_999L, -1L, 0L, 1L, 999_999_999L })
  void testRandomDurationSeconds(final long seconds) {
    final int nanos = 0;
    when(random.nextLong()).thenReturn(seconds);
    final Duration expectedValue = Duration.ofSeconds(seconds, nanos);

    assertEquals(expectedValue, randomDurationGenerator.randomDurationSeconds());
  }

  @ParameterizedTest
  @ValueSource(longs = { -999_999_999L, -1L, 0L, 1L, 999_999_999L })
  void testRandomDurationSecondsUnique(final long seconds) {
    final Duration notEqualTo1 = Duration.ofSeconds(2L);
    final Duration notEqualTo2 = Duration.ofSeconds(3L);
    when(random.nextLong()).thenReturn(notEqualTo1.getSeconds(), notEqualTo2.getSeconds(), seconds);
    final Duration expectedValue = Duration.ofSeconds(seconds);

    assertEquals(expectedValue, randomDurationGenerator.randomDurationSeconds(notEqualTo1, notEqualTo2));
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 999_999_999 })
  void testRandomDurationWithNanos(final int nanos) {
    final long seconds = 0L;
    when(random.nextLong()).thenReturn(seconds);
    when(random.nextInt()).thenReturn(nanos);
    final Duration expectedValue = Duration.ofSeconds(seconds, nanos);

    assertEquals(expectedValue, randomDurationGenerator.randomDuration());
  }

  @ParameterizedTest
  @ValueSource(longs = { -999_999_999L, -1L, 0L, 1L, 999_999_999L })
  void testRandomDurationWithSeconds(final long seconds) {
    final int nanos = 0;
    when(random.nextLong()).thenReturn(seconds);
    when(random.nextInt()).thenReturn(nanos);
    final Duration expectedValue = Duration.ofSeconds(seconds, nanos);

    assertEquals(expectedValue, randomDurationGenerator.randomDuration());
  }

  @ParameterizedTest
  @ValueSource(longs = { -999_999_999L, -1L, 0L, 1L, 999_999_999L })
  void testRandomDurationWithSecondsUnique(final long seconds) {
    final Duration notEqualTo1 = Duration.ofSeconds(1L, 2);
    final Duration notEqualTo2 = Duration.ofSeconds(2L, 3);
    final int nanos = 0;
    when(random.nextLong()).thenReturn(notEqualTo1.getSeconds(), notEqualTo2.getSeconds(), seconds);
    when(random.nextInt()).thenReturn(notEqualTo1.getNano(), notEqualTo2.getNano(), nanos);
    final Duration expectedValue = Duration.ofSeconds(seconds, nanos);

    assertEquals(expectedValue, randomDurationGenerator.randomDuration(notEqualTo1, notEqualTo2));
  }

}
