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
package nl.vsmeets.amr.backend.database.beans;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.vsmeets.amr.backend.database.entities.P1TelegramEntity;
import nl.vsmeets.amr.backend.database.entities.SiteEntity;

/**
 * A repository to access the {@link P1TelegramEntity}.
 *
 * @author vincent
 */
public interface P1TelegramRepository
        extends JpaRepository<P1TelegramEntity, Integer>, RefreshRepository<P1TelegramEntity> {

    /**
     * Find a {@link P1TelegramEntity}.
     *
     * @param site               The site where this P1 telegram is generated.
     * @param headerInformation  The header information.
     * @param versionInformation The version information.
     * @return The {@link P1TelegramEntity}.
     */
    Optional<P1TelegramEntity> findBySiteAndHeaderInformationAndVersionInformation(final SiteEntity site,
            final String headerInformation, final Byte versionInformation);

}
