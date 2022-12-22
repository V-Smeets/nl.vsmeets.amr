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
import javax.measure.quantity.Volume;
import javax.measure.spi.QuantityFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convert between {@link Volume} cubic deci meter (dm3) and {@link Integer}.
 *
 * @author vincent
 */
@Component
@Configurable
@Converter
public class CubicDeciMeter2IntegerConverter implements AttributeConverter<Quantity<Volume>, Integer> {

    /**
     * A factory to create a quantity of {@link Volume}.
     */
    @Autowired
    private QuantityFactory<Volume> volumeQuantityFactory;

    /**
     * The unit for {@link Volume} cubic deci meter (dm3).
     */
    @Autowired
    private Unit<Volume> cubicDeciMeter;

    @Override
    public Integer convertToDatabaseColumn(final Quantity<Volume> volume) {
        return volume.to(cubicDeciMeter).getValue().intValue();
    }

    @Override
    public Quantity<Volume> convertToEntityAttribute(final Integer value) {
        return volumeQuantityFactory.create(value, cubicDeciMeter);
    }

}
