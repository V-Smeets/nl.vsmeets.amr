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

import java.time.ZoneId;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link RandomZoneIdGenerator}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class RandomZoneIdGeneratorTest {

  @Mock
  private Random random;

  private RandomZoneIdGenerator randomZoneIdGenerator;

  @BeforeEach
  void setup() {
    randomZoneIdGenerator = new RandomZoneIdGenerator() {

      @Override
      public Random getRandom() {
        return random;
      }
    };
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6 })
  void testRandomZoneId(final int randomValue) {
    final String[] zoneIdNames = ZoneId.getAvailableZoneIds().toArray(new String[0]);
    final int nrOfZoneIds = zoneIdNames.length;
    final int index = randomValue % nrOfZoneIds;
    final ZoneId expectedValue = ZoneId.of(zoneIdNames[index]);
    when(random.nextInt()).thenReturn(index);

    assertEquals(expectedValue, randomZoneIdGenerator.randomZoneId());
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6 })
  void testRandomZoneIdUnique(final int randomValue) {
    final String[] zoneIdNames = ZoneId.getAvailableZoneIds().toArray(new String[0]);
    final int nrOfZoneIds = zoneIdNames.length;
    final int notEqualTo1Index = nrOfZoneIds - 1;
    final ZoneId notEqualTo1 = ZoneId.of(zoneIdNames[notEqualTo1Index]);
    final int notEqualTo2Index = nrOfZoneIds - 2;
    final ZoneId notEqualTo2 = ZoneId.of(zoneIdNames[notEqualTo2Index]);
    final int index = randomValue % nrOfZoneIds;
    final ZoneId expectedValue = ZoneId.of(zoneIdNames[index]);
    when(random.nextInt()).thenReturn(notEqualTo1Index, notEqualTo2Index, index);

    assertEquals(expectedValue, randomZoneIdGenerator.randomZoneId(notEqualTo1, notEqualTo2));
  }

}
