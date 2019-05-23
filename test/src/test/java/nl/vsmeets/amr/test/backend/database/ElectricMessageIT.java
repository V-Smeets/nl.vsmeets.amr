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
import nl.vsmeets.amr.backend.database.ElectricMessage;
import nl.vsmeets.amr.backend.database.ElectricMessageFactory;
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
 * Integration tests for {@link ElectricMessage}.
 *
 * @author vincent
 */
@ContextConfiguration(classes = { BackendDatabaseConfig.class })
@DataJpaTest
class ElectricMessageIT
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

  private final LocalDateTime localDateTime1 = randomLocalDateTimeZeroPrecision();
  private final LocalDateTime localDateTime2 = randomLocalDateTimeZeroPrecision(localDateTime1);
  private final String textMessage1 = randomString();
  private final String textMessage2 = randomString(textMessage1);

  @Autowired
  private ElectricMessageFactory electricMessageFactory;
  private ElectricMessage electricMessage;

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

    electricMessage = assertDoesNotThrow(() -> electricMessageFactory.create(meter1, localDateTime1, textMessage1));
  }

  @Test
  void testFind() {
    final Optional<? extends ElectricMessage> foundElectricMessage =
        electricMessageFactory.find(meter1, localDateTime1);
    assertAll( //
        () -> assertNotNull(foundElectricMessage), //
        () -> assertTrue(foundElectricMessage.isPresent()), //
        () -> assertEquals(electricMessage, foundElectricMessage.get()) //
    );
  }

  @Test
  void testIdGenerated() {
    final Integer id1 = electricMessage.getId();
    assertNotNull(id1);

    final ElectricMessage electricMessage2 =
        assertDoesNotThrow(() -> electricMessageFactory.create(meter2, localDateTime2, textMessage2));
    assertNotNull(electricMessage2);
    final Integer id2 = electricMessage2.getId();
    assertNotNull(id2);

    assertNotEquals(id1, id2);
  }

  @Test
  void testLocalDateTime() {
    final LocalDateTime localDateTime = electricMessage.getLocalDateTime();
    assertEquals(localDateTime1, localDateTime);
  }

  @ParameterizedTest
  @ValueSource(longs = { -999_999_999L, -1L, 0L, 1L, 999_999_999L })
  void testLocalDateTime(final long seconds) {
    final LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.ofTotalSeconds(0));
    final ElectricMessage electricMessage2 =
        assertDoesNotThrow(() -> electricMessageFactory.create(meter2, localDateTime, textMessage2));
    assertEquals(localDateTime, electricMessage2.getLocalDateTime());
  }

  @Test
  void testLocalDateTimeNotNull() {
    assertThrows(NullPointerException.class, () -> electricMessageFactory.create(meter2, null, textMessage2));
  }

  @Test
  void testMeter() {
    final Meter meter = electricMessage.getMeter();
    assertAll( //
        () -> assertEquals(meter1, meter), //
        () -> assertTrue(meter.getElectricMessages().contains(electricMessage)) //
    );
  }

  @Test
  void testMeterNotNull() {
    assertThrows(NullPointerException.class, () -> electricMessageFactory.create(null, localDateTime2, textMessage2));
  }

  @Test
  void testTextMessage() {
    final String textMessage = electricMessage.getTextMessage();
    assertEquals(textMessage1, textMessage);
  }

  @Test
  void testTextMessageInvalidLength() {
    assertThrows(ConstraintViolationException.class,
        () -> electricMessageFactory.create(meter2, localDateTime2, randomStringOfCharacters(1025)));
  }

  @Test
  void testTextMessageNotNull() {
    assertThrows(NullPointerException.class, () -> electricMessageFactory.create(meter2, localDateTime2, null));
  }

  @Test
  void testTextMessageValidLength() {
    assertDoesNotThrow(() -> electricMessageFactory.create(meter2, localDateTime2, randomStringOfCharacters(1024)));
  }

  @Test
  void testUnique() {
    assertAll( //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricMessageFactory.create(meter1, localDateTime1, textMessage1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricMessageFactory.create(meter1, localDateTime1, textMessage2)), //
        () -> assertDoesNotThrow(() -> electricMessageFactory.create(meter2, localDateTime1, textMessage1)), //
        () -> assertDoesNotThrow(() -> electricMessageFactory.create(meter1, localDateTime2, textMessage1)) //
    );
  }

}
