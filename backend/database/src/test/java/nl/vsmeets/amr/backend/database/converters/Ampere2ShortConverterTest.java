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
import javax.measure.quantity.ElectricCurrent;
import javax.measure.spi.QuantityFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

/**
 * Unit tests for the class {@link Ampere2ShortConverter}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class Ampere2ShortConverterTest {

  /**
   * The object under test.
   */
  private Ampere2ShortConverter testObject;

  @Mock
  private QuantityFactory<ElectricCurrent> electricCurrentQuantityFactory;

  @Mock
  private Unit<ElectricCurrent> ampere;

  @BeforeEach
  void setUp(@Mock final ApplicationContext applicationContext) throws Exception {
    testObject = new Ampere2ShortConverter();
    final ApplicationContextStore applicationContextStore = new ApplicationContextStore();
    lenient().when(applicationContext.getBean("electricCurrentQuantityFactory", QuantityFactory.class))
        .then(i -> electricCurrentQuantityFactory);
    lenient().when(applicationContext.getBean("ampere", Unit.class)).then(i -> ampere);
    applicationContextStore.setApplicationContext(applicationContext);
  }

  @Test
  void testConvertToDatabaseColumn(@Mock final Quantity<ElectricCurrent> current) {
    final Short convertToDatabaseColumn = 123;
    when(current.to(ampere)).then(i -> current);
    when(current.getValue()).then(i -> convertToDatabaseColumn);

    assertEquals(convertToDatabaseColumn, testObject.convertToDatabaseColumn(current));
    assertEquals(convertToDatabaseColumn, testObject.convertToDatabaseColumn(current));
  }

  @Test
  void testConvertToEntityAttribute(@Mock final Quantity<ElectricCurrent> convertToEntityAttribute) {
    final Short value = 123;
    when(electricCurrentQuantityFactory.create(value, ampere)).then(i -> convertToEntityAttribute);

    assertEquals(convertToEntityAttribute, testObject.convertToEntityAttribute(value));
  }

}
