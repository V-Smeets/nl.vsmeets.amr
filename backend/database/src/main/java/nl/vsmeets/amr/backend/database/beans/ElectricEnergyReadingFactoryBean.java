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
import nl.vsmeets.amr.backend.database.ElectricEnergyReading;
import nl.vsmeets.amr.backend.database.ElectricEnergyReadingFactory;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.entities.ElectricEnergyReadingEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;

/**
 * Create and find {@link ElectricEnergyReading} entities.
 *
 * @author vincent
 */
@Service
public class ElectricEnergyReadingFactoryBean implements ElectricEnergyReadingFactory {

    /**
     * The repository for a {@link ElectricEnergyReading}.
     */
    @Autowired
    private ElectricEnergyReadingRepository repository;

    @Override
    public ElectricEnergyReading create(final Meter meter, final LocalDateTime localDateTime,
            final Short tariffIndicator, final Quantity<Energy> consumedEnergy, final Quantity<Energy> producedEnergy)
            throws ConstraintViolationException {
        final ElectricEnergyReadingEntity electricEnergyReading = new ElectricEnergyReadingEntity((MeterEntity) meter,
                localDateTime, tariffIndicator, consumedEnergy, producedEnergy);
        try {
            final ElectricEnergyReadingEntity entity = repository.save(electricEnergyReading);
            repository.refresh(entity);
            return entity;
        } catch (final DataIntegrityViolationException e) {
            throw new ConstraintViolationException(electricEnergyReading.toString(), e);
        }
    }

    @Override
    public Optional<? extends ElectricEnergyReading> find(final Meter meter, final LocalDateTime localDateTime,
            final Short tariffIndicator) {
        return repository.findByMeterAndLocalDateTimeAndTariffIndicator((MeterEntity) meter, localDateTime,
                tariffIndicator);
    }

}
