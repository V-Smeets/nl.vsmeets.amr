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

import java.util.Optional;

/**
 * Create and find {@link Meter} entities.
 *
 * @author vincent
 */
public interface MeterFactory {

    /**
     * Create a new {@link Meter}.
     *
     * @param p1Telegram          The P1 telegram.
     * @param measuredMedium      The measured medium.
     * @param equipmentIdentifier The Equipment identifier.
     * @return The {@link Meter}.
     * @throws ConstraintViolationException A database constraint constraint has
     *                                      been violated.
     */
    Meter create(final P1Telegram p1Telegram, final MeasuredMedium measuredMedium, final String equipmentIdentifier)
            throws ConstraintViolationException;

    /**
     * Find a {@link Meter}.
     *
     * @param p1Telegram          The P1 telegram.
     * @param measuredMedium      The measured medium.
     * @param equipmentIdentifier The Equipment identifier.
     * @return The {@link Meter}.
     */
    Optional<? extends Meter> find(final P1Telegram p1Telegram, final MeasuredMedium measuredMedium,
            final String equipmentIdentifier);

}
