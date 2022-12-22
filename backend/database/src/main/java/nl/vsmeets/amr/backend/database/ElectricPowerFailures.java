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
import java.util.Set;

/**
 * The electric power failures.
 *
 * @author vincent
 */
public interface ElectricPowerFailures extends Table {

    /**
     * Get the electric power failure events.
     *
     * @return The electric power failure events
     */
    Set<? extends ElectricPowerFailureEvent> getElectricPowerFailureEvents();

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
     * Get the number of long power failures.
     *
     * @return The number of long power failures.
     */
    Integer getNrOfLongPowerFailures();

    /**
     * Get the number of power failures.
     *
     * @return The number of power failures.
     */
    Integer getNrOfPowerFailures();

}
