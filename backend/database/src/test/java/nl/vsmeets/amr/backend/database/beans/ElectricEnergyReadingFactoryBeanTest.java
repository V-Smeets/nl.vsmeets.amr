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
import javax.measure.quantity.Energy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.ElectricEnergyReading;
import nl.vsmeets.amr.backend.database.entities.ElectricEnergyReadingEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;

/**
 * Unit tests for the class {@link ElectricEnergyReadingFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricEnergyReadingFactoryBeanTest {

    /**
     * Values used during tests.
     */
    private static final LocalDateTime localDateTime = LocalDateTime.MIN;
    private static final Short tariffIndicator = Short.MIN_VALUE;

    /**
     * Constructor parameters.
     */
    @Mock
    private ElectricEnergyReadingRepository repository;

    /**
     * The object under test.
     */
    @InjectMocks
    private ElectricEnergyReadingFactoryBean electricEnergyReadingFactoryBean;

    @Test
    void testCreate(@Mock final MeterEntity meter, @Mock final Quantity<Energy> consumedEnergy,
            @Mock final Quantity<Energy> producedEnergy) {
        Mockito.when(repository.save(any(ElectricEnergyReadingEntity.class))).then(i -> i.getArgument(0));

        final ElectricEnergyReading result = assertDoesNotThrow(() -> electricEnergyReadingFactoryBean.create(meter,
                localDateTime, tariffIndicator, consumedEnergy, producedEnergy));
        verify(repository).refresh(any(ElectricEnergyReadingEntity.class));
        assertAll( //
                () -> assertNull(result.getId()), //
                () -> assertEquals(meter, result.getMeter()), //
                () -> assertEquals(localDateTime, result.getLocalDateTime()), //
                () -> assertEquals(tariffIndicator, result.getTariffIndicator()), //
                () -> assertEquals(consumedEnergy, result.getConsumedEnergy()), //
                () -> assertEquals(producedEnergy, result.getProducedEnergy()) //
        );
    }

    @Test
    void testCreateDataIntegrityViolationException(@Mock final MeterEntity meter,
            @Mock final Quantity<Energy> consumedEnergy, @Mock final Quantity<Energy> producedEnergy) {
        Mockito.when(repository.save(any(ElectricEnergyReadingEntity.class)))
                .thenThrow(new DataIntegrityViolationException(null));

        assertThrows(ConstraintViolationException.class, () -> electricEnergyReadingFactoryBean.create(meter,
                localDateTime, tariffIndicator, consumedEnergy, producedEnergy));
    }

    @Test
    void testFind(@Mock final MeterEntity meter, @Mock final ElectricEnergyReading electricEnergyReading) {
        final Optional<? extends ElectricEnergyReading> expectedResult = Optional.of(electricEnergyReading);
        Mockito.when(repository.findByMeterAndLocalDateTimeAndTariffIndicator(meter, localDateTime, tariffIndicator))
                .then(i -> expectedResult);

        assertEquals(expectedResult, electricEnergyReadingFactoryBean.find(meter, localDateTime, tariffIndicator));
    }

}
