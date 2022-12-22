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
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.ElectricPotential;
import javax.measure.quantity.Power;

/**
 * Create and find {@link ElectricPhasePowerReading} entities.
 *
 * @author vincent
 */
public interface ElectricPhasePowerReadingFactory {

    /**
     * Create a new {@link ElectricPhasePowerReading}.
     *
     * @param meter                      The meter of these errors.
     * @param localDateTime              The local date and time of this reading.
     * @param phaseNumber                The phase number.
     * @param instantaneousVoltage       The instantaneous voltage.
     * @param instantaneousCurrent       The instantaneous current.
     * @param instantaneousConsumedPower The instantaneous consumed power.
     * @param instantaneousProducedPower The instantaneous produced power.
     * @return The {@link ElectricPhasePowerReading}.
     * @throws ConstraintViolationException A database constraint constraint has
     *                                      been violated.
     */
    ElectricPhasePowerReading create(final Meter meter, final LocalDateTime localDateTime, final Byte phaseNumber,
            final Quantity<ElectricPotential> instantaneousVoltage,
            final Quantity<ElectricCurrent> instantaneousCurrent, final Quantity<Power> instantaneousConsumedPower,
            final Quantity<Power> instantaneousProducedPower) throws ConstraintViolationException;

    /**
     * Find a {@link ElectricPhasePowerReading}.
     *
     * @param meter         The meter of this reading.
     * @param localDateTime The local date and time of this reading.
     * @param phaseNumber   The phase number.
     * @return The {@link ElectricPhasePowerReading}.
     */
    Optional<? extends ElectricPhasePowerReading> find(final Meter meter, final LocalDateTime localDateTime,
            final Byte phaseNumber);

}
