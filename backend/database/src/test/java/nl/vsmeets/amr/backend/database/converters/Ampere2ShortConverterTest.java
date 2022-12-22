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

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.spi.QuantityFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link Ampere2ShortConverter}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class Ampere2ShortConverterTest {

    @Mock
    private QuantityFactory<ElectricCurrent> electricCurrentQuantityFactory;

    @Mock
    private Unit<ElectricCurrent> ampere;

    /**
     * The object under test.
     */
    @InjectMocks
    private Ampere2ShortConverter testObject;

    @Test
    void testConvertToDatabaseColumn(@Mock final Quantity<ElectricCurrent> current) {
        final Short convertToDatabaseColumn = 123;
        Mockito.when(current.to(ampere)).then(i -> current);
        Mockito.when(current.getValue()).then(i -> convertToDatabaseColumn);

        assertEquals(convertToDatabaseColumn, testObject.convertToDatabaseColumn(current));
        assertEquals(convertToDatabaseColumn, testObject.convertToDatabaseColumn(current));
    }

    @Test
    void testConvertToEntityAttribute(@Mock final Quantity<ElectricCurrent> convertToEntityAttribute) {
        final Short value = 123;
        Mockito.when(electricCurrentQuantityFactory.create(value, ampere)).then(i -> convertToEntityAttribute);

        assertEquals(convertToEntityAttribute, testObject.convertToEntityAttribute(value));
    }

}
