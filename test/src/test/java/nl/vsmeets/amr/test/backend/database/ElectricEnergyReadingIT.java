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
import nl.vsmeets.amr.backend.database.ElectricEnergyReading;
import nl.vsmeets.amr.backend.database.ElectricEnergyReadingFactory;
import nl.vsmeets.amr.backend.database.MeasuredMedium;
import nl.vsmeets.amr.backend.database.MeasuredMediumFactory;
import nl.vsmeets.amr.backend.database.Meter;
import nl.vsmeets.amr.backend.database.MeterFactory;
import nl.vsmeets.amr.backend.database.P1Telegram;
import nl.vsmeets.amr.backend.database.P1TelegramFactory;
import nl.vsmeets.amr.backend.database.Site;
import nl.vsmeets.amr.backend.database.SiteFactory;

/**
 * Integration tests for {@link ElectricEnergyReading}.
 *
 * @author vincent
 */
@ContextConfiguration(classes = { BackendDatabaseConfig.class })
@DataJpaTest
class ElectricEnergyReadingIT {

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
  private static final short tariffIndicator1 = 0x0000;
  private static final short tariffIndicator2 = -1; // 0xFFFF;
  private static final int consumedEnergyValue1 = 0;
  private static final int consumedEnergyValue2 = 999_999_999;
  private static final int producedEnergyValue1 = 0;
  private static final int producedEnergyValue2 = 999_999_999;

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
  @Qualifier("joule")
  private Unit<Energy> joule;
  @Autowired
  @Qualifier("wattHour")
  private Unit<Energy> wattHour;

  private Quantity<Energy> consumedEnergy1;
  private Quantity<Energy> consumedEnergy2;
  private Quantity<Energy> producedEnergy1;
  private Quantity<Energy> producedEnergy2;

  @Autowired
  private ElectricEnergyReadingFactory electricEnergyReadingFactory;
  private ElectricEnergyReading electricEnergyReading;

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

    consumedEnergy1 = energyQuantityFactory.create(consumedEnergyValue1, wattHour);
    consumedEnergy2 = energyQuantityFactory.create(consumedEnergyValue2, wattHour);
    producedEnergy1 = energyQuantityFactory.create(producedEnergyValue1, wattHour);
    producedEnergy2 = energyQuantityFactory.create(producedEnergyValue2, wattHour);

