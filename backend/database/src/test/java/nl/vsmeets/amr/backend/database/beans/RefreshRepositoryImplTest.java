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

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link RefreshRepositoryImpl}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class RefreshRepositoryImplTest<T> {

  /**
   * The object under test.
   */
  private RefreshRepositoryImpl<T> testObject;

  /**
   * Constructor parameters.
   */
  @Mock
  private EntityManager entityManager;

  @BeforeEach
  void setUp() throws Exception {
    testObject = new RefreshRepositoryImpl<>(entityManager);
  }

  @Test
  void testRefresh(@Mock final T entity) {
    assertDoesNotThrow(() -> testObject.refresh(entity));
    verify(entityManager).refresh(entity);
  }

}
