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

import javax.measure.Quantity;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.ElectricPotential;
import javax.measure.quantity.Power;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.ElectricPhasePowerReading;
import nl.vsmeets.amr.backend.database.entities.ElectricPhasePowerReadingEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;
import nl.vsmeets.amr.libs.junit.RandomByteGenerator;

/**
 * Unit tests for the class {@link ElectricPhasePowerReadingFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricPhasePowerReadingFactoryBeanTest implements RandomByteGenerator {

  /**
   * The object under test.
   */
  private ElectricPhasePowerReadingFactoryBean testObject;

  /**
   * Constructor parameters.
   */
  @Mock
  private ElectricPhasePowerReadingRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    testObject = new ElectricPhasePowerReadingFactoryBean(repository);
  }

  @Test
  void testCreate(@Mock final MeterEntity meter, @Mock final Quantity<ElectricPotential> instantaneousVoltage,
      @Mock final Quantity<ElectricCurrent> instantaneousCurrent,
      @Mock final Quantity<Power> instantaneousConsumedPower, @Mock final Quantity<Power> instantaneousProducedPower) {
    final LocalDateTime localDateTime = randomLocalDateTime();
    final Byte phaseNumber = randomByte();

    when(repository.save(any(ElectricPhasePowerReadingEntity.class))).then(i -> i.getArgument(0));

    final ElectricPhasePowerReading result =
        assertDoesNotThrow(() -> testObject.create(meter, localDateTime, phaseNumber, instantaneousVoltage,
            instantaneousCurrent, instantaneousConsumedPower, instantaneousProducedPower));
    verify(repository).refresh(any(ElectricPhasePowerReadingEntity.class));
    // @formatter:off
    assertAll(
        () -> assertNull(result.getId()),
        () -> assertEquals(meter, result.getMeter()),
        () -> assertEquals(localDateTime, result.getLocalDateTime()),
        () -> assertEquals(phaseNumber, result.getPhaseNumber()),
        () -> assertEquals(instantaneousVoltage, result.getInstantaneousVoltage()),
        () -> assertEquals(instantaneousCurrent, result.getInstantaneousCurrent()),
        () -> assertEquals(instantaneousConsumedPower, result.getInstantaneousConsumedPower()),
        () -> assertEquals(instantaneousProducedPower, result.getInstantaneousProducedPower()));
    // @formatter:on
  }

  @Test
  void testCreateDataIntegrityViolationException(@Mock final MeterEntity meter,
      @Mock final Quantity<ElectricPotential> instantaneousVoltage,
      @Mock final Quantity<ElectricCurrent> instantaneousCurrent,
      @Mock final Quantity<Power> instantaneousConsumedPower, @Mock final Quantity<Power> instantaneousProducedPower) {
    final LocalDateTime dateTime = randomLocalDateTime();
    final Byte phaseNumber = randomByte();

    when(repository.save(any(ElectricPhasePowerReadingEntity.class)))
        .thenThrow(new DataIntegrityViolationException(null));

    assertThrows(ConstraintViolationException.class, () -> testObject.create(meter, dateTime, phaseNumber,
        instantaneousVoltage, instantaneousCurrent, instantaneousConsumedPower, instantaneousProducedPower));
  }

  @Test
  void testFind(@Mock final MeterEntity meter, @Mock final ElectricPhasePowerReading electricPhasePowerReading) {
    final LocalDateTime dateTime = randomLocalDateTime();
    final Byte phaseNumber = randomByte();
    final Optional<? extends ElectricPhasePowerReading> expectedResult = Optional.of(electricPhasePowerReading);

    when(repository.findByMeterAndDateTimeAndPhaseNumber(meter, dateTime, phaseNumber)).then(i -> expectedResult);

    assertEquals(expectedResult, testObject.find(meter, dateTime, phaseNumber));
  }

}
