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

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.MeasuredMedium;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.MeterFactory;
import nl.vsmeets.amr.backend.database.P1Telegram;
import nl.vsmeets.amr.backend.database.entities.MeasuredMediumEntity;
import nl.vsmeets.amr.backend.database.entities.MeterEntity;
import nl.vsmeets.amr.backend.database.entities.P1TelegramEntity;

/**
 * Create and find {@link Meter} entities.
 *
 * @author vincent
 */
@Service
public class MeterFactoryBean implements MeterFactory {

    /**
     * The repository for a {@link Meter}.
     */
    @Autowired
    private MeterRepository repository;

    @Override
    public Meter create(final P1Telegram p1Telegram, final MeasuredMedium measuredMedium,
            final String equipmentIdentifier) throws ConstraintViolationException {
        final MeterEntity meter = new MeterEntity((P1TelegramEntity) p1Telegram, (MeasuredMediumEntity) measuredMedium,
                equipmentIdentifier);
        try {
            final MeterEntity entity = repository.save(meter);
            repository.refresh(entity);
            return entity;
        } catch (final DataIntegrityViolationException e) {
            throw new ConstraintViolationException(meter.toString(), e);
        }
    }

    @Override
    public Optional<? extends Meter> find(final P1Telegram p1Telegram, final MeasuredMedium measuredMedium,
            final String equipmentIdentifier) {
        return repository.findByP1TelegramAndMeasuredMediumAndEquipmentIdentifier((P1TelegramEntity) p1Telegram,
                (MeasuredMediumEntity) measuredMedium, equipmentIdentifier);
    }

}
