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

import nl.vsmeets.amr.backend.database.entities.ElectricPhasePowerReadingEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;

/**
 * A repository to access the {@link ElectricPhasePowerReadingEntity}.
 *
 * @author vincent
 */
public interface ElectricPhasePowerReadingRepository extends JpaRepository<ElectricPhasePowerReadingEntity, Integer>,
        RefreshRepository<ElectricPhasePowerReadingEntity> {

    /**
     * Find a {@link ElectricPhasePowerReadingEntity}.
     *
     * @param meter         The meter of this reading.
     * @param localDateTime The local date and time of this reading.
     * @param phaseNumber   The phase number.
     * @return The {@link ElectricPhasePowerReadingEntity}.
     */
    Optional<ElectricPhasePowerReadingEntity> findByMeterAndLocalDateTimeAndPhaseNumber(final MeterEntity meter,
            final LocalDateTime localDateTime, final Byte phaseNumber);

}
