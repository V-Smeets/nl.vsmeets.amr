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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import nl.vsmeets.amr.backend.database.BackendDatabaseConfig;
import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.ElectricPowerFailureEvent;
import nl.vsmeets.amr.backend.database.ElectricPowerFailureEventFactory;
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
import nl.vsmeets.amr.libs.junit.RandomDurationGenerator;
import nl.vsmeets.amr.libs.junit.RandomLocalDateTimeGenerator;
import nl.vsmeets.amr.libs.junit.RandomStringGenerator;
import nl.vsmeets.amr.libs.junit.RandomZoneIdGenerator;

/**
 * Integration tests for {@link ElectricPowerFailureEvent}.
 *
 * @author vincent
 */
@ContextConfiguration(classes = { BackendDatabaseConfig.class })
@DataJpaTest
class ElectricPowerFailureEventIT implements RandomByteGenerator, RandomDurationGenerator, RandomLocalDateTimeGenerator,
    RandomStringGenerator, RandomZoneIdGenerator {

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
  private ElectricPowerFailures electricPowerFailures1;
  private ElectricPowerFailures electricPowerFailures2;

  private final Duration failureDuration1 = randomDurationSeconds();
  private final Duration failureDuration2 = randomDurationSeconds(failureDuration1);
  private final LocalDateTime endOfFailureTime1 = randomLocalDateTimeZeroPrecision();
  private final LocalDateTime endOfFailureTime2 = randomLocalDateTimeZeroPrecision(endOfFailureTime1);

  @Autowired
  private ElectricPowerFailureEventFactory electricPowerFailureEventFactory;
  private ElectricPowerFailureEvent electricPowerFailureEvent;

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
    electricPowerFailures1 = assertDoesNotThrow(() -> electricPowerFailuresFactory.create(meter1,
        randomLocalDateTimeZeroPrecision(), randomInt(), randomInt()));
    assertNotNull(electricPowerFailures1);
    electricPowerFailures2 = assertDoesNotThrow(() -> electricPowerFailuresFactory.create(meter2,
        randomLocalDateTimeZeroPrecision(electricPowerFailures1.getLocalDateTime()),
        randomInt(electricPowerFailures1.getNrOfPowerFailures()),
        randomInt(electricPowerFailures1.getNrOfLongPowerFailures())));
    assertNotNull(electricPowerFailures2);

    electricPowerFailureEvent = assertDoesNotThrow(
        () -> electricPowerFailureEventFactory.create(electricPowerFailures1, endOfFailureTime1, failureDuration1));
  }

  @Test
  void testElectricPowerFailures() {
    final ElectricPowerFailures electricPowerFailures = electricPowerFailureEvent.getElectricPowerFailures();
    assertAll( //
        () -> assertEquals(electricPowerFailures1, electricPowerFailures), //
        () -> assertTrue(electricPowerFailures.getElectricPowerFailureEvents().contains(electricPowerFailureEvent)) //
    );
  }

  @Test
  void testElectricPowerFailuresNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricPowerFailureEventFactory.create(null, endOfFailureTime2, failureDuration2));
  }

  @Test
  void testEndOfFailureTime() {
    final LocalDateTime endOfFailureTime = electricPowerFailureEvent.getEndOfFailureTime();
    assertEquals(endOfFailureTime1, endOfFailureTime);
  }

  @ParameterizedTest
  @ValueSource(longs = { -999_999_999L, -1L, 0L, 1L, 999_999_999L })
  void testEndOfFailureTime(final long seconds) {
    final LocalDateTime endOfFailureTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.ofTotalSeconds(0));
    final ElectricPowerFailureEvent electricPowerFailureEvent2 = assertDoesNotThrow(
        () -> electricPowerFailureEventFactory.create(electricPowerFailures2, endOfFailureTime, failureDuration2));
    assertEquals(endOfFailureTime, electricPowerFailureEvent2.getEndOfFailureTime());
  }

  @Test
  void testEndOfFailureTimeNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricPowerFailureEventFactory.create(electricPowerFailures2, null, failureDuration2));
  }

  @Test
  void testFailureDuration() {
    final Duration failureDuration = electricPowerFailureEvent.getFailureDuration();
    assertEquals(failureDuration1, failureDuration);
  }

  @ParameterizedTest
  @ValueSource(longs = { 0L, 1L, 9_999_999_999L })
  void testFailureDuration(final long value) {
    final Duration failureDuration = Duration.ofSeconds(value);
    final ElectricPowerFailureEvent electricPowerFailureEvent2 = assertDoesNotThrow(
        () -> electricPowerFailureEventFactory.create(electricPowerFailures2, endOfFailureTime2, failureDuration));
    assertEquals(failureDuration, electricPowerFailureEvent2.getFailureDuration());
  }

  @Test
  void testFailureDurationNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricPowerFailureEventFactory.create(electricPowerFailures2, endOfFailureTime2, null));
  }

  @Test
  void testFind() {
    final Optional<? extends ElectricPowerFailureEvent> foundElectricPowerFailureEvent =
        electricPowerFailureEventFactory.find(electricPowerFailures1, endOfFailureTime1);
    assertAll( //
        () -> assertNotNull(foundElectricPowerFailureEvent), //
        () -> assertTrue(foundElectricPowerFailureEvent.isPresent()), //
        () -> assertEquals(electricPowerFailureEvent, foundElectricPowerFailureEvent.get()) //
    );
  }

  @Test
  void testIdGenerated() {
    final Integer id1 = electricPowerFailureEvent.getId();
    assertNotNull(id1);

    final ElectricPowerFailureEvent electricPowerFailureEvent2 = assertDoesNotThrow(
        () -> electricPowerFailureEventFactory.create(electricPowerFailures2, endOfFailureTime2, failureDuration2));
    assertNotNull(electricPowerFailureEvent2);
    final Integer id2 = electricPowerFailureEvent2.getId();
    assertNotNull(id2);

    assertNotEquals(id1, id2);
  }

  @Test
  void testUnique() {
    assertAll( //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricPowerFailureEventFactory.create(electricPowerFailures1, endOfFailureTime1, failureDuration1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricPowerFailureEventFactory.create(electricPowerFailures1, endOfFailureTime1, failureDuration2)), //
        () -> assertDoesNotThrow(
            () -> electricPowerFailureEventFactory.create(electricPowerFailures2, endOfFailureTime1, failureDuration1)), //
        () -> assertDoesNotThrow(
            () -> electricPowerFailureEventFactory.create(electricPowerFailures1, endOfFailureTime2, failureDuration1)) //
    );
  }

}
