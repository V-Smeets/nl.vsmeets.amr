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
import nl.vsmeets.amr.backend.database.MeasuredMedium;
import nl.vsmeets.amr.backend.database.MeasuredMediumFactory;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.MeterFactory;
import nl.vsmeets.amr.backend.database.P1Telegram;
import nl.vsmeets.amr.backend.database.P1TelegramFactory;
import nl.vsmeets.amr.backend.database.Site;
import nl.vsmeets.amr.backend.database.SiteFactory;

/**
 * Integration tests for {@link P1Telegram}.
 *
 * @author vincent
 */
@ContextConfiguration(classes = { BackendDatabaseConfig.class })
@DataJpaTest(showSql = false)
class MeterIT {

  /**
   * Values used during tests.
   */
  private static final String siteName1 = "Name 1";
  private static final String siteName2 = "Name 2";
  private static final String[] ZoneIds = ZoneId.getAvailableZoneIds().toArray(String[]::new);
  private static final ZoneId timeZone1 = ZoneId.of(ZoneIds[0]);
  private static final ZoneId timeZone2 = ZoneId.of(ZoneIds[1]);
  private static final String headerInformation1 = "Header Info 1";
  private static final String headerInformation2 = "Header Info 2";
  private static final byte versionInformation1 = 0x00;
  private static final byte versionInformation2 = -1; // 0xFF;
  private static final byte measuredMediumId1 = 0x00;
  private static final byte measuredMediumId2 = 0x19;
  private static final String equipmentIdentifier1 = "Equipment Identifier 1";
  private static final String equipmentIdentifier2 = "Equipment Identifier 2";
  private static final String equipmentIdentifierMax = String.format("%48s", "");

  @Autowired
  private SiteFactory siteFactory;
  private Site site1;
  private Site site2;

  @Autowired
  private P1TelegramFactory p1TelegramFactory;
  private P1Telegram p1Telegram1;
  private P1Telegram p1Telegram2;

  @Autowired
  private MeasuredMediumFactory measuredMediumFactory;
  private MeasuredMedium measuredMedium1;
  private MeasuredMedium measuredMedium2;

  @Autowired
  private MeterFactory meterFactory;
  private Meter meter;

  @BeforeEach
  void setup() {
    site1 = assertDoesNotThrow(() -> siteFactory.create(siteName1, timeZone1));
    assertNotNull(site1);
    site2 = assertDoesNotThrow(() -> siteFactory.create(siteName2, timeZone2));
    assertNotNull(site2);
    p1Telegram1 = assertDoesNotThrow(() -> p1TelegramFactory.create(site1, headerInformation1, versionInformation1));
    assertNotNull(p1Telegram1);
    p1Telegram2 = assertDoesNotThrow(() -> p1TelegramFactory.create(site2, headerInformation2, versionInformation2));
    assertNotNull(p1Telegram2);
    final Optional<? extends MeasuredMedium> optionalMeasuredMedium1 =
        assertDoesNotThrow(() -> measuredMediumFactory.find(measuredMediumId1));
    assertAll( //
        () -> assertNotNull(optionalMeasuredMedium1), //
        () -> assertTrue(optionalMeasuredMedium1.isPresent()) //
    );
    measuredMedium1 = optionalMeasuredMedium1.get();
    assertNotNull(measuredMedium1);
    final Optional<? extends MeasuredMedium> optionalMeasuredMedium2 =
        assertDoesNotThrow(() -> measuredMediumFactory.find(measuredMediumId2));
    assertAll( //
        () -> assertNotNull(optionalMeasuredMedium2), //
        () -> assertTrue(optionalMeasuredMedium2.isPresent()) //
    );
    measuredMedium2 = optionalMeasuredMedium2.get();
    assertNotNull(measuredMedium2);

    meter = assertDoesNotThrow(() -> meterFactory.create(p1Telegram1, measuredMedium1, equipmentIdentifier1));
    assertNotNull(meter);
  }

  @Test
  void testEquipmentIdentifier() {
    final String equipmentIdentifier = meter.getEquipmentIdentifier();
    assertEquals(equipmentIdentifier1, equipmentIdentifier);
  }

  @Test
  void testEquipmentIdentifierInvalidLength() {
    assertThrows(ConstraintViolationException.class,
        () -> meterFactory.create(p1Telegram2, measuredMedium2, equipmentIdentifierMax + " "));
  }

  @Test
  void testEquipmentIdentifierNotNull() {
    assertThrows(NullPointerException.class, () -> meterFactory.create(p1Telegram2, measuredMedium2, null));
  }

  @Test
  void testEquipmentIdentifierValidLength() {
    assertDoesNotThrow(() -> meterFactory.create(p1Telegram2, measuredMedium2, equipmentIdentifierMax));
  }

  @Test
  void testFind() {
    final Optional<? extends Meter> foundMeter = meterFactory.find(p1Telegram1, measuredMedium1, equipmentIdentifier1);
    assertAll( //
        () -> assertNotNull(foundMeter), //
        () -> assertTrue(foundMeter.isPresent()), //
        () -> assertEquals(meter, foundMeter.get()) //
    );
  }

  @Test
  void testIdGenerated() {
    final Integer id1 = meter.getId();
    assertNotNull(id1);

    final Meter meter2 =
        assertDoesNotThrow(() -> meterFactory.create(p1Telegram2, measuredMedium2, equipmentIdentifier2));
    assertNotNull(meter2);
    final Integer id2 = meter2.getId();
    assertNotNull(id2);

    assertNotEquals(id1, id2);
  }

  @Test
  void testMeasuredMedium() {
    final MeasuredMedium measuredMedium = meter.getMeasuredMedium();
    assertAll( //
        () -> assertEquals(measuredMedium1, measuredMedium), //
        () -> assertTrue(measuredMedium.getMeters().contains(meter)) //
    );
  }

  @Test
  void testMeasuredMediumNotNull() {
    assertThrows(NullPointerException.class, () -> meterFactory.create(p1Telegram2, null, equipmentIdentifier2));
  }

  @Test
  void testP1Telegram() {
    final P1Telegram p1Telegram = meter.getP1Telegram();
    assertAll( //
        () -> assertEquals(p1Telegram1, p1Telegram), //
        () -> assertTrue(p1Telegram.getMeters().contains(meter)) //
    );
  }

  @Test
  void testP1TelegramNotNull() {
    assertThrows(NullPointerException.class, () -> meterFactory.create(null, measuredMedium2, equipmentIdentifier2));
  }

  @Test
  void testUnique() {
    assertAll( //
        () -> assertThrows(ConstraintViolationException.class,
            () -> meterFactory.create(p1Telegram1, measuredMedium1, equipmentIdentifier1)), //
        () -> assertDoesNotThrow(() -> meterFactory.create(p1Telegram2, measuredMedium1, equipmentIdentifier1)), //
        () -> assertDoesNotThrow(() -> meterFactory.create(p1Telegram1, measuredMedium2, equipmentIdentifier1)), //
        () -> assertDoesNotThrow(() -> meterFactory.create(p1Telegram1, measuredMedium1, equipmentIdentifier2)) //
    );
  }

}
