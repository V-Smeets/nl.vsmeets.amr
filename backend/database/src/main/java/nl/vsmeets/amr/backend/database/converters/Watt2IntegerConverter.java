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
import javax.measure.quantity.Power;
import javax.measure.spi.QuantityFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convert between {@link Power} watt (W) and {@link Integer}.
 *
 * @author vincent
 */
@Component
@Configurable
@Converter
public class Watt2IntegerConverter implements AttributeConverter<Quantity<Power>, Integer> {

    /**
     * A factory to create a quantity of {@link Power}.
     */
    @Autowired
    private QuantityFactory<Power> powerQuantityFactory;

    /**
     * The unit for {@link Power} watt (W).
     */
    @Autowired
    private Unit<Power> watt;

    @Override
    public Integer convertToDatabaseColumn(final Quantity<Power> power) {
        return power.to(watt).getValue().intValue();
    }

    @Override
    public Quantity<Power> convertToEntityAttribute(final Integer value) {
        return powerQuantityFactory.create(value, watt);
    }

}
