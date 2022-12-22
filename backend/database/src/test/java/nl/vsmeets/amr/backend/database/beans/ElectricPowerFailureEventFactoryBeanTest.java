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

import java.time.Duration;
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
import nl.vsmeets.amr.backend.database.ElectricPowerFailureEvent;
import nl.vsmeets.amr.backend.database.entities.ElectricPowerFailureEventEntity;
import nl.vsmeets.amr.backend.database.entities.ElectricPowerFailuresEntity;

/**
 * Unit tests for the class {@link ElectricPowerFailureEventFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricPowerFailureEventFactoryBeanTest {

    /**
     * Values used during tests.
     */
    private static final LocalDateTime endOfFailureTime = LocalDateTime.MIN;
    private static final Duration failureDuration = Duration.ZERO;

    /**
     * Constructor parameters.
     */
    @Mock
    private ElectricPowerFailureEventRepository repository;

    /**
     * The object under test.
     */
    @InjectMocks
    private ElectricPowerFailureEventFactoryBean testObject;

    @Test
    void testCreate(@Mock final ElectricPowerFailuresEntity electricPowerFailures) {
        Mockito.when(repository.save(any(ElectricPowerFailureEventEntity.class))).then(i -> i.getArgument(0));

        final ElectricPowerFailureEvent result = assertDoesNotThrow(
                () -> testObject.create(electricPowerFailures, endOfFailureTime, failureDuration));
        verify(repository).refresh(any(ElectricPowerFailureEventEntity.class));
        assertAll( //
                () -> assertNull(result.getId()), //
                () -> assertEquals(electricPowerFailures, result.getElectricPowerFailures()), //
                () -> assertEquals(endOfFailureTime, result.getEndOfFailureTime()), //
                () -> assertEquals(failureDuration, result.getFailureDuration()) //
        );
    }

    @Test
    void testCreateDataIntegrityViolationException(@Mock final ElectricPowerFailuresEntity electricPowerFailures) {
        Mockito.when(repository.save(any(ElectricPowerFailureEventEntity.class)))
                .thenThrow(new DataIntegrityViolationException(null));

        assertThrows(ConstraintViolationException.class,
                () -> testObject.create(electricPowerFailures, endOfFailureTime, failureDuration));
    }

    @Test
    void testFind(@Mock final ElectricPowerFailuresEntity electricPowerFailures,
            @Mock final ElectricPowerFailureEvent electricPowerFailureEvent) {
        final Optional<? extends ElectricPowerFailureEvent> expectedResult = Optional.of(electricPowerFailureEvent);

        Mockito.when(repository.findByElectricPowerFailuresAndEndOfFailureTime(electricPowerFailures, endOfFailureTime))
                .then(i -> expectedResult);

        assertEquals(expectedResult, testObject.find(electricPowerFailures, endOfFailureTime));
    }

}
