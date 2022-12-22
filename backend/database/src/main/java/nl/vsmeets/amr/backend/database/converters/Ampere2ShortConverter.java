/**
 * Copyright (C) 2018 Vincent Smeets
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

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.spi.QuantityFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convert between {@link ElectricCurrent} ampere (A) and {@link Short}.
 *
 * @author vincent
 */
@Component
@Configurable
@Converter
public class Ampere2ShortConverter implements AttributeConverter<Quantity<ElectricCurrent>, Short> {

    /**
     * A factory to create a quantity of {@link ElectricCurrent}.
     */
    @Autowired
    private QuantityFactory<ElectricCurrent> electricCurrentQuantityFactory;

    /**
     * The unit for {@link ElectricCurrent} ampere (A).
     */
    @Autowired
    private Unit<ElectricCurrent> ampere;

    @Override
    public Short convertToDatabaseColumn(final Quantity<ElectricCurrent> current) {
        return current.to(ampere).getValue().shortValue();
    }

    @Override
    public Quantity<ElectricCurrent> convertToEntityAttribute(final Short value) {
        return electricCurrentQuantityFactory.create(value, ampere);
    }

}
