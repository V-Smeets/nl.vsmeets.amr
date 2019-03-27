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
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.context.ApplicationContext;

/**
 * Convert between {@link Power} watt (W) and {@link Integer}.
 *
 * @author vincent
 */
@Converter
public class Watt2IntegerConverter implements AttributeConverter<Quantity<Power>, Integer> {

  /**
   * A factory to create a quantity of {@link Power}.
   */
  private QuantityFactory<Power> powerQuantityFactory;

  /**
   * The unit for {@link Power} watt (W).
   */
  private Unit<Power> watt;

  /**
   * Create a new instance.
   */
  public Watt2IntegerConverter() {
    super();
  }

  @Override
  public Integer convertToDatabaseColumn(final Quantity<Power> power) {
    autoWire();
    return power.to(watt).getValue().intValue();
  }

  @Override
  public Quantity<Power> convertToEntityAttribute(final Integer value) {
    autoWire();
    return powerQuantityFactory.create(value, watt);
  }

  @SuppressWarnings("unchecked")
  private synchronized void autoWire() {
    if (powerQuantityFactory == null) {
      final ApplicationContext applicationContext = ApplicationContextStore.getApplicationContext();
      powerQuantityFactory = applicationContext.getBean("powerQuantityFactory", QuantityFactory.class);
      watt = applicationContext.getBean("watt", Unit.class);
    }
  }

}
