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
import javax.measure.quantity.Volume;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
   * The object under test.
   */
  private WaterVolumeReadingFactoryBean testObject;

  /**
   * Constructor parameters.
   */
  @Mock
  private WaterVolumeReadingRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    testObject = new WaterVolumeReadingFactoryBean(repository);
  }

  @Test
  void testCreate(@Mock final MeterEntity meter, @Mock final Quantity<Volume> consumedWater) {
    final LocalDateTime localDateTime = randomLocalDateTime();

    when(repository.save(any(WaterVolumeReadingEntity.class))).then(i -> i.getArgument(0));

    final WaterVolumeReading result = assertDoesNotThrow(() -> testObject.create(meter, localDateTime, consumedWater));
    verify(repository).refresh(any(WaterVolumeReadingEntity.class));
    // @formatter:off
    assertAll(
        () -> assertNull(result.getId()),
        () -> assertEquals(meter, result.getMeter()),
        () -> assertEquals(localDateTime, result.getLocalDateTime()),
        () -> assertEquals(consumedWater, result.getConsumedWater()));
    // @formatter:on
  }

  @Test
  void testCreateDataIntegrityViolationException(@Mock final MeterEntity meter,
      @Mock final Quantity<Volume> consumedWater) {
    final LocalDateTime dateTime = randomLocalDateTime();

    when(repository.save(any(WaterVolumeReadingEntity.class))).thenThrow(new DataIntegrityViolationException(null));

    assertThrows(ConstraintViolationException.class, () -> testObject.create(meter, dateTime, consumedWater));
  }

  @Test
  void testFind(@Mock final MeterEntity meter, @Mock final WaterVolumeReading waterVolumeReading) {
    final LocalDateTime dateTime = randomLocalDateTime();
    final Optional<? extends WaterVolumeReading> expectedResult = Optional.of(waterVolumeReading);

    when(repository.findByMeterAndDateTime(meter, dateTime)).then(i -> expectedResult);

    assertEquals(expectedResult, testObject.find(meter, dateTime));
  }

}
