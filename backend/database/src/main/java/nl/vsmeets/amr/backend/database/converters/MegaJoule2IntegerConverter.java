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
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.context.ApplicationContext;

/**
 * Convert between {@link Energy} mega joule (MJ) and {@link Integer}.
 *
 * @author vincent
 */
@Converter
public class MegaJoule2IntegerConverter implements AttributeConverter<Quantity<Energy>, Integer> {

  /**
   * A factory to create a quantity of {@link Energy}.
   */
  private QuantityFactory<Energy> energyQuantityFactory;

  /**
   * The unit for {@link Energy} mega joule (MJ).
   */
  private Unit<Energy> megaJoule;

  /**
   * Create a new instance.
   */
  public MegaJoule2IntegerConverter() {
    super();
  }

  private synchronized void autoWire() {
    if (energyQuantityFactory == null) {
      final ApplicationContext applicationContext = ApplicationContextStore.getApplicationContext();
      energyQuantityFactory = applicationContext.getBean("energyQuantityFactory", QuantityFactory.class);
      megaJoule = applicationContext.getBean("megaJoule", Unit.class);
    }
  }

  @Override
  public Integer convertToDatabaseColumn(final Quantity<Energy> energy) {
    autoWire();
    return energy.to(megaJoule).getValue().intValue();
  }

  @Override
  public Quantity<Energy> convertToEntityAttribute(final Integer value) {
    autoWire();
    return energyQuantityFactory.create(value, megaJoule);
  }

}
