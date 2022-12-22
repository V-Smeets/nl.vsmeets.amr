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

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * The electric power failure event.
 *
 * @author vincent
 */
public interface ElectricPowerFailureEvent extends Table {

    /**
     * Get the electric power failures.
     *
     * @return The electric power failures.
     */
    ElectricPowerFailures getElectricPowerFailures();

    /**
     * Get the end of failure time.
     *
     * @return The end of failure time.
     */
    LocalDateTime getEndOfFailureTime();

    /**
     * Get the failure duration.
     *
     * @return The failure duration.
     */
    Duration getFailureDuration();

}
