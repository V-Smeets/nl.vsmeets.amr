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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.Site;
import nl.vsmeets.amr.backend.database.entities.SiteEntity;
import nl.vsmeets.amr.libs.junit.RandomStringGenerator;

/**
 * Unit tests for the class {@link SiteFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class SiteFactoryBeanTest implements RandomStringGenerator {

  /**
   * The object under test.
   */
  private SiteFactoryBean testObject;

  /**
   * Constructor parameters.
   */
  @Mock
  private SiteRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    testObject = new SiteFactoryBean(repository);
  }

  @Test
  void testCreate() {
    final String name = randomString();
    final ZoneId timeZone = ZoneId.systemDefault();

    when(repository.save(any(SiteEntity.class))).then(i -> i.getArgument(0));

    final Site result = assertDoesNotThrow(() -> testObject.create(name, timeZone));
    verify(repository).refresh(any(SiteEntity.class));
    // @formatter:off
    assertAll(
        () -> assertNull(result.getId()),
        () -> assertEquals(name, result.getName()),
        () -> assertEquals(timeZone, result.getTimeZone()));
    // @formatter:on
  }

  @Test
  void testCreateDataIntegrityViolationException() {
    final String name = randomString();
    final ZoneId timeZone = ZoneId.systemDefault();

    when(repository.save(any(SiteEntity.class))).thenThrow(new DataIntegrityViolationException(null));

    assertThrows(ConstraintViolationException.class, () -> testObject.create(name, timeZone));
  }

  @Test
  void testFind(@Mock final Site site) {
    final String name = randomString();
    final Optional<? extends Site> expectedResult = Optional.of(site);

    when(repository.findByName(name)).then(i -> expectedResult);

    assertEquals(expectedResult, testObject.find(name));
  }

}
