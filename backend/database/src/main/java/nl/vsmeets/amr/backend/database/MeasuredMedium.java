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

import java.util.Set;

/**
 * The measured medium.
 *
 * @author vincent
 */
public interface MeasuredMedium extends Table {

    /**
     * Get the M-Bus id of the measured medium.
     *
     * @return The M-Bus id of the measured medium.
     */
    Byte getMediumId();

    /**
     * Get the meters.
     *
     * @return The meters.
     */
    Set<? extends Meter> getMeters();

    /**
     * Get the name of the measured medium.
     *
     * @return The name of the measured medium.
     */
    String getName();

}
