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
import java.util.Optional;

/**
 * Create and find {@link ElectricPowerFailureEvent} entities.
 *
 * @author vincent
 */
public interface ElectricPowerFailureEventFactory {

    /**
     * Create a new {@link ElectricPowerFailureEvent}.
     *
     * @param electricPowerFailures The electric power failures.
     * @param endOfFailureTime      The end of failure time.
     * @param failureDuration       The failure duration.
     * @return The {@link ElectricPowerFailureEvent}.
     * @throws ConstraintViolationException A database constraint constraint has
     *                                      been violated.
     */
    ElectricPowerFailureEvent create(final ElectricPowerFailures electricPowerFailures,
            final LocalDateTime endOfFailureTime, final Duration failureDuration) throws ConstraintViolationException;

    /**
     * Find a {@link ElectricPowerFailureEvent}.
     *
     * @param electricPowerFailures The electric power failures.
     * @param endOfFailureTime      The end of failure time.
     * @return The {@link ElectricPowerFailureEvent}.
     */
    Optional<? extends ElectricPowerFailureEvent> find(final ElectricPowerFailures electricPowerFailures,
            final LocalDateTime endOfFailureTime);

}
