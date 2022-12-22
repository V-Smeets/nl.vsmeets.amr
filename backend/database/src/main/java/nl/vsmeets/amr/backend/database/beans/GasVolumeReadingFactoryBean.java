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
import nl.vsmeets.amr.backend.database.GasVolumeReading;
import nl.vsmeets.amr.backend.database.GasVolumeReadingFactory;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.entities.GasVolumeReadingEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;

/**
 * Create and find {@link GasVolumeReading} entities.
 *
 * @author vincent
 */
@Service
public class GasVolumeReadingFactoryBean implements GasVolumeReadingFactory {

    /**
     * The repository for a {@link GasVolumeReading}.
     */
    @Autowired
    private GasVolumeReadingRepository repository;

    @Override
    public GasVolumeReading create(final Meter meter, final LocalDateTime localDateTime,
            final Quantity<Volume> consumedGas) throws ConstraintViolationException {
        final GasVolumeReadingEntity gasVolumeReading = new GasVolumeReadingEntity((MeterEntity) meter, localDateTime,
                consumedGas);
        try {
            final GasVolumeReadingEntity entity = repository.save(gasVolumeReading);
            repository.refresh(entity);
            return entity;
        } catch (final DataIntegrityViolationException e) {
            throw new ConstraintViolationException(gasVolumeReading.toString(), e);
        }
    }

    @Override
    public Optional<? extends GasVolumeReading> find(final Meter meter, final LocalDateTime localDateTime) {
        return repository.findByMeterAndLocalDateTime((MeterEntity) meter, localDateTime);
    }

}
