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
package nl.vsmeets.amr.backend.database;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.measure.Quantity;
import javax.measure.quantity.Energy;

/**
 * Create and find {@link ElectricEnergyReading} entities.
 *
 * @author vincent
 */
public interface ElectricEnergyReadingFactory {

    /**
     * Create a new {@link ElectricEnergyReading}.
     *
     * @param meter           The meter of this reading.
     * @param localDateTime   The local date and time of this reading.
     * @param tariffIndicator The tariff indicator.
     * @param consumedEnergy  The consumed energy.
     * @param producedEnergy  The produced energy.
     * @return The {@link ElectricEnergyReading}.
     * @throws ConstraintViolationException A database constraint constraint has
     *                                      been violated.
     */
    ElectricEnergyReading create(final Meter meter, final LocalDateTime localDateTime, final Short tariffIndicator,
            final Quantity<Energy> consumedEnergy, final Quantity<Energy> producedEnergy)
            throws ConstraintViolationException;

    /**
     * Find a {@link ElectricEnergyReading}.
     *
     * @param meter           The meter of this reading.
     * @param localDateTime   The local date and time of this reading.
     * @param tariffIndicator The tariff indicator.
     * @return The {@link ElectricEnergyReading}.
     */
    Optional<? extends ElectricEnergyReading> find(final Meter meter, final LocalDateTime localDateTime,
            final Short tariffIndicator);

}