    electricEnergyReading = assertDoesNotThrow(() -> electricEnergyReadingFactory.create(meter1, localDateTime1,
        tariffIndicator1, consumedEnergy1, producedEnergy1));
  }

  @Test
  void testConsumedEnergy() {
    final Quantity<Energy> consumedEnergy = electricEnergyReading.getConsumedEnergy();
    assertEquals(consumedEnergy1, consumedEnergy);
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 999_999_999 })
  void testConsumedEnergy(final int value) {
    final Quantity<Energy> consumedEnergy = energyQuantityFactory.create(value, wattHour);
    final ElectricEnergyReading electricEnergyReading2 = assertDoesNotThrow(() -> electricEnergyReadingFactory
        .create(meter2, localDateTime2, tariffIndicator2, consumedEnergy, producedEnergy2));
    assertEquals(consumedEnergy, electricEnergyReading2.getConsumedEnergy());
  }

  @Test
  void testConsumedEnergyJoule() {
    final Quantity<Energy> consumedEnergy = energyQuantityFactory.create(3600, joule);
    final ElectricEnergyReading electricEnergyReading2 = assertDoesNotThrow(() -> electricEnergyReadingFactory
        .create(meter2, localDateTime2, tariffIndicator2, consumedEnergy, producedEnergy2));
    final Quantity<Energy> consumedEnergyWh = electricEnergyReading2.getConsumedEnergy();
    assertAll( //
        () -> assertEquals(wattHour, consumedEnergyWh.getUnit()), //
        () -> assertEquals(1, consumedEnergyWh.getValue().intValue()) //
    );
  }

  @Test
  void testConsumedEnergyNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricEnergyReadingFactory.create(meter2, localDateTime2, tariffIndicator2, null, producedEnergy2));
  }

  @Test
  void testFind() {
    final Optional<? extends ElectricEnergyReading> foundElectricEnergyReading =
        electricEnergyReadingFactory.find(meter1, localDateTime1, tariffIndicator1);
    assertAll( //
        () -> assertNotNull(foundElectricEnergyReading), //
        () -> assertTrue(foundElectricEnergyReading.isPresent()), //
        () -> assertEquals(electricEnergyReading, foundElectricEnergyReading.get()) //
    );
  }

  @Test
  void testIdGenerated() {
    final Integer id1 = electricEnergyReading.getId();
    assertNotNull(id1);

    final ElectricEnergyReading electricEnergyReading2 = assertDoesNotThrow(() -> electricEnergyReadingFactory
        .create(meter2, localDateTime2, tariffIndicator2, consumedEnergy2, producedEnergy2));
    assertNotNull(electricEnergyReading2);
    final Integer id2 = electricEnergyReading2.getId();
    assertNotNull(id2);

    assertNotEquals(id1, id2);
  }

  @Test
  void testLocalDateTime() {
    final LocalDateTime localDateTime = electricEnergyReading.getLocalDateTime();
    assertEquals(localDateTime1, localDateTime);
  }

  @ParameterizedTest
  @MethodSource("nl.vsmeets.amr.test.backend.database.DatabaseConstants#validLocalDateTimeValues")
  void testLocalDateTime(final LocalDateTime localDateTime) {
    final ElectricEnergyReading electricEnergyReading2 = assertDoesNotThrow(() -> electricEnergyReadingFactory
        .create(meter2, localDateTime, tariffIndicator2, consumedEnergy2, producedEnergy2));
    assertEquals(localDateTime, electricEnergyReading2.getLocalDateTime());
  }

  @Test
  void testLocalDateTimeNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricEnergyReadingFactory.create(meter2, null, tariffIndicator2, consumedEnergy2, producedEnergy2));
  }

  @Test
  void testMeter() {
    final Meter meter = electricEnergyReading.getMeter();
    assertAll( //
        () -> assertEquals(meter1, meter), //
        () -> assertTrue(meter.getElectricEnergyReadings().contains(electricEnergyReading)) //
    );
  }

  @Test
  void testMeterNotNull() {
    assertThrows(NullPointerException.class, () -> electricEnergyReadingFactory.create(null, localDateTime2,
        tariffIndicator2, consumedEnergy2, producedEnergy2));
  }

  @Test
  void testProducedEnergy() {
    final Quantity<Energy> producedEnergy = electricEnergyReading.getProducedEnergy();
    assertEquals(producedEnergy1, producedEnergy);
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 999_999_999 })
  void testProducedEnergy(final int value) {
    final Quantity<Energy> producedEnergy = energyQuantityFactory.create(value, wattHour);
    final ElectricEnergyReading electricEnergyReading2 = assertDoesNotThrow(() -> electricEnergyReadingFactory
        .create(meter2, localDateTime2, tariffIndicator2, consumedEnergy2, producedEnergy));
    assertEquals(producedEnergy, electricEnergyReading2.getProducedEnergy());
  }

  @Test
  void testProducedEnergyJoule() {
    final Quantity<Energy> producedEnergy = energyQuantityFactory.create(3600, joule);
    final ElectricEnergyReading electricEnergyReading2 = assertDoesNotThrow(() -> electricEnergyReadingFactory
        .create(meter2, localDateTime2, tariffIndicator2, consumedEnergy2, producedEnergy));
    final Quantity<Energy> producedEnergyWh = electricEnergyReading2.getProducedEnergy();
    assertAll( //
        () -> assertEquals(wattHour, producedEnergyWh.getUnit()), //
        () -> assertEquals(1, producedEnergyWh.getValue().intValue()) //
    );
  }

  @Test
  void testProducedEnergyNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricEnergyReadingFactory.create(meter2, localDateTime2, tariffIndicator2, consumedEnergy2, null));
  }

  @Test
  void testTariffIndicator() {
    final Short tariffIndicator = electricEnergyReading.getTariffIndicator();
    assertEquals(tariffIndicator1, tariffIndicator);
  }

  @Test
  void testTariffIndicatorNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricEnergyReadingFactory.create(meter2, localDateTime2, null, consumedEnergy2, producedEnergy2));
  }

  @Test
  void testUnique() {
    assertAll( //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricEnergyReadingFactory.create(meter1, localDateTime1, tariffIndicator1, consumedEnergy1,
                producedEnergy1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricEnergyReadingFactory.create(meter1, localDateTime1, tariffIndicator1, consumedEnergy2,
                producedEnergy1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricEnergyReadingFactory.create(meter1, localDateTime1, tariffIndicator1, consumedEnergy1,
                producedEnergy2)), //
        () -> assertDoesNotThrow(() -> electricEnergyReadingFactory.create(meter2, localDateTime1, tariffIndicator1,
            consumedEnergy1, producedEnergy1)), //
        () -> assertDoesNotThrow(() -> electricEnergyReadingFactory.create(meter1, localDateTime2, tariffIndicator1,
            consumedEnergy1, producedEnergy1)), //
        () -> assertDoesNotThrow(() -> electricEnergyReadingFactory.create(meter1, localDateTime1, tariffIndicator2,
            consumedEnergy1, producedEnergy1)) //
    );
  }

}
