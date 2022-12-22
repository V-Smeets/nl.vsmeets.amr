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

import javax.measure.Quantity;
import javax.measure.quantity.Power;

/**
 * The electric power reading.
 *
 * @author vincent
 */
public interface ElectricPowerReading extends Table {

    /**
     * Get the consumed power.
     *
     * @return The consumed power.
     */
    Quantity<Power> getConsumedPower();

    /**
     * Get the local date and time of this reading.
     *
     * @return The local date and time of this reading.
     */
    LocalDateTime getLocalDateTime();

    /**
     * Get the meter.
     *
     * @return The meter.
     */
    Meter getMeter();

    /**
     * Get the produced power.
     *
     * @return The produced power.
     */
    Quantity<Power> getProducedPower();

}
