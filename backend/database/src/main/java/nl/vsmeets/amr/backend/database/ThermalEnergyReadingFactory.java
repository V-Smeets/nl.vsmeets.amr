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
 * Create and find {@link ThermalEnergyReading} entities.
 *
 * @author vincent
 */
public interface ThermalEnergyReadingFactory {

    /**
     * Create a new {@link ThermalEnergyReading}.
     *
     * @param meter          The meter of this reading.
     * @param localDateTime  The local date and time of this reading.
     * @param consumedEnergy The consumed thermal energy.
     * @return The {@link ThermalEnergyReading}.
     * @throws ConstraintViolationException A database constraint constraint has
     *                                      been violated.
     */
    ThermalEnergyReading create(final Meter meter, final LocalDateTime localDateTime,
            final Quantity<Energy> consumedEnergy) throws ConstraintViolationException;

    /**
     * Find a {@link ThermalEnergyReading}.
     *
     * @param meter         The meter of this reading.
     * @param localDateTime The local date and time of this reading.
     * @return The {@link ThermalEnergyReading}.
     */
    Optional<? extends ThermalEnergyReading> find(final Meter meter, final LocalDateTime localDateTime);

}
