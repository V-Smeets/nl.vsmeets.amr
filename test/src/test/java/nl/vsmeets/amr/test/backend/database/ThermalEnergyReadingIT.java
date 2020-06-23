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

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Energy;
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
import nl.vsmeets.amr.backend.database.MeasuredMedium;
import nl.vsmeets.amr.backend.database.MeasuredMediumFactory;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.MeterFactory;
import nl.vsmeets.amr.backend.database.P1Telegram;
import nl.vsmeets.amr.backend.database.P1TelegramFactory;
import nl.vsmeets.amr.backend.database.Site;
import nl.vsmeets.amr.backend.database.SiteFactory;
import nl.vsmeets.amr.backend.database.ThermalEnergyReading;
import nl.vsmeets.amr.backend.database.ThermalEnergyReadingFactory;

/**
 * Integration tests for {@link ThermalEnergyReading}.
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
class ThermalEnergyReadingIT {

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
  private static final int consumedEnergyValue1 = 0;
  private static final int consumedEnergyValue2 = 999_999;

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
  private QuantityFactory<Energy> energyQuantityFactory;
  @Autowired
  @Qualifier("megaJoule")
  private Unit<Energy> megaJoule;

  private Quantity<Energy> consumedEnergy1;
  private Quantity<Energy> consumedEnergy2;

  @Autowired
  private ThermalEnergyReadingFactory thermalEnergyReadingFactory;
  private ThermalEnergyReading thermalEnergyReading;

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

    consumedEnergy1 = energyQuantityFactory.create(consumedEnergyValue1, megaJoule);
    consumedEnergy2 = energyQuantityFactory.create(consumedEnergyValue2, megaJoule);

    thermalEnergyReading =
        assertDoesNotThrow(() -> thermalEnergyReadingFactory.create(meter1, localDateTime1, consumedEnergy1));
  }

  @Test
  void testConsumedEnergy() {
    final Quantity<Energy> consumedEnergy = thermalEnergyReading.getConsumedEnergy();
    assertEquals(consumedEnergy1, consumedEnergy);
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 999_999 })
  void testConsumedEnergy(final int value) {
    final Quantity<Energy> consumedEnergy = energyQuantityFactory.create(value, megaJoule);
    final ThermalEnergyReading thermalEnergyReading2 =
        assertDoesNotThrow(() -> thermalEnergyReadingFactory.create(meter2, localDateTime2, consumedEnergy));
    assertEquals(consumedEnergy, thermalEnergyReading2.getConsumedEnergy());
  }

  @Test
  void testConsumedEnergyGigaJoule() {
    final Unit<Energy> gigaJoule = megaJoule.multiply(1000);
    final Quantity<Energy> consumedEnergy = energyQuantityFactory.create(1, gigaJoule);
    final ThermalEnergyReading thermalEnergyReading2 =
        assertDoesNotThrow(() -> thermalEnergyReadingFactory.create(meter2, localDateTime2, consumedEnergy));
    final Quantity<Energy> consumedEnergy3 = thermalEnergyReading2.getConsumedEnergy();
    assertAll( //
        () -> assertEquals(megaJoule, consumedEnergy3.getUnit()), //
        () -> assertEquals(1000, consumedEnergy3.getValue().intValue()) //
    );
  }

  @Test
  void testFind() {
    final Optional<? extends ThermalEnergyReading> foundThermalEnergyReading =
        thermalEnergyReadingFactory.find(meter1, localDateTime1);
    assertAll( //
        () -> assertNotNull(foundThermalEnergyReading), //
        () -> assertTrue(foundThermalEnergyReading.isPresent()), //
        () -> assertEquals(thermalEnergyReading, foundThermalEnergyReading.get()) //
    );
  }

  @Test
  void testIdGenerated() {
    final Integer id1 = thermalEnergyReading.getId();
    assertNotNull(id1);

    final ThermalEnergyReading thermalEnergyReading2 =
        assertDoesNotThrow(() -> thermalEnergyReadingFactory.create(meter2, localDateTime2, consumedEnergy2));
    assertNotNull(thermalEnergyReading2);
    final Integer id2 = thermalEnergyReading2.getId();
    assertNotNull(id2);

    assertNotEquals(id1, id2);
  }

  @Test
  void testLocalDateTime() {
    final LocalDateTime localDateTime = thermalEnergyReading.getLocalDateTime();
    assertEquals(localDateTime1, localDateTime);
  }

  @ParameterizedTest
  @MethodSource("nl.vsmeets.amr.test.backend.database.DatabaseConstants#validLocalDateTimeValues")
  void testLocalDateTime(final LocalDateTime localDateTime) {
    final ThermalEnergyReading thermalEnergyReading2 =
        assertDoesNotThrow(() -> thermalEnergyReadingFactory.create(meter2, localDateTime, consumedEnergy2));
    assertEquals(localDateTime, thermalEnergyReading2.getLocalDateTime());
  }

  @Test
  void testLocalDateTimeNotNull() {
    assertThrows(NullPointerException.class, () -> thermalEnergyReadingFactory.create(meter2, null, consumedEnergy2));
  }

  @Test
  void testMeter() {
    final Meter meter = thermalEnergyReading.getMeter();
    assertAll( //
        () -> assertEquals(meter1, meter), //
        () -> assertTrue(meter.getThermalEnergyReadings().contains(thermalEnergyReading)) //
    );
  }

  @Test
  void testMeterNotNull() {
    assertThrows(NullPointerException.class,
        () -> thermalEnergyReadingFactory.create(null, localDateTime2, consumedEnergy2));
  }

  @Test
  void testUnique() {
    assertAll( //
        () -> assertThrows(ConstraintViolationException.class,
            () -> thermalEnergyReadingFactory.create(meter1, localDateTime1, consumedEnergy1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> thermalEnergyReadingFactory.create(meter1, localDateTime1, consumedEnergy2)), //
        () -> assertDoesNotThrow(() -> thermalEnergyReadingFactory.create(meter2, localDateTime1, consumedEnergy1)), //
        () -> assertDoesNotThrow(() -> thermalEnergyReadingFactory.create(meter1, localDateTime2, consumedEnergy1)) //
    );
  }

}
