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
import nl.vsmeets.amr.backend.database.ThermalEnergyReading;
import nl.vsmeets.amr.backend.database.ThermalEnergyReadingFactory;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;
import nl.vsmeets.amr.backend.database.entities.ThermalEnergyReadingEntity;

/**
 * Create and find {@link ThermalEnergyReading} entities.
 *
 * @author vincent
 */
@Service
public class ThermalEnergyReadingFactoryBean implements ThermalEnergyReadingFactory {

    /**
     * The repository for a {@link ThermalEnergyReading}.
     */
    @Autowired
    private ThermalEnergyReadingRepository repository;

    @Override
    public ThermalEnergyReading create(final Meter meter, final LocalDateTime localDateTime,
            final Quantity<Energy> consumedEnergy) throws ConstraintViolationException {
        final ThermalEnergyReadingEntity thermalEnergyReading = new ThermalEnergyReadingEntity((MeterEntity) meter,
                localDateTime, consumedEnergy);
        try {
            final ThermalEnergyReadingEntity entity = repository.save(thermalEnergyReading);
            repository.refresh(entity);
            return entity;
        } catch (final DataIntegrityViolationException e) {
            throw new ConstraintViolationException(thermalEnergyReading.toString(), e);
        }
    }

    @Override
    public Optional<? extends ThermalEnergyReading> find(final Meter meter, final LocalDateTime localDateTime) {
        return repository.findByMeterAndLocalDateTime((MeterEntity) meter, localDateTime);
    }

}
