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
package nl.vsmeets.amr.test.backend.database;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import nl.vsmeets.amr.backend.database.BackendDatabaseConfig;
import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.Site;
import nl.vsmeets.amr.backend.database.SiteFactory;
import nl.vsmeets.amr.libs.junit.RandomStringGenerator;
import nl.vsmeets.amr.libs.junit.RandomZoneIdGenerator;

/**
 * Integration tests for {@link Site}.
 *
 * @author vincent
 */
@ContextConfiguration(classes = { BackendDatabaseConfig.class })
@DataJpaTest
class SiteIT implements RandomStringGenerator, RandomZoneIdGenerator {

  private final String name1 = randomString();
  private final String name2 = randomString(name1);
  private final ZoneId timeZone1 = randomZoneId();
  private final ZoneId timeZone2 = randomZoneId(timeZone1);

  @Autowired
  private SiteFactory siteFactory;
  private Site site;

  @BeforeEach
  void setup() {
    site = assertDoesNotThrow(() -> siteFactory.create(name1, timeZone1));
    assertNotNull(site);
  }

  @Test
  void testFind() {
    final Optional<? extends Site> foundSite = siteFactory.find(name1);
    assertAll( //
        () -> assertNotNull(foundSite), //
        () -> assertTrue(foundSite.isPresent()), //
        () -> assertEquals(site, foundSite.get()) //
    );
  }

  @Test
  void testIdGenerated() {
    final Integer id1 = site.getId();
    assertNotNull(id1);

    final Site site2 = assertDoesNotThrow(() -> siteFactory.create(name2, timeZone2));
    assertNotNull(site2);
    final Integer id2 = site2.getId();
    assertNotNull(id2);

    assertNotEquals(id1, id2);
  }

  @Test
  void testName() {
    assertEquals(name1, site.getName());
  }

  @Test
  void testNameInvalidLength() {
    assertThrows(ConstraintViolationException.class, () -> siteFactory.create(randomStringOfCharacters(65), timeZone2));
  }

  @Test
  void testNameNotNull() {
    assertThrows(NullPointerException.class, () -> siteFactory.create(null, timeZone2));
  }

  @Test
  void testNameValidLength() {
    assertDoesNotThrow(() -> siteFactory.create(randomStringOfCharacters(64), timeZone2));
  }

  @Test
  void testTimeZone() {
    assertEquals(timeZone1, site.getTimeZone());
  }

  @Test
  void testTimeZoneNotNull() {
    assertThrows(NullPointerException.class, () -> siteFactory.create(name2, null));
  }

  @Test
  void testUnique() {
    assertAll( //
        () -> assertThrows(ConstraintViolationException.class, () -> siteFactory.create(name1, timeZone1)), //
        () -> assertThrows(ConstraintViolationException.class, () -> siteFactory.create(name1, timeZone2)), //
        () -> assertDoesNotThrow(() -> siteFactory.create(name2, timeZone1)) //
    );
  }

}
