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
import javax.measure.quantity.Power;
import javax.measure.spi.QuantityFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link Watt2IntegerConverter}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class Watt2IntegerConverterTest {

    @Mock
    private QuantityFactory<Power> powerQuantityFactory;

    @Mock
    private Unit<Power> watt;

    /**
     * The object under test.
     */
    @InjectMocks
    private Watt2IntegerConverter testObject;

    @Test
    void testConvertToDatabaseColumn(@Mock final Quantity<Power> power) {
        final Integer convertToDatabaseColumn = 123;
        Mockito.when(power.to(watt)).then(i -> power);
        Mockito.when(power.getValue()).then(i -> convertToDatabaseColumn);

        assertEquals(convertToDatabaseColumn, testObject.convertToDatabaseColumn(power));
        assertEquals(convertToDatabaseColumn, testObject.convertToDatabaseColumn(power));
    }

    @Test
    void testConvertToEntityAttribute(@Mock final Quantity<Power> convertToEntityAttribute) {
        final Integer value = 123;
        Mockito.when(powerQuantityFactory.create(value, watt)).then(i -> convertToEntityAttribute);

        assertEquals(convertToEntityAttribute, testObject.convertToEntityAttribute(value));
    }

}
