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
import nl.vsmeets.amr.backend.database.ElectricPhaseVoltageErrors;
import nl.vsmeets.amr.backend.database.entities.ElectricPhaseVoltageErrorsEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;
import nl.vsmeets.amr.libs.junit.RandomByteGenerator;
import nl.vsmeets.amr.libs.junit.RandomLocalDateTimeGenerator;

/**
 * Unit tests for the class {@link ElectricPhaseVoltageErrorsFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricPhaseVoltageErrorsFactoryBeanTest implements RandomByteGenerator, RandomLocalDateTimeGenerator {

  /**
   * The object under test.
   */
  private ElectricPhaseVoltageErrorsFactoryBean testObject;

  /**
   * Constructor parameters.
   */
  @Mock
  private ElectricPhaseVoltageErrorsRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    testObject = new ElectricPhaseVoltageErrorsFactoryBean(repository);
  }

  @Test
  void testCreate(@Mock final MeterEntity meter) {
    final LocalDateTime localDateTime = randomLocalDateTime();
    final Byte phaseNumber = randomByte();
    final Integer nrOfVoltageSags = randomInt();
    final Integer nrOfVoltageSwells = randomInt();

    when(repository.save(any(ElectricPhaseVoltageErrorsEntity.class))).then(i -> i.getArgument(0));

    final ElectricPhaseVoltageErrors result = assertDoesNotThrow(
        () -> testObject.create(meter, localDateTime, phaseNumber, nrOfVoltageSags, nrOfVoltageSwells));
    verify(repository).refresh(any(ElectricPhaseVoltageErrorsEntity.class));
    // @formatter:off
    assertAll(
        () -> assertNull(result.getId()),
        () -> assertEquals(meter, result.getMeter()),
        () -> assertEquals(localDateTime, result.getLocalDateTime()),
        () -> assertEquals(phaseNumber, result.getPhaseNumber()),
        () -> assertEquals(nrOfVoltageSags, result.getNrOfVoltageSags()),
        () -> assertEquals(nrOfVoltageSwells, result.getNrOfVoltageSwells()));
    // @formatter:on
  }

  @Test
  void testCreateDataIntegrityViolationException(@Mock final MeterEntity meter) {
    final LocalDateTime dateTime = randomLocalDateTime();
    final Byte phaseNumber = randomByte();
    final Integer nrOfVoltageSags = randomInt();
    final Integer nrOfVoltageSwells = randomInt();

    when(repository.save(any(ElectricPhaseVoltageErrorsEntity.class)))
        .thenThrow(new DataIntegrityViolationException(null));

    assertThrows(ConstraintViolationException.class,
        () -> testObject.create(meter, dateTime, phaseNumber, nrOfVoltageSags, nrOfVoltageSwells));
  }

  @Test
  void testFind(@Mock final MeterEntity meter, @Mock final ElectricPhaseVoltageErrors electricPhaseVoltageErrors) {
    final LocalDateTime dateTime = randomLocalDateTime();
    final Byte phaseNumber = randomByte();
    final Optional<? extends ElectricPhaseVoltageErrors> expectedResult = Optional.of(electricPhaseVoltageErrors);

    when(repository.findByMeterAndDateTimeAndPhaseNumber(meter, dateTime, phaseNumber)).then(i -> expectedResult);

    assertEquals(expectedResult, testObject.find(meter, dateTime, phaseNumber));
  }

}
