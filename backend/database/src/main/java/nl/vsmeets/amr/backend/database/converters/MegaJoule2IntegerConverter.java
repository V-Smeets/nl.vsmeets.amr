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
import javax.measure.quantity.Energy;
import javax.measure.spi.QuantityFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convert between {@link Energy} mega joule (MJ) and {@link Integer}.
 *
 * @author vincent
 */
@Component
@Configurable
@Converter
public class MegaJoule2IntegerConverter implements AttributeConverter<Quantity<Energy>, Integer> {

    /**
     * A factory to create a quantity of {@link Energy}.
     */
    @Autowired
    private QuantityFactory<Energy> energyQuantityFactory;

    /**
     * The unit for {@link Energy} mega joule (MJ).
     */
    @Autowired
    private Unit<Energy> megaJoule;

    @Override
    public Integer convertToDatabaseColumn(final Quantity<Energy> energy) {
        return energy.to(megaJoule).getValue().intValue();
    }

    @Override
    public Quantity<Energy> convertToEntityAttribute(final Integer value) {
        return energyQuantityFactory.create(value, megaJoule);
    }

}
