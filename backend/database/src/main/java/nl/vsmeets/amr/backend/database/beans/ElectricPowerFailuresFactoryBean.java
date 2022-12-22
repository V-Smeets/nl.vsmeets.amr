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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.ElectricPowerFailures;
import nl.vsmeets.amr.backend.database.ElectricPowerFailuresFactory;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.entities.ElectricPowerFailuresEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;

/**
 * Create and find {@link ElectricPowerFailures} entities.
 *
 * @author vincent
 */
@Service
public class ElectricPowerFailuresFactoryBean implements ElectricPowerFailuresFactory {

    /**
     * The repository for a {@link ElectricPowerFailures}.
     */
    @Autowired
    private ElectricPowerFailuresRepository repository;

    @Override
    public ElectricPowerFailures create(final Meter meter, final LocalDateTime localDateTime,
            final Integer nrOfPowerFailures, final Integer nrOfLongPowerFailures) throws ConstraintViolationException {
        final ElectricPowerFailuresEntity electricPowerFailures = new ElectricPowerFailuresEntity((MeterEntity) meter,
                localDateTime, nrOfPowerFailures, nrOfLongPowerFailures);
        try {
            final ElectricPowerFailuresEntity entity = repository.save(electricPowerFailures);
            repository.refresh(entity);
            return entity;
        } catch (final DataIntegrityViolationException e) {
            throw new ConstraintViolationException(electricPowerFailures.toString(), e);
        }
    }

    @Override
    public Optional<? extends ElectricPowerFailures> find(final Meter meter, final LocalDateTime localDateTime) {
        return repository.findByMeterAndLocalDateTime((MeterEntity) meter, localDateTime);
    }

}
