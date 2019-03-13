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
package nl.vsmeets.amr.backend.database.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Energy;
import javax.measure.spi.QuantityFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

/**
 * Unit tests for the class {@link WattHour2IntegerConverter}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class Watt2IntegerConverter2Test {

  /**
   * The object under test.
   */
  private WattHour2IntegerConverter testObject;

  @Mock
  private QuantityFactory<Energy> energyQuantityFactory;

  @Mock
  private Unit<Energy> wattHour;

  @BeforeEach
  void setUp(@Mock final ApplicationContext applicationContext) throws Exception {
    testObject = new WattHour2IntegerConverter();
    final ApplicationContextStore applicationContextStore = new ApplicationContextStore();
    lenient().when(applicationContext.getBean("energyQuantityFactory", QuantityFactory.class))
        .then(i -> energyQuantityFactory);
    lenient().when(applicationContext.getBean("wattHour", Unit.class)).then(i -> wattHour);
    applicationContextStore.setApplicationContext(applicationContext);
  }

  @Test
  void testConvertToDatabaseColumn(@Mock final Quantity<Energy> energy) {
    final Integer convertToDatabaseColumn = 123;
    when(energy.to(wattHour)).then(i -> energy);
    when(energy.getValue()).then(i -> convertToDatabaseColumn);

    assertEquals(convertToDatabaseColumn, testObject.convertToDatabaseColumn(energy));
  }

  @Test
  void testConvertToEntityAttribute(@Mock final Quantity<Energy> convertToEntityAttribute) {
    final Integer value = 123;
    when(energyQuantityFactory.create(value, wattHour)).then(i -> convertToEntityAttribute);

    assertEquals(convertToEntityAttribute, testObject.convertToEntityAttribute(value));
  }

}
