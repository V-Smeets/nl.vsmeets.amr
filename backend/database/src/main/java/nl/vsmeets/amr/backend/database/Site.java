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

import java.time.ZoneId;
import java.util.Set;

/**
 * The site where a meter is located.
 *
 * @author vincent
 */
public interface Site extends Table {

    /**
     * Get the name of the site.
     *
     * @return The name of the site.
     */
    String getName();

    /**
     * Get the P1 telegrams.
     *
     * @return The P1 telegrams.
     */
    Set<? extends P1Telegram> getP1Telegrams();

    /**
     * Get the time zone of the site.
     *
     * @return The time zone of the site.
     */
    ZoneId getTimeZone();

}
