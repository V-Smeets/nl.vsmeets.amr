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

/**
 * Integration tests for {@link Site}.
 *
 * @author vincent
 */
@ContextConfiguration(classes = { BackendDatabaseConfig.class })
@DataJpaTest(showSql = false)
class SiteIT {

  /**
   * Values used during tests.
   */
  private static final String siteName1 = "Name 1";
  private static final String siteName2 = "Name 2";
  private static final String siteNameMax = String.format("%64s", "");
  private static final String[] ZoneIds = ZoneId.getAvailableZoneIds().toArray(String[]::new);
  private static final ZoneId timeZone1 = ZoneId.of(ZoneIds[0]);
  private static final ZoneId timeZone2 = ZoneId.of(ZoneIds[1]);

  @Autowired
  private SiteFactory siteFactory;
  private Site site;

  @BeforeEach
  void setup() {
    site = assertDoesNotThrow(() -> siteFactory.create(siteName1, timeZone1));
    assertNotNull(site);
  }

  @Test
  void testFind() {
    final Optional<? extends Site> foundSite = siteFactory.find(siteName1);
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

    final Site site2 = assertDoesNotThrow(() -> siteFactory.create(siteName2, timeZone2));
    assertNotNull(site2);
    final Integer id2 = site2.getId();
    assertNotNull(id2);

    assertNotEquals(id1, id2);
  }

  @Test
  void testName() {
    assertEquals(siteName1, site.getName());
  }

  @Test
  void testNameInvalidLength() {
    assertThrows(ConstraintViolationException.class, () -> siteFactory.create(siteNameMax + " ", timeZone2));
  }

  @Test
  void testNameNotNull() {
    assertThrows(NullPointerException.class, () -> siteFactory.create(null, timeZone2));
  }

  @Test
  void testNameValidLength() {
    assertDoesNotThrow(() -> siteFactory.create(siteNameMax, timeZone2));
  }

  @Test
  void testTimeZone() {
    assertEquals(timeZone1, site.getTimeZone());
  }

  @Test
  void testTimeZoneNotNull() {
    assertThrows(NullPointerException.class, () -> siteFactory.create(siteName2, null));
  }

  @Test
  void testUnique() {
    assertAll( //
        () -> assertThrows(ConstraintViolationException.class, () -> siteFactory.create(siteName1, timeZone1)), //
        () -> assertThrows(ConstraintViolationException.class, () -> siteFactory.create(siteName1, timeZone2)), //
        () -> assertDoesNotThrow(() -> siteFactory.create(siteName2, timeZone1)) //
    );
  }

}
