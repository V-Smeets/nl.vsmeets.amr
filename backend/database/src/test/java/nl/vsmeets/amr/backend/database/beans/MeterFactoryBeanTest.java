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

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.entities.MeasuredMediumEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;
import nl.vsmeets.amr.backend.database.entities.P1TelegramEntity;
import nl.vsmeets.amr.libs.junit.RandomStringGenerator;

/**
 * Unit tests for the class {@link MeterFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class MeterFactoryBeanTest implements RandomStringGenerator {

  /**
   * The object under test.
   */
  private MeterFactoryBean testObject;

  /**
   * Constructor parameters.
   */
  @Mock
  private MeterRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    testObject = new MeterFactoryBean(repository);
  }

  @Test
  void testCreate(@Mock final P1TelegramEntity p1Telegram, @Mock final MeasuredMediumEntity measuredMedium) {
    final String equipmentIdentifier = randomString();

    when(repository.save(any(MeterEntity.class))).then(i -> i.getArgument(0));

    final Meter result = assertDoesNotThrow(() -> testObject.create(p1Telegram, measuredMedium, equipmentIdentifier));
    verify(repository).refresh(any(MeterEntity.class));
    assertAll( //
        () -> assertNull(result.getId()), //
        () -> assertEquals(p1Telegram, result.getP1Telegram()), //
        () -> assertEquals(measuredMedium, result.getMeasuredMedium()), //
        () -> assertEquals(equipmentIdentifier, result.getEquipmentIdentifier()) //
    );
  }

  @Test
  void testCreateDataIntegrityViolationException(@Mock final P1TelegramEntity p1Telegram,
      @Mock final MeasuredMediumEntity measuredMedium) {
    final String equipmentIdentifier = randomString();

    when(repository.save(any(MeterEntity.class))).thenThrow(new DataIntegrityViolationException(null));

    assertThrows(ConstraintViolationException.class,
        () -> testObject.create(p1Telegram, measuredMedium, equipmentIdentifier));
  }

  @Test
  void testFind(@Mock final P1TelegramEntity p1Telegram, @Mock final MeasuredMediumEntity measuredMedium,
      @Mock final Meter meter) {
    final String equipmentIdentifier = randomString();
    final Optional<? extends Meter> expectedResult = Optional.of(meter);

    when(repository.findByP1TelegramAndMeasuredMediumAndEquipmentIdentifier(p1Telegram, measuredMedium,
        equipmentIdentifier)).then(i -> expectedResult);

    assertEquals(expectedResult, testObject.find(p1Telegram, measuredMedium, equipmentIdentifier));
  }

}
