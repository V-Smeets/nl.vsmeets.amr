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
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.context.ApplicationContext;

/**
 * Convert between {@link Volume} cubic deci meter (dm3) and {@link Integer}.
 *
 * @author vincent
 */
@Converter
public class CubicDeciMeter2IntegerConverter implements AttributeConverter<Quantity<Volume>, Integer> {

  /**
   * A factory to create a quantity of {@link Volume}.
   */
  private QuantityFactory<Volume> volumeQuantityFactory;

  /**
   * The unit for {@link Volume} cubic deci meter (dm3).
   */
  private Unit<Volume> cubicDeciMeter;

  /**
   * Create a new instance.
   */
  public CubicDeciMeter2IntegerConverter() {
    super();
  }

  @Override
  public Integer convertToDatabaseColumn(final Quantity<Volume> volume) {
    autoWire();
    return volume.to(cubicDeciMeter).getValue().intValue();
  }

  @Override
  public Quantity<Volume> convertToEntityAttribute(final Integer value) {
    autoWire();
    return volumeQuantityFactory.create(value, cubicDeciMeter);
  }

  private synchronized void autoWire() {
    if (volumeQuantityFactory == null) {
      final ApplicationContext applicationContext = ApplicationContextStore.getApplicationContext();
      volumeQuantityFactory = applicationContext.getBean("volumeQuantityFactory", QuantityFactory.class);
      cubicDeciMeter = applicationContext.getBean("cubicDeciMeter", Unit.class);
    }
  }

}
