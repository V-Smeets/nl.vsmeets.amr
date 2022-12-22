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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.ElectricPowerFailureEvent;
import nl.vsmeets.amr.backend.database.ElectricPowerFailureEventFactory;
import nl.vsmeets.amr.backend.database.ElectricPowerFailures;
import nl.vsmeets.amr.backend.database.entities.ElectricPowerFailureEventEntity;
import nl.vsmeets.amr.backend.database.entities.ElectricPowerFailuresEntity;

/**
 * Create and find {@link ElectricPowerFailureEvent} entities.
 *
 * @author vincent
 */
@Service
public class ElectricPowerFailureEventFactoryBean implements ElectricPowerFailureEventFactory {

    /**
     * The repository for a {@link ElectricPowerFailureEvent}.
     */
    @Autowired
    private ElectricPowerFailureEventRepository repository;

    @Override
    public ElectricPowerFailureEvent create(final ElectricPowerFailures electricPowerFailures,
            final LocalDateTime endOfFailureTime, final Duration failureDuration) throws ConstraintViolationException {
        final ElectricPowerFailureEventEntity electricPowerFailureEvent = new ElectricPowerFailureEventEntity(
                (ElectricPowerFailuresEntity) electricPowerFailures, endOfFailureTime, failureDuration);
        try {
            final ElectricPowerFailureEventEntity entity = repository.save(electricPowerFailureEvent);
            repository.refresh(entity);
            return entity;
        } catch (final DataIntegrityViolationException e) {
            throw new ConstraintViolationException(electricPowerFailureEvent.toString(), e);
        }
    }

    @Override
    public Optional<? extends ElectricPowerFailureEvent> find(final ElectricPowerFailures electricPowerFailures,
            final LocalDateTime endOfFailureTime) {
        return repository.findByElectricPowerFailuresAndEndOfFailureTime(
                (ElectricPowerFailuresEntity) electricPowerFailures, endOfFailureTime);
    }

}
