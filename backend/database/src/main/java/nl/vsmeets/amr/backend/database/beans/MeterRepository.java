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

import nl.vsmeets.amr.backend.database.entities.MeasuredMediumEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;
import nl.vsmeets.amr.backend.database.entities.P1TelegramEntity;

/**
 * A repository to access the {@link MeterEntity}.
 *
 * @author vincent
 */
public interface MeterRepository extends JpaRepository<MeterEntity, Integer>, RefreshRepository<MeterEntity> {

    /**
     * Find a {@link MeterEntity}.
     *
     * @param p1Telegram          The P1 telegram.
     * @param measuredMedium      The measured medium.
     * @param equipmentIdentifier The Equipment identifier.
     * @return The {@link MeterEntity}.
     */
    Optional<MeterEntity> findByP1TelegramAndMeasuredMediumAndEquipmentIdentifier(final P1TelegramEntity p1Telegram,
            final MeasuredMediumEntity measuredMedium, final String equipmentIdentifier);

}
