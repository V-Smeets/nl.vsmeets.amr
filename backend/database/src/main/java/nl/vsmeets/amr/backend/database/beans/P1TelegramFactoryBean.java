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
import nl.vsmeets.amr.backend.database.P1Telegram;
import nl.vsmeets.amr.backend.database.P1TelegramFactory;
import nl.vsmeets.amr.backend.database.Site;
import nl.vsmeets.amr.backend.database.entities.P1TelegramEntity;
import nl.vsmeets.amr.backend.database.entities.SiteEntity;

/**
 * Create and find {@link P1Telegram} entities.
 *
 * @author vincent
 */
@Service
public class P1TelegramFactoryBean implements P1TelegramFactory {

    /**
     * The repository for a {@link P1Telegram}.
     */
    @Autowired
    private P1TelegramRepository repository;

    @Override
    public P1Telegram create(final Site site, final String headerInformation, final Byte versionInformation)
            throws ConstraintViolationException {
        final P1TelegramEntity p1Telegram = new P1TelegramEntity((SiteEntity) site, headerInformation,
                versionInformation);
        try {
            final P1TelegramEntity entity = repository.save(p1Telegram);
            repository.refresh(entity);
            return entity;
        } catch (final DataIntegrityViolationException e) {
            throw new ConstraintViolationException(p1Telegram.toString(), e);
        }
    }

    @Override
    public Optional<? extends P1Telegram> find(final Site site, final String headerInformation,
            final Byte versionInformation) {
        return repository.findBySiteAndHeaderInformationAndVersionInformation((SiteEntity) site, headerInformation,
                versionInformation);
    }

}
