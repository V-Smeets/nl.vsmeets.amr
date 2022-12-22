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
import javax.measure.quantity.Volume;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the class {@link WaterVolumeReadingEntity}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class WaterVolumeReadingEntityTest {

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
    private Quantity<Volume> consumedWater1;
    @Mock
    private Quantity<Volume> consumedWater2;

    @Test
    void testEquals() {
        final EqualsTester equalsTester = new EqualsTester();
        equalsTester.addEqualityGroup(new WaterVolumeReadingEntity());
        Stream.of(meter1, meter2).forEach( //
                meter -> Stream.of(localDateTime1, localDateTime2).forEach( //
                        localDateTime -> equalsTester.addEqualityGroup(Stream.of(consumedWater1, consumedWater2).map( //
                                consumedWater -> new WaterVolumeReadingEntity(meter, localDateTime, consumedWater))
                                .toArray())));
        equalsTester.testEquals();
    }

    @Test
    void testGetters() {
        final WaterVolumeReadingEntity waterVolumeReadingEntity = new WaterVolumeReadingEntity(meter1, localDateTime1,
                consumedWater1);

        assertAll( //
                () -> assertEquals(meter1, waterVolumeReadingEntity.getMeter()), //
                () -> assertEquals(localDateTime1, waterVolumeReadingEntity.getLocalDateTime()), //
                () -> assertEquals(consumedWater1, waterVolumeReadingEntity.getConsumedWater()) //
        );
    }

    @Test
    void testRequiredParameters() {
        assertThrows(NullPointerException.class,
                () -> new WaterVolumeReadingEntity(null, localDateTime1, consumedWater1));
        assertThrows(NullPointerException.class, () -> new WaterVolumeReadingEntity(meter1, null, consumedWater1));
        assertThrows(NullPointerException.class, () -> new WaterVolumeReadingEntity(meter1, localDateTime1, null));
    }

    @Test
    void testToString() {
        final WaterVolumeReadingEntity waterVolumeReadingEntity = new WaterVolumeReadingEntity();

        assertNotNull(waterVolumeReadingEntity.toString());
    }

    @Test
    void testToStringWithParameters() {
        final WaterVolumeReadingEntity waterVolumeReadingEntity = new WaterVolumeReadingEntity(meter1, localDateTime1,
                consumedWater1);

        assertAll( //
                () -> assertNotNull(waterVolumeReadingEntity.toString()), //
                () -> assertTrue(waterVolumeReadingEntity.toString()
                        .contains(waterVolumeReadingEntity.getClass().getSimpleName())), //
                () -> assertTrue(waterVolumeReadingEntity.toString().contains(localDateTime1.toString())), //
                () -> assertTrue(waterVolumeReadingEntity.toString().contains(consumedWater1.toString())) //
        );
    }

}
