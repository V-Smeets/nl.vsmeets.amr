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

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.vsmeets.amr.backend.database.entities.ElectricPowerFailureEventEntity;
import nl.vsmeets.amr.backend.database.entities.ElectricPowerFailuresEntity;

/**
 * A repository to access the {@link ElectricPowerFailureEventEntity}.
 *
 * @author vincent
 */
public interface ElectricPowerFailureEventRepository extends JpaRepository<ElectricPowerFailureEventEntity, Integer>,
        RefreshRepository<ElectricPowerFailureEventEntity> {

    /**
     * Find a {@link ElectricPowerFailureEventEntity}.
     *
     * @param electricPowerFailures The electric power failures.
     * @param endOfFailureTime      The end of failure time.
     * @return The {@link ElectricPowerFailureEventEntity}.
     */
    Optional<ElectricPowerFailureEventEntity> findByElectricPowerFailuresAndEndOfFailureTime(
            final ElectricPowerFailuresEntity electricPowerFailures, final LocalDateTime endOfFailureTime);

}
