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

import java.time.LocalDateTime;
import java.util.stream.Stream;

import javax.measure.Quantity;
import javax.measure.quantity.Power;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the class {@link ElectricPowerReadingEntity}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricPowerReadingEntityTest {

    /**
     * Values used during tests.
     */
    private static final LocalDateTime localDateTime1 = LocalDateTime.MIN;
    private static final LocalDateTime localDateTime2 = LocalDateTime.MAX;

    @Mock
    private MeterEntity meter1;
    @Mock
    private MeterEntity meter2;
    @Mock
    private Quantity<Power> consumedPower1;
    @Mock
    private Quantity<Power> consumedPower2;
    @Mock
    private Quantity<Power> producedPower1;
    @Mock
    private Quantity<Power> producedPower2;

    @Test
    void testEquals() {
        final EqualsTester equalsTester = new EqualsTester();
        equalsTester.addEqualityGroup(new ElectricPowerReadingEntity());
        Stream.of(meter1, meter2).forEach( //
                meter -> Stream.of(localDateTime1, localDateTime2).forEach( //
                        localDateTime -> equalsTester.addEqualityGroup(Stream.of(consumedPower1, consumedPower2)
                                .flatMap( //
                                        consumedPower -> Stream.of(producedPower1, producedPower2).map( //
                                                producedPower -> new ElectricPowerReadingEntity(meter, localDateTime,
                                                        consumedPower, producedPower)))
                                .toArray())));
        equalsTester.testEquals();
    }

    @Test
    void testGetters() {
        final ElectricPowerReadingEntity electricPowerReadingEntity = new ElectricPowerReadingEntity(meter1,
                localDateTime1, consumedPower1, producedPower1);

        assertAll( //
                () -> assertEquals(meter1, electricPowerReadingEntity.getMeter()), //
                () -> assertEquals(localDateTime1, electricPowerReadingEntity.getLocalDateTime()), //
                () -> assertEquals(consumedPower1, electricPowerReadingEntity.getConsumedPower()), //
                () -> assertEquals(producedPower1, electricPowerReadingEntity.getProducedPower()) //
        );
    }

    @Test
    void testRequiredParameters() {
        assertThrows(NullPointerException.class,
                () -> new ElectricPowerReadingEntity(null, localDateTime1, consumedPower1, producedPower1));
        assertThrows(NullPointerException.class,
                () -> new ElectricPowerReadingEntity(meter1, null, consumedPower1, producedPower1));
        assertThrows(NullPointerException.class,
                () -> new ElectricPowerReadingEntity(meter1, localDateTime1, null, producedPower1));
        assertThrows(NullPointerException.class,
                () -> new ElectricPowerReadingEntity(meter1, localDateTime1, consumedPower1, null));
    }

    @Test
    void testToString() {
        final ElectricPowerReadingEntity electricPowerReadingEntity = new ElectricPowerReadingEntity();

        assertNotNull(electricPowerReadingEntity.toString());
    }

    @Test
    void testToStringWithParameters() {
        final ElectricPowerReadingEntity electricPowerReadingEntity = new ElectricPowerReadingEntity(meter1,
                localDateTime1, consumedPower1, producedPower1);

        assertAll( //
                () -> assertNotNull(electricPowerReadingEntity.toString()), //
                () -> assertTrue(electricPowerReadingEntity.toString()
                        .contains(electricPowerReadingEntity.getClass().getSimpleName())), //
                () -> assertTrue(electricPowerReadingEntity.toString().contains(localDateTime1.toString())), //
                () -> assertTrue(electricPowerReadingEntity.toString().contains(consumedPower1.toString())), //
                () -> assertTrue(electricPowerReadingEntity.toString().contains(producedPower1.toString())) //
        );
    }

}
