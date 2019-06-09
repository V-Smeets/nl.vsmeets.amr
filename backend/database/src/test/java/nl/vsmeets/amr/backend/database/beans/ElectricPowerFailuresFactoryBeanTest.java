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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.ElectricPowerFailures;
import nl.vsmeets.amr.backend.database.entities.ElectricPowerFailuresEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;
import nl.vsmeets.amr.libs.junit.RandomLocalDateTimeGenerator;

/**
 * Unit tests for the class {@link ElectricPowerFailuresFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricPowerFailuresFactoryBeanTest implements RandomLocalDateTimeGenerator {

  /**
   * The object under test.
   */
  private ElectricPowerFailuresFactoryBean testObject;

  /**
   * Constructor parameters.
   */
  @Mock
  private ElectricPowerFailuresRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    testObject = new ElectricPowerFailuresFactoryBean(repository);
  }

  @Test
  void testCreate(@Mock final MeterEntity meter) {
    final LocalDateTime localDateTime = randomLocalDateTime();
    final Integer nrOfPowerFailures = randomInt();
    final Integer nrOfLongPowerFailures = randomInt();

    when(repository.save(any(ElectricPowerFailuresEntity.class))).then(i -> i.getArgument(0));

    final ElectricPowerFailures result =
        assertDoesNotThrow(() -> testObject.create(meter, localDateTime, nrOfPowerFailures, nrOfLongPowerFailures));
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
    final LocalDateTime localDateTime = randomLocalDateTime();
    final Integer nrOfPowerFailures = randomInt();
    final Integer nrOfLongPowerFailures = randomInt();

    when(repository.save(any(ElectricPowerFailuresEntity.class))).thenThrow(new DataIntegrityViolationException(null));

    assertThrows(ConstraintViolationException.class,
        () -> testObject.create(meter, localDateTime, nrOfPowerFailures, nrOfLongPowerFailures));
  }

  @Test
  void testFind(@Mock final MeterEntity meter, @Mock final ElectricPowerFailures electricPowerFailures) {
    final LocalDateTime localDateTime = randomLocalDateTime();
    final Optional<? extends ElectricPowerFailures> expectedResult = Optional.of(electricPowerFailures);

    when(repository.findByMeterAndLocalDateTime(meter, localDateTime)).then(i -> expectedResult);

    assertEquals(expectedResult, testObject.find(meter, localDateTime));
  }

}
