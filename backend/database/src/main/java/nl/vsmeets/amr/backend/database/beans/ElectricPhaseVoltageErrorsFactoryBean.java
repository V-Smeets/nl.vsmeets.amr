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
import nl.vsmeets.amr.backend.database.ElectricPhaseVoltageErrors;
import nl.vsmeets.amr.backend.database.ElectricPhaseVoltageErrorsFactory;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.entities.ElectricPhaseVoltageErrorsEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;

/**
 * Create and find {@link ElectricPhaseVoltageErrors} entities.
 *
 * @author vincent
 */
@Service
public class ElectricPhaseVoltageErrorsFactoryBean implements ElectricPhaseVoltageErrorsFactory {

    /**
     * The repository for a {@link ElectricPhaseVoltageErrors}.
     */
    @Autowired
    private ElectricPhaseVoltageErrorsRepository repository;

    @Override
    public ElectricPhaseVoltageErrors create(final Meter meter, final LocalDateTime localDateTime,
            final Byte phaseNumber, final Integer nrOfVoltageSags, final Integer nrOfVoltageSwells)
            throws ConstraintViolationException {
        final ElectricPhaseVoltageErrorsEntity electricPhaseVoltageErrors = new ElectricPhaseVoltageErrorsEntity(
                (MeterEntity) meter, localDateTime, phaseNumber, nrOfVoltageSags, nrOfVoltageSwells);
        try {
            final ElectricPhaseVoltageErrorsEntity entity = repository.save(electricPhaseVoltageErrors);
            repository.refresh(entity);
            return entity;
        } catch (final DataIntegrityViolationException e) {
            throw new ConstraintViolationException(electricPhaseVoltageErrors.toString(), e);
        }
    }

    @Override
    public Optional<? extends ElectricPhaseVoltageErrors> find(final Meter meter, final LocalDateTime localDateTime,
            final Byte phaseNumber) {
        return repository.findByMeterAndLocalDateTimeAndPhaseNumber((MeterEntity) meter, localDateTime, phaseNumber);
    }

}
