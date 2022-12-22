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

/**
 * Create and find {@link ElectricMessage} entities.
 *
 * @author vincent
 */
public interface ElectricMessageFactory {

    /**
     * Create a new {@link ElectricMessage}.
     *
     * @param meter         The meter of these failures.
     * @param localDateTime The local date and time of this message.
     * @param textMessage   The text message.
     * @return The {@link ElectricMessage}.
     * @throws ConstraintViolationException A database constraint constraint has
     *                                      been violated.
     */
    ElectricMessage create(final Meter meter, final LocalDateTime localDateTime, final String textMessage)
            throws ConstraintViolationException;

    /**
     * Find a {@link ElectricMessage}.
     *
     * @param meter         The meter of this reading.
     * @param localDateTime The local date and time of this message.
     * @return The {@link ElectricMessage}.
     */
    Optional<? extends ElectricMessage> find(final Meter meter, final LocalDateTime localDateTime);

}
