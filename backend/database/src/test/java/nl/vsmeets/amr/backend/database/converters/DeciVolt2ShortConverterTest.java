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
import javax.measure.quantity.ElectricPotential;
import javax.measure.spi.QuantityFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link DeciVolt2ShortConverter}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class DeciVolt2ShortConverterTest {

    @Mock
    private QuantityFactory<ElectricPotential> electricPotentialQuantityFactory;

    @Mock
    private Unit<ElectricPotential> deciVolt;

    /**
     * The object under test.
     */
    @InjectMocks
    private DeciVolt2ShortConverter testObject;

    @Test
    void testConvertToDatabaseColumn(@Mock final Quantity<ElectricPotential> electricPotential) {
        final Short convertToDatabaseColumn = 123;
        Mockito.when(electricPotential.to(deciVolt)).then(i -> electricPotential);
        Mockito.when(electricPotential.getValue()).then(i -> convertToDatabaseColumn);

        assertEquals(convertToDatabaseColumn, testObject.convertToDatabaseColumn(electricPotential));
        assertEquals(convertToDatabaseColumn, testObject.convertToDatabaseColumn(electricPotential));
    }

    @Test
    void testConvertToEntityAttribute(@Mock final Quantity<ElectricPotential> convertToEntityAttribute) {
        final Short value = 123;
        Mockito.when(electricPotentialQuantityFactory.create(value, deciVolt)).then(i -> convertToEntityAttribute);

        assertEquals(convertToEntityAttribute, testObject.convertToEntityAttribute(value));
    }

}
