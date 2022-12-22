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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

/**
 * A repository fragment that provides the {@link #refresh(Object)} method of
 * the entity manager.
 *
 * @author vincent
 * @param <T> The type of the entity.
 */
@Transactional(readOnly = true)
public class RefreshRepositoryImpl<T> implements RefreshRepository<T> {

    /**
     * The entity manager.
     */
    @Autowired
    private EntityManager entityManager;

    @Override
    public void refresh(final T entity) {
        entityManager.refresh(entity);
    }

}
