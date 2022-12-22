/**
 * Copyright (C) 2019 Vincent Smeets
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;

/**
 * Unit tests for the class {@link RefreshRepositoryImpl}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class RefreshRepositoryImplTest<T> {

    /**
     * Constructor parameters.
     */
    @Mock
    private EntityManager entityManager;

    /**
     * The object under test.
     */
    @InjectMocks
    private RefreshRepositoryImpl<T> testObject;

    @Test
    void testRefresh(@Mock final T entity) {
        assertDoesNotThrow(() -> testObject.refresh(entity));
        verify(entityManager).refresh(entity);
    }

}
