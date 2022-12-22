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

import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.Site;
import nl.vsmeets.amr.backend.database.SiteFactory;
import nl.vsmeets.amr.backend.database.entities.SiteEntity;

/**
 * Create and find {@link Site} entities.
 *
 * @author vincent
 */
@Service
public class SiteFactoryBean implements SiteFactory {

    /**
     * The repository for a {@link Site}.
     */
    @Autowired
    private SiteRepository repository;

    @Override
    public Site create(final String name, final ZoneId timeZone) throws ConstraintViolationException {
        final SiteEntity site = new SiteEntity(name, timeZone);
        try {
            final SiteEntity entity = repository.save(site);
            repository.refresh(entity);
            return entity;
        } catch (final DataIntegrityViolationException e) {
            throw new ConstraintViolationException(site.toString(), e);
        }
    }

    @Override
    public Optional<? extends Site> find(final String name) {
        return repository.findByName(name);
    }

}
