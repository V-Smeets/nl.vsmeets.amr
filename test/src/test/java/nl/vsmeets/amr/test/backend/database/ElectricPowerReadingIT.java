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

import static javax.measure.MetricPrefix.KILO;
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

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Power;
import javax.measure.spi.QuantityFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import nl.vsmeets.amr.backend.database.BackendDatabaseConfig;
import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.ElectricPowerReading;
import nl.vsmeets.amr.backend.database.ElectricPowerReadingFactory;
import nl.vsmeets.amr.backend.database.MeasuredMedium;
import nl.vsmeets.amr.backend.database.MeasuredMediumFactory;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.MeterFactory;
import nl.vsmeets.amr.backend.database.P1Telegram;
import nl.vsmeets.amr.backend.database.P1TelegramFactory;
import nl.vsmeets.amr.backend.database.Site;
import nl.vsmeets.amr.backend.database.SiteFactory;

/**
 * Integration tests for {@link ElectricPowerReading}.
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
class ElectricPowerReadingIT {

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
  private static final int consumedPowerValue1 = 0;
  private static final int consumedPowerValue2 = 99_999;
  private static final int producedPowerValue1 = 0;
  private static final int producedPowerValue2 = 99_999;

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
  private QuantityFactory<Power> powerQuantityFactory;
  @Autowired
  @Qualifier("watt")
  private Unit<Power> watt;

  @Autowired
  private ElectricPowerReadingFactory electricPowerReadingFactory;
  private ElectricPowerReading electricPowerReading;

  private Quantity<Power> consumedPower1;
  private Quantity<Power> consumedPower2;
  private Quantity<Power> producedPower1;
  private Quantity<Power> producedPower2;

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

    consumedPower1 = powerQuantityFactory.create(consumedPowerValue1, watt);
    consumedPower2 = powerQuantityFactory.create(consumedPowerValue2, watt);
    producedPower1 = powerQuantityFactory.create(producedPowerValue1, watt);
    producedPower2 = powerQuantityFactory.create(producedPowerValue2, watt);

    electricPowerReading = assertDoesNotThrow(
        () -> electricPowerReadingFactory.create(meter1, localDateTime1, consumedPower1, producedPower1));
  }

  @Test
  void testConsumedPower() {
    final Quantity<Power> consumedPower = electricPowerReading.getConsumedPower();
    assertEquals(consumedPower1, consumedPower);
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 99_999 })
  void testConsumedPower(final int value) {
    final Quantity<Power> consumedPower = powerQuantityFactory.create(value, watt);
    final ElectricPowerReading electricPowerReading2 = assertDoesNotThrow(
        () -> electricPowerReadingFactory.create(meter2, localDateTime2, consumedPower, producedPower2));
    assertEquals(consumedPower, electricPowerReading2.getConsumedPower());
  }

  @Test
  void testConsumedPowerKiloWatt() {
    final Unit<Power> kiloWatt = KILO(watt);
    final Quantity<Power> consumedPower = powerQuantityFactory.create(1, kiloWatt);
    final ElectricPowerReading electricPowerReading2 = assertDoesNotThrow(
        () -> electricPowerReadingFactory.create(meter2, localDateTime2, consumedPower, producedPower2));
    final Quantity<Power> consumedPowerKW = electricPowerReading2.getConsumedPower();
    assertAll( //
        () -> assertEquals(watt, consumedPowerKW.getUnit()), //
        () -> assertEquals(1_000, consumedPowerKW.getValue().intValue()) //
    );
  }

  @Test
  void testConsumedPowerNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricPowerReadingFactory.create(meter2, localDateTime2, null, producedPower2));
  }

  @Test
  void testFind() {
    final Optional<? extends ElectricPowerReading> foundElectricPowerReading =
        electricPowerReadingFactory.find(meter1, localDateTime1);
    assertAll( //
        () -> assertNotNull(foundElectricPowerReading), //
        () -> assertTrue(foundElectricPowerReading.isPresent()), //
        () -> assertEquals(electricPowerReading, foundElectricPowerReading.get()) //
    );
  }

  @Test
  void testIdGenerated() {
    final Integer id1 = electricPowerReading.getId();
    assertNotNull(id1);

    final ElectricPowerReading electricPowerReading2 = assertDoesNotThrow(
        () -> electricPowerReadingFactory.create(meter2, localDateTime2, consumedPower2, producedPower2));
    assertNotNull(electricPowerReading2);
    final Integer id2 = electricPowerReading2.getId();
    assertNotNull(id2);

    assertNotEquals(id1, id2);
  }

  @Test
  void testLocalDateTime() {
    final LocalDateTime localDateTime = electricPowerReading.getLocalDateTime();
    assertEquals(localDateTime1, localDateTime);
  }

  @ParameterizedTest
  @MethodSource("nl.vsmeets.amr.test.backend.database.DatabaseConstants#validLocalDateTimeValues")
  void testLocalDateTime(final LocalDateTime localDateTime) {
    final ElectricPowerReading electricPowerReading2 = assertDoesNotThrow(
        () -> electricPowerReadingFactory.create(meter2, localDateTime, consumedPower2, producedPower2));
    assertEquals(localDateTime, electricPowerReading2.getLocalDateTime());
  }

  @Test
  void testLocalDateTimeNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricPowerReadingFactory.create(meter2, null, consumedPower2, producedPower2));
  }

  @Test
  void testMeter() {
    final Meter meter = electricPowerReading.getMeter();
    assertAll( //
        () -> assertEquals(meter1, meter), //
        () -> assertTrue(meter.getElectricPowerReadings().contains(electricPowerReading)) //
    );
  }

  @Test
  void testMeterNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricPowerReadingFactory.create(null, localDateTime2, consumedPower2, producedPower2));
  }

  @Test
  void testProducedPower() {
    final Quantity<Power> producedPower = electricPowerReading.getProducedPower();
    assertEquals(producedPower1, producedPower);
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 99_999 })
  void testProducedPower(final int value) {
    final Quantity<Power> producedPower = powerQuantityFactory.create(value, watt);
    final ElectricPowerReading electricPowerReading2 = assertDoesNotThrow(
        () -> electricPowerReadingFactory.create(meter2, localDateTime2, consumedPower2, producedPower));
    assertEquals(producedPower, electricPowerReading2.getProducedPower());
  }

  @Test
  void testProducedPowerKiloWatt() {
    final Unit<Power> kiloWatt = KILO(watt);
    final Quantity<Power> producedPower = powerQuantityFactory.create(1, kiloWatt);
    final ElectricPowerReading electricPowerReading2 = assertDoesNotThrow(
        () -> electricPowerReadingFactory.create(meter2, localDateTime2, consumedPower2, producedPower));
    final Quantity<Power> producedPowerKW = electricPowerReading2.getProducedPower();
    assertAll( //
        () -> assertEquals(watt, producedPowerKW.getUnit()), //
        () -> assertEquals(1_000, producedPowerKW.getValue().intValue()) //
    );
  }

  @Test
  void testProducedPowerNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricPowerReadingFactory.create(meter2, localDateTime2, null, producedPower2));
  }

  @Test
  void testUnique() {
    assertAll( //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricPowerReadingFactory.create(meter1, localDateTime1, consumedPower1, producedPower1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricPowerReadingFactory.create(meter1, localDateTime1, consumedPower2, producedPower1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricPowerReadingFactory.create(meter1, localDateTime1, consumedPower1, producedPower2)), //
        () -> assertDoesNotThrow(
            () -> electricPowerReadingFactory.create(meter2, localDateTime1, consumedPower1, producedPower1)), //
        () -> assertDoesNotThrow(
            () -> electricPowerReadingFactory.create(meter1, localDateTime2, consumedPower1, producedPower1)) //
    );
  }

}
