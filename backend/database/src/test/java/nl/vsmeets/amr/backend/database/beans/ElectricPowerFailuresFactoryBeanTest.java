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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.ElectricPowerFailures;
import nl.vsmeets.amr.backend.database.entities.ElectricPowerFailuresEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;

/**
 * Unit tests for the class {@link ElectricPowerFailuresFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricPowerFailuresFactoryBeanTest {

    /**
     * Values used during tests.
     */
    private static final LocalDateTime localDateTime = LocalDateTime.MIN;
    private static final Integer nrOfPowerFailures = Integer.MIN_VALUE;
    private static final Integer nrOfLongPowerFailures = Integer.MIN_VALUE;

    /**
     * Constructor parameters.
     */
    @Mock
    private ElectricPowerFailuresRepository repository;

    /**
     * The object under test.
     */
    @InjectMocks
    private ElectricPowerFailuresFactoryBean testObject;

    @Test
    void testCreate(@Mock final MeterEntity meter) {
        Mockito.when(repository.save(any(ElectricPowerFailuresEntity.class))).then(i -> i.getArgument(0));

        final ElectricPowerFailures result = assertDoesNotThrow(
                () -> testObject.create(meter, localDateTime, nrOfPowerFailures, nrOfLongPowerFailures));
        verify(repository).refresh(any(ElectricPowerFailuresEntity.class));
        assertAll( //
                () -> assertNull(result.getId()), //
                () -> assertEquals(meter, result.getMeter()), //
                () -> assertEquals(localDateTime, result.getLocalDateTime()), //
                () -> assertEquals(nrOfPowerFailures, result.getNrOfPowerFailures()), //
                () -> assertEquals(nrOfLongPowerFailures, result.getNrOfLongPowerFailures()) //
        );
    }

    @Test
    void testCreateDataIntegrityViolationException(@Mock final MeterEntity meter) {
        Mockito.when(repository.save(any(ElectricPowerFailuresEntity.class)))
                .thenThrow(new DataIntegrityViolationException(null));

        assertThrows(ConstraintViolationException.class,
                () -> testObject.create(meter, localDateTime, nrOfPowerFailures, nrOfLongPowerFailures));
    }

    @Test
    void testFind(@Mock final MeterEntity meter, @Mock final ElectricPowerFailures electricPowerFailures) {
        final Optional<? extends ElectricPowerFailures> expectedResult = Optional.of(electricPowerFailures);

        Mockito.when(repository.findByMeterAndLocalDateTime(meter, localDateTime)).then(i -> expectedResult);

        assertEquals(expectedResult, testObject.find(meter, localDateTime));
    }

}
