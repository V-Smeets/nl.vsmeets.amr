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
import javax.measure.quantity.ElectricPotential;
import javax.measure.spi.QuantityFactory;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.context.ApplicationContext;

/**
 * Convert between {@link ElectricPotential} deci volt (dV) and {@link Short}.
 *
 * @author vincent
 */
@Converter
public class DeciVolt2ShortConverter implements AttributeConverter<Quantity<ElectricPotential>, Short> {

  /**
   * A factory to create a quantity of {@link ElectricPotential}.
   */
  private QuantityFactory<ElectricPotential> electricPotentialQuantityFactory;

  /**
   * The unit for {@link ElectricPotential} deci volt (dV).
   */
  private Unit<ElectricPotential> deciVolt;

  /**
   * Create a new instance.
   */
  public DeciVolt2ShortConverter() {
    super();
  }

  @Override
  public Short convertToDatabaseColumn(final Quantity<ElectricPotential> potential) {
    autoWire();
    return potential.to(deciVolt).getValue().shortValue();
  }

  @Override
  public Quantity<ElectricPotential> convertToEntityAttribute(final Short value) {
    autoWire();
    return electricPotentialQuantityFactory.create(value, deciVolt);
  }

  private synchronized void autoWire() {
    if (electricPotentialQuantityFactory == null) {
      final ApplicationContext applicationContext = ApplicationContextStore.getApplicationContext();
      electricPotentialQuantityFactory =
          applicationContext.getBean("electricPotentialQuantityFactory", QuantityFactory.class);
      deciVolt = applicationContext.getBean("deciVolt", Unit.class);
    }
  }

}
