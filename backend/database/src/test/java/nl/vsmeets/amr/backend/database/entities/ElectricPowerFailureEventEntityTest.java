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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the class {@link ElectricPowerFailureEventEntity}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricPowerFailureEventEntityTest {

    /**
     * Values used during tests.
     */
    private static final LocalDateTime endOfFailureTime1 = LocalDateTime.MIN;
    private static final LocalDateTime endOfFailureTime2 = LocalDateTime.MAX;
    private static final Duration failureDuration1 = Duration.ofSeconds(1L, 2L);
    private static final Duration failureDuration2 = Duration.ofSeconds(3L, 4L);

    @Mock
    private ElectricPowerFailuresEntity electricPowerFailures1;
    @Mock
    private ElectricPowerFailuresEntity electricPowerFailures2;

    @Test
    void testEquals() {
        final EqualsTester equalsTester = new EqualsTester();
        equalsTester.addEqualityGroup(new ElectricPowerFailureEventEntity());
        Stream.of(electricPowerFailures1, electricPowerFailures2).forEach( //
                electricPowerFailures -> Stream.of(endOfFailureTime1, endOfFailureTime2).forEach( //
                        endOfFailureTime -> equalsTester.addEqualityGroup(Stream.of(failureDuration1, failureDuration2)
                                .map( //
                                        failureDuration -> new ElectricPowerFailureEventEntity(electricPowerFailures,
                                                endOfFailureTime, failureDuration))
                                .toArray())));
        equalsTester.testEquals();
    }

    @Test
    void testGetters() {
        final ElectricPowerFailureEventEntity electricPowerFailureEventEntity = new ElectricPowerFailureEventEntity(
                electricPowerFailures1, endOfFailureTime1, failureDuration1);

        assertAll( //
                () -> assertEquals(electricPowerFailures1, electricPowerFailureEventEntity.getElectricPowerFailures()), //
                () -> assertEquals(endOfFailureTime1, electricPowerFailureEventEntity.getEndOfFailureTime()), //
                () -> assertEquals(failureDuration1, electricPowerFailureEventEntity.getFailureDuration()) //
        );
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
        final ElectricPowerFailureEventEntity electricPowerFailureEventEntity = new ElectricPowerFailureEventEntity(
                electricPowerFailures1, endOfFailureTime1, failureDuration1);

        assertAll( //
                () -> assertNotNull(electricPowerFailureEventEntity.toString()), //
                () -> assertTrue(electricPowerFailureEventEntity.toString()
                        .contains(electricPowerFailureEventEntity.getClass().getSimpleName())), //
                () -> assertTrue(electricPowerFailureEventEntity.toString().contains(endOfFailureTime1.toString())), //
                () -> assertTrue(electricPowerFailureEventEntity.toString().contains(failureDuration1.toString())) //
        );
    }

}
