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
package nl.vsmeets.amr.backend.database.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.testing.EqualsTester;

import nl.vsmeets.amr.libs.junit.RandomDurationGenerator;
import nl.vsmeets.amr.libs.junit.RandomLocalDateTimeGenerator;

/**
 * Unit tests for the class {@link ElectricPowerFailureEventEntity}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricPowerFailureEventEntityTest implements RandomDurationGenerator, RandomLocalDateTimeGenerator {

  @Mock
  private ElectricPowerFailuresEntity electricPowerFailures1;
  @Mock
  private ElectricPowerFailuresEntity electricPowerFailures2;
  private final LocalDateTime endOfFailureTime1 = randomLocalDateTime();
  private final LocalDateTime endOfFailureTime2 = randomLocalDateTime();
  private final Duration failureDuration1 = randomDuration();
  private final Duration failureDuration2 = randomDuration();

  @Test
  void testEquals() {
    final EqualsTester equalsTester = new EqualsTester();
    equalsTester.addEqualityGroup(new ElectricPowerFailureEventEntity());
    Stream.of(electricPowerFailures1, electricPowerFailures2).forEach(electricPowerFailures -> //
    Stream.of(endOfFailureTime1, endOfFailureTime2).forEach(endOfFailureTime -> //
    equalsTester.addEqualityGroup( //
        Stream.of(failureDuration1, failureDuration2).map(failureDuration -> //
        new ElectricPowerFailureEventEntity(electricPowerFailures, endOfFailureTime, failureDuration)).toArray())));
    equalsTester.testEquals();
  }

  @Test
  void testGetters() {
    final ElectricPowerFailureEventEntity electricPowerFailureEventEntity =
        new ElectricPowerFailureEventEntity(electricPowerFailures1, endOfFailureTime1, failureDuration1);

    // @formatter:off
    assertAll(
        () -> assertEquals(electricPowerFailures1, electricPowerFailureEventEntity.getElectricPowerFailures()),
        () -> assertEquals(endOfFailureTime1, electricPowerFailureEventEntity.getEndOfFailureTime()),
        () -> assertEquals(failureDuration1, electricPowerFailureEventEntity.getFailureDuration()));
    // @formatter:on
  }

  @Test
  void testRequiredParameters() {
    assertThrows(NullPointerException.class,
        () -> new ElectricPowerFailureEventEntity(null, endOfFailureTime1, failureDuration1));
    assertThrows(NullPointerException.class,
        () -> new ElectricPowerFailureEventEntity(electricPowerFailures1, null, failureDuration1));
    assertThrows(NullPointerException.class,
        () -> new ElectricPowerFailureEventEntity(electricPowerFailures1, endOfFailureTime1, null));
  }

  @Test
  void testToString() {
    final ElectricPowerFailureEventEntity electricPowerFailureEventEntity = new ElectricPowerFailureEventEntity();

    assertNotNull(electricPowerFailureEventEntity.toString());
  }

  @Test
  void testToStringWithParameters() {
    final ElectricPowerFailureEventEntity electricPowerFailureEventEntity =
        new ElectricPowerFailureEventEntity(electricPowerFailures1, endOfFailureTime1, failureDuration1);

    // @formatter:off
    assertAll(
        () -> assertNotNull(electricPowerFailureEventEntity.toString()),
        () -> assertTrue(electricPowerFailureEventEntity.toString().contains(electricPowerFailureEventEntity.getClass().getSimpleName())),
        () -> assertTrue(electricPowerFailureEventEntity.toString().contains(endOfFailureTime1.toString())),
        () -> assertTrue(electricPowerFailureEventEntity.toString().contains(failureDuration1.toString()))
        );
    // @formatter:on
  }

}
