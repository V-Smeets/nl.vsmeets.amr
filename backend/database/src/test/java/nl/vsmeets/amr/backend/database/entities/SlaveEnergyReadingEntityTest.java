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
 * Unit tests for the class {@link SlaveEnergyReadingEntity}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class SlaveEnergyReadingEntityTest {

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
    private Quantity<Energy> consumedEnergy1;
    @Mock
    private Quantity<Energy> consumedEnergy2;

    @Test
    void testEquals() {
        final EqualsTester equalsTester = new EqualsTester();
        equalsTester.addEqualityGroup(new SlaveEnergyReadingEntity());
        Stream.of(meter1, meter2).forEach( //
                meter -> Stream.of(localDateTime1, localDateTime2).forEach( //
                        localDateTime -> equalsTester.addEqualityGroup(Stream.of(consumedEnergy1, consumedEnergy2).map( //
                                consumedEnergy -> new SlaveEnergyReadingEntity(meter, localDateTime, consumedEnergy))
                                .toArray())));
        equalsTester.testEquals();
    }

    @Test
    void testGetters() {
        final SlaveEnergyReadingEntity slaveEnergyReadingEntity = new SlaveEnergyReadingEntity(meter1, localDateTime1,
                consumedEnergy1);

        assertAll( //
                () -> assertEquals(meter1, slaveEnergyReadingEntity.getMeter()), //
                () -> assertEquals(localDateTime1, slaveEnergyReadingEntity.getLocalDateTime()), //
                () -> assertEquals(consumedEnergy1, slaveEnergyReadingEntity.getConsumedEnergy()) //
        );
    }

    @Test
    void testRequiredParameters() {
        assertThrows(NullPointerException.class,
                () -> new SlaveEnergyReadingEntity(null, localDateTime1, consumedEnergy1));
        assertThrows(NullPointerException.class, () -> new SlaveEnergyReadingEntity(meter1, null, consumedEnergy1));
        assertThrows(NullPointerException.class, () -> new SlaveEnergyReadingEntity(meter1, localDateTime1, null));
    }

    @Test
    void testToString() {
        final SlaveEnergyReadingEntity slaveEnergyReadingEntity = new SlaveEnergyReadingEntity();

        assertNotNull(slaveEnergyReadingEntity.toString());
    }

    @Test
    void testToStringWithParameters() {
        final SlaveEnergyReadingEntity slaveEnergyReadingEntity = new SlaveEnergyReadingEntity(meter1, localDateTime1,
                consumedEnergy1);

        assertAll( //
                () -> assertNotNull(slaveEnergyReadingEntity.toString()), //
                () -> assertTrue(slaveEnergyReadingEntity.toString()
                        .contains(slaveEnergyReadingEntity.getClass().getSimpleName())), //
                () -> assertTrue(slaveEnergyReadingEntity.toString().contains(localDateTime1.toString())), //
                () -> assertTrue(slaveEnergyReadingEntity.toString().contains(consumedEnergy1.toString())) //
        );
    }

}
