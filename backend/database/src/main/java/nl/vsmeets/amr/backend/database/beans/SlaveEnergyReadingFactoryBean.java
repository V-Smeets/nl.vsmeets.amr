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

import javax.measure.Quantity;
import javax.measure.quantity.Energy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.SlaveEnergyReading;
import nl.vsmeets.amr.backend.database.SlaveEnergyReadingFactory;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;
import nl.vsmeets.amr.backend.database.entities.SlaveEnergyReadingEntity;

/**
 * Create and find {@link SlaveEnergyReading} entities.
 *
 * @author vincent
 */
@Service
public class SlaveEnergyReadingFactoryBean implements SlaveEnergyReadingFactory {

    /**
     * The repository for a {@link SlaveEnergyReading}.
     */
    @Autowired
    private SlaveEnergyReadingRepository repository;

    @Override
    public SlaveEnergyReading create(final Meter meter, final LocalDateTime localDateTime,
            final Quantity<Energy> consumedEnergy) throws ConstraintViolationException {
        final SlaveEnergyReadingEntity slaveEnergyReading = new SlaveEnergyReadingEntity((MeterEntity) meter,
                localDateTime, consumedEnergy);
        try {
            final SlaveEnergyReadingEntity entity = repository.save(slaveEnergyReading);
            repository.refresh(entity);
            return entity;
        } catch (final DataIntegrityViolationException e) {
            throw new ConstraintViolationException(slaveEnergyReading.toString(), e);
        }
    }

    @Override
    public Optional<? extends SlaveEnergyReading> find(final Meter meter, final LocalDateTime localDateTime) {
        return repository.findByMeterAndLocalDateTime((MeterEntity) meter, localDateTime);
    }

}
