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
import java.time.ZoneId;
import java.util.Optional;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Volume;
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
import nl.vsmeets.amr.backend.database.GasVolumeReading;
import nl.vsmeets.amr.backend.database.GasVolumeReadingFactory;
import nl.vsmeets.amr.backend.database.MeasuredMedium;
import nl.vsmeets.amr.backend.database.MeasuredMediumFactory;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.MeterFactory;
import nl.vsmeets.amr.backend.database.P1Telegram;
import nl.vsmeets.amr.backend.database.P1TelegramFactory;
import nl.vsmeets.amr.backend.database.Site;
import nl.vsmeets.amr.backend.database.SiteFactory;

/**
 * Integration tests for {@link GasVolumeReading}.
 *
 * @author vincent
 */
@ContextConfiguration(classes = { BackendDatabaseConfig.class })
@DataJpaTest(showSql = false)
class GasVolumeReadingIT {

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
  private static final int consumedGasValue1 = 0;
  private static final int consumedGasValue2 = 99_999_999;

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
  private QuantityFactory<Volume> volumeQuantityFactory;
  @Autowired
  @Qualifier("cubicDeciMeter")
  private Unit<Volume> cubicDeciMeter;

  private Quantity<Volume> consumedGas1;
  private Quantity<Volume> consumedGas2;

  @Autowired
  private GasVolumeReadingFactory gasVolumeReadingFactory;
  private GasVolumeReading gasVolumeReading;

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

    consumedGas1 = volumeQuantityFactory.create(consumedGasValue1, cubicDeciMeter);
    consumedGas2 = volumeQuantityFactory.create(consumedGasValue2, cubicDeciMeter);

    gasVolumeReading = assertDoesNotThrow(() -> gasVolumeReadingFactory.create(meter1, localDateTime1, consumedGas1));
  }

  @Test
  void testConsumedGas() {
    final Quantity<Volume> consumedGas = gasVolumeReading.getConsumedGas();
    assertEquals(consumedGas1, consumedGas);
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 999_999_999 })
  void testConsumedGas(final int value) {
    final Quantity<Volume> consumedGas = volumeQuantityFactory.create(value, cubicDeciMeter);
    final GasVolumeReading gasVolumeReading2 =
        assertDoesNotThrow(() -> gasVolumeReadingFactory.create(meter2, localDateTime2, consumedGas));
    assertEquals(consumedGas, gasVolumeReading2.getConsumedGas());
  }

  @Test
  void testConsumedGasJoule() {
    final Unit<Volume> cubicMeter = cubicDeciMeter.multiply(1000);
    final Quantity<Volume> consumedGas = volumeQuantityFactory.create(1, cubicMeter);
    final GasVolumeReading gasVolumeReading2 =
        assertDoesNotThrow(() -> gasVolumeReadingFactory.create(meter2, localDateTime2, consumedGas));
    final Quantity<Volume> consumedGas3 = gasVolumeReading2.getConsumedGas();
    assertAll( //
        () -> assertEquals(cubicDeciMeter, consumedGas3.getUnit()), //
        () -> assertEquals(1000, consumedGas3.getValue().intValue()) //
    );
  }

  @Test
  void testFind() {
    final Optional<? extends GasVolumeReading> foundGasVolumeReading =
        gasVolumeReadingFactory.find(meter1, localDateTime1);
    assertAll( //
        () -> assertNotNull(foundGasVolumeReading), //
        () -> assertTrue(foundGasVolumeReading.isPresent()), //
        () -> assertEquals(gasVolumeReading, foundGasVolumeReading.get()) //
    );
  }

  @Test
  void testIdGenerated() {
    final Integer id1 = gasVolumeReading.getId();
    assertNotNull(id1);

    final GasVolumeReading gasVolumeReading2 =
        assertDoesNotThrow(() -> gasVolumeReadingFactory.create(meter2, localDateTime2, consumedGas2));
    assertNotNull(gasVolumeReading2);
    final Integer id2 = gasVolumeReading2.getId();
    assertNotNull(id2);

    assertNotEquals(id1, id2);
  }

  @Test
  void testLocalDateTime() {
    final LocalDateTime localDateTime = gasVolumeReading.getLocalDateTime();
    assertEquals(localDateTime1, localDateTime);
  }

  @ParameterizedTest
  @MethodSource("nl.vsmeets.amr.test.backend.database.DatabaseConstants#validLocalDateTimeValues")
  void testLocalDateTime(final LocalDateTime localDateTime) {
    final GasVolumeReading gasVolumeReading2 =
        assertDoesNotThrow(() -> gasVolumeReadingFactory.create(meter2, localDateTime, consumedGas2));
    assertEquals(localDateTime, gasVolumeReading2.getLocalDateTime());
  }

  @Test
  void testLocalDateTimeNotNull() {
    assertThrows(NullPointerException.class, () -> gasVolumeReadingFactory.create(meter2, null, consumedGas2));
  }

  @Test
  void testMeter() {
    final Meter meter = gasVolumeReading.getMeter();
    assertAll( //
        () -> assertEquals(meter1, meter), //
        () -> assertTrue(meter.getGasVolumeReadings().contains(gasVolumeReading)) //
    );
  }

  @Test
  void testMeterNotNull() {
    assertThrows(NullPointerException.class, () -> gasVolumeReadingFactory.create(null, localDateTime2, consumedGas2));
  }

  @Test
  void testUnique() {
    assertAll( //
        () -> assertThrows(ConstraintViolationException.class,
            () -> gasVolumeReadingFactory.create(meter1, localDateTime1, consumedGas1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> gasVolumeReadingFactory.create(meter1, localDateTime1, consumedGas2)), //
        () -> assertDoesNotThrow(() -> gasVolumeReadingFactory.create(meter2, localDateTime1, consumedGas1)), //
        () -> assertDoesNotThrow(() -> gasVolumeReadingFactory.create(meter1, localDateTime2, consumedGas1)) //
    );
  }

}
