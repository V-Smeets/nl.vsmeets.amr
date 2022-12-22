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
import javax.measure.quantity.Volume;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.WaterVolumeReading;
import nl.vsmeets.amr.backend.database.WaterVolumeReadingFactory;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;
import nl.vsmeets.amr.backend.database.entities.WaterVolumeReadingEntity;

/**
 * Create and find {@link WaterVolumeReading} entities.
 *
 * @author vincent
 */
@Service
public class WaterVolumeReadingFactoryBean implements WaterVolumeReadingFactory {

    /**
     * The repository for a {@link WaterVolumeReading}.
     */
    @Autowired
    private WaterVolumeReadingRepository repository;

    @Override
    public WaterVolumeReading create(final Meter meter, final LocalDateTime localDateTime,
            final Quantity<Volume> consumedWater) throws ConstraintViolationException {
        final WaterVolumeReadingEntity waterVolumeReadingEntity = new WaterVolumeReadingEntity((MeterEntity) meter,
                localDateTime, consumedWater);
        try {
            final WaterVolumeReadingEntity entity = repository.save(waterVolumeReadingEntity);
            repository.refresh(entity);
            return entity;
        } catch (final DataIntegrityViolationException e) {
            throw new ConstraintViolationException(waterVolumeReadingEntity.toString(), e);
        }
    }

    @Override
    public Optional<? extends WaterVolumeReading> find(final Meter meter, final LocalDateTime localDateTime) {
        return repository.findByMeterAndLocalDateTime((MeterEntity) meter, localDateTime);
    }

}
