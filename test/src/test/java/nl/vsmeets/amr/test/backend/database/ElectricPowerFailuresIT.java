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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import nl.vsmeets.amr.backend.database.BackendDatabaseConfig;
import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.ElectricPowerFailures;
import nl.vsmeets.amr.backend.database.ElectricPowerFailuresFactory;
import nl.vsmeets.amr.backend.database.MeasuredMedium;
import nl.vsmeets.amr.backend.database.MeasuredMediumFactory;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.MeterFactory;
import nl.vsmeets.amr.backend.database.P1Telegram;
import nl.vsmeets.amr.backend.database.P1TelegramFactory;
import nl.vsmeets.amr.backend.database.Site;
import nl.vsmeets.amr.backend.database.SiteFactory;

/**
 * Integration tests for {@link ElectricPowerFailures}.
 *
 * @author vincent
 */
@ContextConfiguration(classes = { BackendDatabaseConfig.class })
@DataJpaTest(
// @formatter:off
    properties = {
      "logging.level.org.springframework.test.context.transaction.TransactionContext=WARN"
    },
    showSql = false
// @formatter:on
)
class ElectricPowerFailuresIT {

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
  private static final LocalDateTime localDateTime1 = DatabaseConstants.MIN_LOCAL_DATE_TIME;
  private static final LocalDateTime localDateTime2 = DatabaseConstants.MAX_LOCAL_DATE_TIME;
  private static final int nrOfPowerFailures1 = 1;
  private static final int nrOfPowerFailures2 = 2;
  private static final int nrOfLongPowerFailures1 = 3;
  private static final int nrOfLongPowerFailures2 = 4;

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
  private Meter meter1;
  private Meter meter2;

  @Autowired
  private ElectricPowerFailuresFactory electricPowerFailuresFactory;
  private ElectricPowerFailures electricPowerFailures;

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
    meter1 = assertDoesNotThrow(() -> meterFactory.create(p1Telegram1, measuredMedium1, equipmentIdentifier1));
    assertNotNull(meter1);
    meter2 = assertDoesNotThrow(() -> meterFactory.create(p1Telegram2, measuredMedium2, equipmentIdentifier2));
    assertNotNull(meter2);

    electricPowerFailures = assertDoesNotThrow(
        () -> electricPowerFailuresFactory.create(meter1, localDateTime1, nrOfPowerFailures1, nrOfLongPowerFailures1));
  }

  @Test
  void testFind() {
    final Optional<? extends ElectricPowerFailures> foundElectricPowerFailures =
        electricPowerFailuresFactory.find(meter1, localDateTime1);
    assertAll( //
        () -> assertNotNull(foundElectricPowerFailures), //
        () -> assertTrue(foundElectricPowerFailures.isPresent()), //
        () -> assertEquals(electricPowerFailures, foundElectricPowerFailures.get()) //
    );
  }

  @Test
  void testIdGenerated() {
    final Integer id1 = electricPowerFailures.getId();
    assertNotNull(id1);

    final ElectricPowerFailures electricPowerFailures2 = assertDoesNotThrow(
        () -> electricPowerFailuresFactory.create(meter2, localDateTime2, nrOfPowerFailures2, nrOfLongPowerFailures2));
    assertNotNull(electricPowerFailures2);
    final Integer id2 = electricPowerFailures2.getId();
    assertNotNull(id2);

    assertNotEquals(id1, id2);
  }

  @Test
  void testLocalDateTime() {
    final LocalDateTime localDateTime = electricPowerFailures.getLocalDateTime();
    assertEquals(localDateTime1, localDateTime);
  }

  @ParameterizedTest
  @MethodSource("nl.vsmeets.amr.test.backend.database.DatabaseConstants#validLocalDateTimeValues")
  void testLocalDateTime(final LocalDateTime localDateTime) {
    final ElectricPowerFailures electricPowerFailures2 = assertDoesNotThrow(
        () -> electricPowerFailuresFactory.create(meter2, localDateTime, nrOfPowerFailures2, nrOfLongPowerFailures2));
    assertEquals(localDateTime, electricPowerFailures2.getLocalDateTime());
  }

  @Test
  void testLocalDateTimeNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricPowerFailuresFactory.create(meter2, null, nrOfPowerFailures2, nrOfLongPowerFailures2));
  }

  @Test
  void testMeter() {
    final Meter meter = electricPowerFailures.getMeter();
    assertAll( //
        () -> assertEquals(meter1, meter), //
        () -> assertTrue(meter.getElectricPowerFailures().contains(electricPowerFailures)) //
    );
  }

  @Test
  void testMeterNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricPowerFailuresFactory.create(null, localDateTime2, nrOfPowerFailures2, nrOfLongPowerFailures2));
  }

  @Test
  void testNrOfLongPowerFailures() {
    final Integer nrOfLongPowerFailures = electricPowerFailures.getNrOfLongPowerFailures();
    assertEquals(nrOfLongPowerFailures1, nrOfLongPowerFailures);
  }

  @Test
  void testNrOfLongPowerFailuresNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricPowerFailuresFactory.create(meter2, localDateTime2, nrOfPowerFailures2, null));
  }

  @Test
  void testNrOfPowerFailures() {
    final Integer nrOfPowerFailures = electricPowerFailures.getNrOfPowerFailures();
    assertEquals(nrOfPowerFailures1, nrOfPowerFailures);
  }

  @Test
  void testNrOfPowerFailuresNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricPowerFailuresFactory.create(meter2, localDateTime2, null, nrOfLongPowerFailures2));
  }

  @Test
  void testUnique() {
    assertAll( //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricPowerFailuresFactory.create(meter1, localDateTime1, nrOfPowerFailures1,
                nrOfLongPowerFailures1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricPowerFailuresFactory.create(meter1, localDateTime1, nrOfPowerFailures2,
                nrOfLongPowerFailures1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricPowerFailuresFactory.create(meter1, localDateTime1, nrOfPowerFailures1,
                nrOfLongPowerFailures2)), //
        () -> assertDoesNotThrow(() -> electricPowerFailuresFactory.create(meter2, localDateTime1, nrOfPowerFailures1,
            nrOfLongPowerFailures1)), //
        () -> assertDoesNotThrow(() -> electricPowerFailuresFactory.create(meter1, localDateTime2, nrOfPowerFailures1,
            nrOfLongPowerFailures1)) //
    );
  }

}
