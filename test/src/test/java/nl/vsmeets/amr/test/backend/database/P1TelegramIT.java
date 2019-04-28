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

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import nl.vsmeets.amr.backend.database.BackendDatabaseConfig;
import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.P1Telegram;
import nl.vsmeets.amr.backend.database.P1TelegramFactory;
import nl.vsmeets.amr.backend.database.Site;
import nl.vsmeets.amr.backend.database.SiteFactory;
import nl.vsmeets.amr.libs.junit.RandomByteGenerator;
import nl.vsmeets.amr.libs.junit.RandomStringGenerator;
import nl.vsmeets.amr.libs.junit.RandomZoneIdGenerator;

/**
 * Integration tests for {@link P1Telegram}.
 *
 * @author vincent
 */
@ContextConfiguration(classes = { BackendDatabaseConfig.class })
@DataJpaTest
class P1TelegramIT implements RandomByteGenerator, RandomStringGenerator, RandomZoneIdGenerator {

  @Autowired
  private SiteFactory siteFactory;
  private Site site1;
  private Site site2;

  private final String headerInformation1 = randomString();
  private final String headerInformation2 = randomString(headerInformation1);
  private final Byte versionInformation1 = randomByte();
  private final Byte versionInformation2 = randomByte(versionInformation1);

  @Autowired
  private P1TelegramFactory p1TelegramFactory;
  private P1Telegram p1Telegram;

  @BeforeEach
  void setup() {
    site1 = assertDoesNotThrow(() -> siteFactory.create(randomString(), randomZoneId()));
    assertNotNull(site1);
    site2 =
        assertDoesNotThrow(() -> siteFactory.create(randomString(site1.getName()), randomZoneId(site1.getTimeZone())));
    assertNotNull(site2);

    p1Telegram = assertDoesNotThrow(() -> p1TelegramFactory.create(site1, headerInformation1, versionInformation1));
    assertNotNull(p1Telegram);
  }

  @Test
  void testFind() {
    final Optional<? extends P1Telegram> foundP1Telegram =
        p1TelegramFactory.find(site1, headerInformation1, versionInformation1);
    assertAll( //
        () -> assertNotNull(foundP1Telegram), //
        () -> assertTrue(foundP1Telegram.isPresent()), //
        () -> assertEquals(p1Telegram, foundP1Telegram.get()) //
    );
  }

  @Test
  void testHeaderInformation() {
    final String headerInformation = p1Telegram.getHeaderInformation();
    assertEquals(headerInformation1, headerInformation);
  }

  @Test
  void testHeaderInformationInvalidLength() {
    assertThrows(ConstraintViolationException.class,
        () -> p1TelegramFactory.create(site2, randomStringOfCharacters(17), versionInformation2));
  }

  @Test
  void testHeaderInformationNotNull() {
    assertThrows(NullPointerException.class, () -> p1TelegramFactory.create(site2, null, versionInformation2));
  }

  @Test
  void testHeaderInformationValidLength() {
    assertDoesNotThrow(() -> p1TelegramFactory.create(site2, randomStringOfCharacters(16), versionInformation2));
  }

  @Test
  void testIdGenerated() {
    final Integer id1 = p1Telegram.getId();
    assertNotNull(id1);

    final P1Telegram p1Telegram2 =
        assertDoesNotThrow(() -> p1TelegramFactory.create(site2, headerInformation2, versionInformation2));
    assertNotNull(p1Telegram2);
    final Integer id2 = p1Telegram2.getId();
    assertNotNull(id2);

    assertNotEquals(id1, id2);
  }

  @Test
  void testSite() {
    final Site site = p1Telegram.getSite();
    assertAll( //
        () -> assertEquals(site1, site), //
        () -> assertTrue(site.getP1Telegrams().contains(p1Telegram)) //
    );
  }

  @Test
  void testSiteNotNull() {
    assertThrows(NullPointerException.class,
        () -> p1TelegramFactory.create(null, headerInformation2, versionInformation2));
  }

  @Test
  void testUnique() {
    assertAll( //
        () -> assertThrows(ConstraintViolationException.class,
            () -> p1TelegramFactory.create(site1, headerInformation1, versionInformation1)), //
        () -> assertDoesNotThrow(() -> p1TelegramFactory.create(site2, headerInformation1, versionInformation1)), //
        () -> assertDoesNotThrow(() -> p1TelegramFactory.create(site1, headerInformation2, versionInformation1)), //
        () -> assertDoesNotThrow(() -> p1TelegramFactory.create(site1, headerInformation1, versionInformation2)) //
    );
  }

  @Test
  void testVersionInformation() {
    final Byte versionInformation = p1Telegram.getVersionInformation();
    assertEquals(versionInformation1, versionInformation);
  }

  @Test
  void testVersionInformationNotNull() {
    assertThrows(NullPointerException.class, () -> p1TelegramFactory.create(site2, headerInformation2, null));
  }

}
