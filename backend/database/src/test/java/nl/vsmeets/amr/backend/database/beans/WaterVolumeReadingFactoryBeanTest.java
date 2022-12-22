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
package nl.vsmeets.amr.backend.database.beans;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.measure.Quantity;
import javax.measure.quantity.Volume;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.WaterVolumeReading;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;
import nl.vsmeets.amr.backend.database.entities.WaterVolumeReadingEntity;

/**
 * Unit tests for the class {@link WaterVolumeReadingFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class WaterVolumeReadingFactoryBeanTest {

    /**
     * Values used during tests.
     */
    private static final LocalDateTime localDateTime = LocalDateTime.MIN;

    /**
     * Constructor parameters.
     */
    @Mock
    private WaterVolumeReadingRepository repository;

    /**
     * The object under test.
     */
    @InjectMocks
    private WaterVolumeReadingFactoryBean testObject;

    @Test
    void testCreate(@Mock final MeterEntity meter, @Mock final Quantity<Volume> consumedWater) {
        Mockito.when(repository.save(any(WaterVolumeReadingEntity.class))).then(i -> i.getArgument(0));

        final WaterVolumeReading result = assertDoesNotThrow(
                () -> testObject.create(meter, localDateTime, consumedWater));
        verify(repository).refresh(any(WaterVolumeReadingEntity.class));
        assertAll( //
                () -> assertNull(result.getId()), //
                () -> assertEquals(meter, result.getMeter()), //
                () -> assertEquals(localDateTime, result.getLocalDateTime()), //
                () -> assertEquals(consumedWater, result.getConsumedWater()) //
        );
    }

    @Test
    void testCreateDataIntegrityViolationException(@Mock final MeterEntity meter,
            @Mock final Quantity<Volume> consumedWater) {
        Mockito.when(repository.save(any(WaterVolumeReadingEntity.class)))
                .thenThrow(new DataIntegrityViolationException(null));

        assertThrows(ConstraintViolationException.class, () -> testObject.create(meter, localDateTime, consumedWater));
    }

    @Test
    void testFind(@Mock final MeterEntity meter, @Mock final WaterVolumeReading waterVolumeReading) {
        final Optional<? extends WaterVolumeReading> expectedResult = Optional.of(waterVolumeReading);

        Mockito.when(repository.findByMeterAndLocalDateTime(meter, localDateTime)).then(i -> expectedResult);

        assertEquals(expectedResult, testObject.find(meter, localDateTime));
    }

}
