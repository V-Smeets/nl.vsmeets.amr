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

import java.time.LocalDateTime;
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
import nl.vsmeets.amr.libs.junit.RandomByteGenerator;
import nl.vsmeets.amr.libs.junit.RandomLocalDateTimeGenerator;
import nl.vsmeets.amr.libs.junit.RandomStringGenerator;
import nl.vsmeets.amr.libs.junit.RandomZoneIdGenerator;

/**
 * Integration tests for {@link ElectricPowerFailures}.
 *
 * @author vincent
 */
@ContextConfiguration(classes = { BackendDatabaseConfig.class })
@DataJpaTest
class ElectricPowerFailuresIT
    implements RandomByteGenerator, RandomLocalDateTimeGenerator, RandomStringGenerator, RandomZoneIdGenerator {

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

  private final LocalDateTime localDateTime1 = randomLocalDateTimeZeroPrecisionRange(
      DatabaseConstants.MIN_LOCAL_DATE_TIME, DatabaseConstants.MAX_LOCAL_DATE_TIME);
  private final LocalDateTime localDateTime2 = randomLocalDateTimeZeroPrecisionRange(
      DatabaseConstants.MIN_LOCAL_DATE_TIME, DatabaseConstants.MAX_LOCAL_DATE_TIME, localDateTime1);
  private final Integer nrOfPowerFailures1 = randomInt();
  private final Integer nrOfPowerFailures2 = randomInt(nrOfPowerFailures1);
  private final Integer nrOfLongPowerFailures1 = randomInt();
  private final Integer nrOfLongPowerFailures2 = randomInt(nrOfLongPowerFailures1);

  @Autowired
  private ElectricPowerFailuresFactory electricPowerFailuresFactory;
  private ElectricPowerFailures electricPowerFailures;

  @BeforeEach
  void setup() {
    site1 = assertDoesNotThrow(() -> siteFactory.create(randomString(), randomZoneId()));
    assertNotNull(site1);
    site2 =
        assertDoesNotThrow(() -> siteFactory.create(randomString(site1.getName()), randomZoneId(site1.getTimeZone())));
    assertNotNull(site2);
    p1Telegram1 = assertDoesNotThrow(() -> p1TelegramFactory.create(site1, randomString(), randomByte()));
    assertNotNull(p1Telegram1);
    p1Telegram2 = assertDoesNotThrow(() -> p1TelegramFactory.create(site2,
        randomString(p1Telegram1.getHeaderInformation()), randomByte(p1Telegram1.getVersionInformation())));
    assertNotNull(p1Telegram2);
    final Optional<? extends MeasuredMedium> optionalMeasuredMedium1 =
        assertDoesNotThrow(() -> measuredMediumFactory.find((byte) 0x02));
    assertAll( //
        () -> assertNotNull(optionalMeasuredMedium1), //
        () -> assertTrue(optionalMeasuredMedium1.isPresent()) //
    );
    measuredMedium1 = optionalMeasuredMedium1.get();
    assertNotNull(measuredMedium1);
    final Optional<? extends MeasuredMedium> optionalMeasuredMedium2 =
        assertDoesNotThrow(() -> measuredMediumFactory.find((byte) 0x03));
    assertAll( //
        () -> assertNotNull(optionalMeasuredMedium2), //
        () -> assertTrue(optionalMeasuredMedium2.isPresent()) //
    );
    measuredMedium2 = optionalMeasuredMedium2.get();
    assertNotNull(measuredMedium2);
    meter1 = assertDoesNotThrow(() -> meterFactory.create(p1Telegram1, measuredMedium1, randomString()));
    assertNotNull(meter1);
    meter2 = assertDoesNotThrow(
        () -> meterFactory.create(p1Telegram2, measuredMedium2, randomString(meter1.getEquipmentIdentifier())));
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
