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
import nl.vsmeets.amr.backend.database.ElectricPhaseVoltageErrors;
import nl.vsmeets.amr.backend.database.entities.ElectricPhaseVoltageErrorsEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;

/**
 * Unit tests for the class {@link ElectricPhaseVoltageErrorsFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricPhaseVoltageErrorsFactoryBeanTest {

    /**
     * Values used during tests.
     */
    private static final LocalDateTime localDateTime = LocalDateTime.MIN;
    private static final Byte phaseNumber = Byte.MIN_VALUE;
    private static final Integer nrOfVoltageSags = Integer.MIN_VALUE;
    private static final Integer nrOfVoltageSwells = Integer.MIN_VALUE;

    /**
     * Constructor parameters.
     */
    @Mock
    private ElectricPhaseVoltageErrorsRepository repository;

    /**
     * The object under test.
     */
    @InjectMocks
    private ElectricPhaseVoltageErrorsFactoryBean testObject;

    @Test
    void testCreate(@Mock final MeterEntity meter) {
        Mockito.when(repository.save(any(ElectricPhaseVoltageErrorsEntity.class))).then(i -> i.getArgument(0));

        final ElectricPhaseVoltageErrors result = assertDoesNotThrow(
                () -> testObject.create(meter, localDateTime, phaseNumber, nrOfVoltageSags, nrOfVoltageSwells));
        verify(repository).refresh(any(ElectricPhaseVoltageErrorsEntity.class));
        assertAll( //
                () -> assertNull(result.getId()), //
                () -> assertEquals(meter, result.getMeter()), //
                () -> assertEquals(localDateTime, result.getLocalDateTime()), //
                () -> assertEquals(phaseNumber, result.getPhaseNumber()), //
                () -> assertEquals(nrOfVoltageSags, result.getNrOfVoltageSags()), //
                () -> assertEquals(nrOfVoltageSwells, result.getNrOfVoltageSwells()) //
        );
    }

    @Test
    void testCreateDataIntegrityViolationException(@Mock final MeterEntity meter) {
        Mockito.when(repository.save(any(ElectricPhaseVoltageErrorsEntity.class)))
                .thenThrow(new DataIntegrityViolationException(null));

        assertThrows(ConstraintViolationException.class,
                () -> testObject.create(meter, localDateTime, phaseNumber, nrOfVoltageSags, nrOfVoltageSwells));
    }

    @Test
    void testFind(@Mock final MeterEntity meter, @Mock final ElectricPhaseVoltageErrors electricPhaseVoltageErrors) {
        final Optional<? extends ElectricPhaseVoltageErrors> expectedResult = Optional.of(electricPhaseVoltageErrors);

        Mockito.when(repository.findByMeterAndLocalDateTimeAndPhaseNumber(meter, localDateTime, phaseNumber))
                .then(i -> expectedResult);

        assertEquals(expectedResult, testObject.find(meter, localDateTime, phaseNumber));
    }

}
