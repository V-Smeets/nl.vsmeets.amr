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
import javax.measure.quantity.Energy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the class {@link ElectricEnergyReadingEntity}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricEnergyReadingEntityTest {

    /**
     * Values used during tests.
     */
    private static final LocalDateTime localDateTime1 = LocalDateTime.MIN;
    private static final LocalDateTime localDateTime2 = LocalDateTime.MAX;
    private static final Short tariffIndicator1 = 1;
    private static final Short tariffIndicator2 = 2;

    @Mock
    private MeterEntity meter1;
    @Mock
    private MeterEntity meter2;
    @Mock
    private Quantity<Energy> consumedEnergy1;
    @Mock
    private Quantity<Energy> consumedEnergy2;
    @Mock
    private Quantity<Energy> producedEnergy1;
    @Mock
    private Quantity<Energy> producedEnergy2;

    @Test
    void testEquals() {
        final EqualsTester equalsTester = new EqualsTester();
        equalsTester.addEqualityGroup(new ElectricEnergyReadingEntity());
        Stream.of(meter1, meter2).forEach( //
                meter -> Stream.of(localDateTime1, localDateTime2).forEach( //
                        localDateTime -> Stream.of(tariffIndicator1, tariffIndicator2).forEach( //
                                tariffIndicator -> equalsTester
                                        .addEqualityGroup(Stream.of(consumedEnergy1, consumedEnergy2).flatMap( //
                                                consumedEnergy -> Stream.of(producedEnergy1, producedEnergy2).map( //
                                                        producedEnergy -> new ElectricEnergyReadingEntity(meter,
                                                                localDateTime, tariffIndicator, consumedEnergy,
                                                                producedEnergy)))
                                                .toArray()))));
        equalsTester.testEquals();
    }

    @Test
    void testGetters() {
        final ElectricEnergyReadingEntity electricEnergyReadingEntity = new ElectricEnergyReadingEntity(meter1,
                localDateTime1, tariffIndicator1, consumedEnergy1, producedEnergy1);

        assertAll( //
                () -> assertEquals(meter1, electricEnergyReadingEntity.getMeter()), //
                () -> assertEquals(localDateTime1, electricEnergyReadingEntity.getLocalDateTime()), //
                () -> assertEquals(tariffIndicator1, electricEnergyReadingEntity.getTariffIndicator()), //
                () -> assertEquals(consumedEnergy1, electricEnergyReadingEntity.getConsumedEnergy()), //
                () -> assertEquals(producedEnergy1, electricEnergyReadingEntity.getProducedEnergy()) //
        );
    }

    @Test
    void testRequiredParameters() {
        assertThrows(NullPointerException.class, () -> new ElectricEnergyReadingEntity(null, localDateTime1,
                tariffIndicator1, consumedEnergy1, producedEnergy1));
        assertThrows(NullPointerException.class, () -> new ElectricEnergyReadingEntity(meter1, null, tariffIndicator1,
                consumedEnergy1, producedEnergy1));
        assertThrows(NullPointerException.class,
                () -> new ElectricEnergyReadingEntity(meter1, localDateTime1, null, consumedEnergy1, producedEnergy1));
        assertThrows(NullPointerException.class,
                () -> new ElectricEnergyReadingEntity(meter1, localDateTime1, tariffIndicator1, null, producedEnergy1));
        assertThrows(NullPointerException.class,
                () -> new ElectricEnergyReadingEntity(meter1, localDateTime1, tariffIndicator1, consumedEnergy1, null));
    }

    @Test
    void testToString() {
        final ElectricEnergyReadingEntity electricEnergyReadingEntity = new ElectricEnergyReadingEntity();

        assertNotNull(electricEnergyReadingEntity.toString());
    }

    @Test
    void testToStringWithParameters() {
        final ElectricEnergyReadingEntity electricEnergyReadingEntity = new ElectricEnergyReadingEntity(meter1,
                localDateTime1, tariffIndicator1, consumedEnergy1, producedEnergy1);

        assertAll( //
                () -> assertNotNull(electricEnergyReadingEntity.toString()), //
                () -> assertTrue(electricEnergyReadingEntity.toString()
                        .contains(electricEnergyReadingEntity.getClass().getSimpleName())), //
                () -> assertTrue(electricEnergyReadingEntity.toString().contains(localDateTime1.toString())), //
                () -> assertTrue(electricEnergyReadingEntity.toString().contains(tariffIndicator1.toString())), //
                () -> assertTrue(electricEnergyReadingEntity.toString().contains(consumedEnergy1.toString())), //
                () -> assertTrue(electricEnergyReadingEntity.toString().contains(producedEnergy1.toString())) //
        );
    }

}
