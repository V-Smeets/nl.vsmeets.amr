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
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.ElectricPotential;
import javax.measure.spi.QuantityFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

/**
 * Unit tests for the class {@link DeciVolt2ShortConverter}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class DeciVolt2ShortConverterTest {

  /**
   * The object under test.
   */
  private DeciVolt2ShortConverter testObject;

  @Mock
  private QuantityFactory<ElectricPotential> electricPotentialQuantityFactory;

  @Mock
  private Unit<ElectricPotential> deciVolt;

  @BeforeEach
  void setUp(@Mock final ApplicationContext applicationContext) throws Exception {
    testObject = new DeciVolt2ShortConverter();
    final ApplicationContextStore applicationContextStore = new ApplicationContextStore();
    lenient().when(applicationContext.getBean("electricPotentialQuantityFactory", QuantityFactory.class))
        .then(i -> electricPotentialQuantityFactory);
    lenient().when(applicationContext.getBean("deciVolt", Unit.class)).then(i -> deciVolt);
    applicationContextStore.setApplicationContext(applicationContext);
  }

  @Test
  void testConvertToDatabaseColumn(@Mock final Quantity<ElectricPotential> electricPotential) {
    final Short convertToDatabaseColumn = 123;
    when(electricPotential.to(deciVolt)).then(i -> electricPotential);
    when(electricPotential.getValue()).then(i -> convertToDatabaseColumn);

    assertEquals(convertToDatabaseColumn, testObject.convertToDatabaseColumn(electricPotential));
    assertEquals(convertToDatabaseColumn, testObject.convertToDatabaseColumn(electricPotential));
  }

  @Test
  void testConvertToEntityAttribute(@Mock final Quantity<ElectricPotential> convertToEntityAttribute) {
    final Short value = 123;
    when(electricPotentialQuantityFactory.create(value, deciVolt)).then(i -> convertToEntityAttribute);

    assertEquals(convertToEntityAttribute, testObject.convertToEntityAttribute(value));
  }

}
