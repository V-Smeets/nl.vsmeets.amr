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
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.ElectricPotential;
import javax.measure.quantity.Power;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.ElectricPhasePowerReading;
import nl.vsmeets.amr.backend.database.ElectricPhasePowerReadingFactory;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.entities.ElectricPhasePowerReadingEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;

/**
 * Create and find {@link ElectricPhasePowerReading} entities.
 *
 * @author vincent
 */
@Service
public class ElectricPhasePowerReadingFactoryBean implements ElectricPhasePowerReadingFactory {

    /**
     * The repository for a {@link ElectricPhasePowerReading}.
     */
    @Autowired
    private ElectricPhasePowerReadingRepository repository;

    @Override
    public ElectricPhasePowerReading create(final Meter meter, final LocalDateTime localDateTime,
            final Byte phaseNumber, final Quantity<ElectricPotential> instantaneousVoltage,
            final Quantity<ElectricCurrent> instantaneousCurrent, final Quantity<Power> instantaneousConsumedPower,
            final Quantity<Power> instantaneousProducedPower) throws ConstraintViolationException {
        final ElectricPhasePowerReadingEntity electricPhasePowerReading = new ElectricPhasePowerReadingEntity(
                (MeterEntity) meter, localDateTime, phaseNumber, instantaneousVoltage, instantaneousCurrent,
                instantaneousConsumedPower, instantaneousProducedPower);
        try {
            final ElectricPhasePowerReadingEntity entity = repository.save(electricPhasePowerReading);
            repository.refresh(entity);
            return entity;
        } catch (final DataIntegrityViolationException e) {
            throw new ConstraintViolationException(electricPhasePowerReading.toString(), e);
        }
    }

    @Override
    public Optional<? extends ElectricPhasePowerReading> find(final Meter meter, final LocalDateTime localDateTime,
            final Byte phaseNumber) {
        return repository.findByMeterAndLocalDateTimeAndPhaseNumber((MeterEntity) meter, localDateTime, phaseNumber);
    }

}
