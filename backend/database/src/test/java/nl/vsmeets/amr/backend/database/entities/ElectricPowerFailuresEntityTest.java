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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the class {@link ElectricPowerFailuresEntity}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricPowerFailuresEntityTest {

    /**
     * Values used during tests.
     */
    private static final LocalDateTime localDateTime1 = LocalDateTime.MIN;
    private static final LocalDateTime localDateTime2 = LocalDateTime.MAX;
    private static final Integer nrOfPowerFailures1 = 1;
    private static final Integer nrOfPowerFailures2 = 2;
    private static final Integer nrOfLongPowerFailures1 = 3;
    private static final Integer nrOfLongPowerFailures2 = 4;

    @Mock
    private MeterEntity meter1;
    @Mock
    private MeterEntity meter2;

    @Test
    void testEquals() {
        final EqualsTester equalsTester = new EqualsTester();
        equalsTester.addEqualityGroup(new ElectricPowerFailuresEntity());
        Stream.of(meter1, meter2).forEach( //
                meter -> Stream.of(localDateTime1, localDateTime2).forEach( //
                        localDateTime -> equalsTester.addEqualityGroup(Stream.of(nrOfPowerFailures1, nrOfPowerFailures2)
                                .flatMap( //
                                        nrOfPowerFailures -> Stream.of(nrOfLongPowerFailures1, nrOfLongPowerFailures2)
                                                .map( //
                                                        nrOfLongPowerFailures -> new ElectricPowerFailuresEntity(meter,
                                                                localDateTime, nrOfPowerFailures,
                                                                nrOfLongPowerFailures)))
                                .toArray())));
        equalsTester.testEquals();
    }

    @Test
    void testGetters() {
        final ElectricPowerFailuresEntity electricPowerFailuresEntity = new ElectricPowerFailuresEntity(meter1,
                localDateTime1, nrOfPowerFailures1, nrOfLongPowerFailures1);

        assertAll( //
                () -> assertEquals(meter1, electricPowerFailuresEntity.getMeter()), //
                () -> assertEquals(localDateTime1, electricPowerFailuresEntity.getLocalDateTime()), //
                () -> assertEquals(nrOfPowerFailures1, electricPowerFailuresEntity.getNrOfPowerFailures()), //
                () -> assertEquals(nrOfLongPowerFailures1, electricPowerFailuresEntity.getNrOfLongPowerFailures()), //
                () -> assertNull(electricPowerFailuresEntity.getElectricPowerFailureEvents()) //
        );
    }

    @Test
    void testRequiredParameters() {
        assertThrows(NullPointerException.class, () -> new ElectricPowerFailuresEntity(null, localDateTime1,
                nrOfPowerFailures1, nrOfLongPowerFailures1));
        assertThrows(NullPointerException.class,
                () -> new ElectricPowerFailuresEntity(meter1, null, nrOfPowerFailures1, nrOfLongPowerFailures1));
        assertThrows(NullPointerException.class,
                () -> new ElectricPowerFailuresEntity(meter1, localDateTime1, null, nrOfLongPowerFailures1));
        assertThrows(NullPointerException.class,
                () -> new ElectricPowerFailuresEntity(meter1, localDateTime1, nrOfPowerFailures1, null));
    }

    @Test
    void testToString() {
        final ElectricPowerFailuresEntity electricPowerFailuresEntity = new ElectricPowerFailuresEntity();

        assertNotNull(electricPowerFailuresEntity.toString());
    }

    @Test
    void testToStringWithParameters() {
        final ElectricPowerFailuresEntity electricPowerFailuresEntity = new ElectricPowerFailuresEntity(meter1,
                localDateTime1, nrOfPowerFailures1, nrOfLongPowerFailures1);

        assertAll( //
                () -> assertNotNull(electricPowerFailuresEntity.toString()), //
                () -> assertTrue(electricPowerFailuresEntity.toString()
                        .contains(electricPowerFailuresEntity.getClass().getSimpleName())), //
                () -> assertTrue(electricPowerFailuresEntity.toString().contains(localDateTime1.toString())), //
                () -> assertTrue(electricPowerFailuresEntity.toString().contains(nrOfPowerFailures1.toString())), //
                () -> assertTrue(electricPowerFailuresEntity.toString().contains(nrOfLongPowerFailures1.toString())) //
        );
    }

}
