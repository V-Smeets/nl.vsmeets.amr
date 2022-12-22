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
import nl.vsmeets.amr.backend.database.ElectricMessage;
import nl.vsmeets.amr.backend.database.entities.ElectricMessageEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;

/**
 * Unit tests for the class {@link ElectricMessageFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricMessageFactoryBeanTest {

    /**
     * Values used during tests.
     */
    private static final LocalDateTime localDateTime = LocalDateTime.MIN;
    private static final String textMessage = "Text Message";

    /**
     * Constructor parameters.
     */
    @Mock
    private ElectricMessageRepository repository;

    /**
     * The object under test.
     */
    @InjectMocks
    private ElectricMessageFactoryBean testObject;

    @Test
    void testCreate(@Mock final MeterEntity meter, @Mock final Quantity<Energy> consumedEnergy,
            @Mock final Quantity<Energy> producedEnergy) {
        Mockito.when(repository.save(any(ElectricMessageEntity.class))).then(i -> i.getArgument(0));

        final ElectricMessage result = assertDoesNotThrow(() -> testObject.create(meter, localDateTime, textMessage));
        verify(repository).refresh(any(ElectricMessageEntity.class));
        assertAll( //
                () -> assertNull(result.getId()), //
                () -> assertEquals(meter, result.getMeter()), //
                () -> assertEquals(localDateTime, result.getLocalDateTime()), //
                () -> assertEquals(textMessage, result.getTextMessage()) //
        );
    }

    @Test
    void testCreateDataIntegrityViolationException(@Mock final MeterEntity meter,
            @Mock final Quantity<Energy> consumedEnergy, @Mock final Quantity<Energy> producedEnergy) {
        Mockito.when(repository.save(any(ElectricMessageEntity.class)))
                .thenThrow(new DataIntegrityViolationException(null));

        assertThrows(ConstraintViolationException.class, () -> testObject.create(meter, localDateTime, textMessage));
    }

    @Test
    void testFind(@Mock final MeterEntity meter, @Mock final ElectricMessage electricMessage) {
        final Optional<? extends ElectricMessage> expectedResult = Optional.of(electricMessage);
        Mockito.when(repository.findByMeterAndLocalDateTime(meter, localDateTime)).then(i -> expectedResult);

        assertEquals(expectedResult, testObject.find(meter, localDateTime));
    }

}
