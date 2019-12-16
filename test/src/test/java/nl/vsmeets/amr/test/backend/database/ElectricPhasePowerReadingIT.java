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

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.ElectricPotential;
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
import nl.vsmeets.amr.backend.database.ElectricPhasePowerReading;
import nl.vsmeets.amr.backend.database.ElectricPhasePowerReadingFactory;
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
import nl.vsmeets.amr.libs.junit.RandomShortGenerator;
import nl.vsmeets.amr.libs.junit.RandomStringGenerator;
import nl.vsmeets.amr.libs.junit.RandomZoneIdGenerator;

/**
 * Integration tests for {@link ElectricPhasePowerReading}.
 *
 * @author vincent
 */
@ContextConfiguration(classes = { BackendDatabaseConfig.class })
@DataJpaTest
class ElectricPhasePowerReadingIT implements RandomByteGenerator, RandomLocalDateTimeGenerator, RandomShortGenerator,
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
  private QuantityFactory<ElectricPotential> electricPotentialQuantityFactory;
  @Autowired
  @Qualifier("deciVolt")
  private Unit<ElectricPotential> deciVolt;

  @Autowired
  private QuantityFactory<ElectricCurrent> electricCurrentQuantityFactory;
  @Autowired
  @Qualifier("ampere")
  private Unit<ElectricCurrent> ampere;

  @Autowired
  private QuantityFactory<Power> powerQuantityFactory;
  @Autowired
  @Qualifier("watt")
  private Unit<Power> watt;

  private final LocalDateTime localDateTime1 = randomLocalDateTimeZeroPrecisionRange(
      DatabaseConstants.MIN_LOCAL_DATE_TIME, DatabaseConstants.MAX_LOCAL_DATE_TIME);
  private final LocalDateTime localDateTime2 = randomLocalDateTimeZeroPrecisionRange(
      DatabaseConstants.MIN_LOCAL_DATE_TIME, DatabaseConstants.MAX_LOCAL_DATE_TIME, localDateTime1);
  private final Byte phaseNumber1 = randomByte();
  private final Byte phaseNumber2 = randomByte(phaseNumber1);
  private Quantity<ElectricPotential> instantaneousVoltage1;
  private Quantity<ElectricPotential> instantaneousVoltage2;
  private Quantity<ElectricCurrent> instantaneousCurrent1;
  private Quantity<ElectricCurrent> instantaneousCurrent2;
  private Quantity<Power> instantaneousConsumedPower1;
  private Quantity<Power> instantaneousConsumedPower2;
  private Quantity<Power> instantaneousProducedPower1;
  private Quantity<Power> instantaneousProducedPower2;

  @Autowired
  private ElectricPhasePowerReadingFactory electricPhasePowerReadingFactory;
  private ElectricPhasePowerReading electricPhasePowerReading;

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

    instantaneousVoltage1 =
        electricPotentialQuantityFactory.create(randomShortRange((short) 0, (short) 10_000), deciVolt);
    instantaneousVoltage2 = electricPotentialQuantityFactory
        .create(randomShortRange((short) 0, (short) 10_000, instantaneousVoltage1.getValue().shortValue()), deciVolt);
    instantaneousCurrent1 = electricCurrentQuantityFactory.create(randomShortRange((short) 0, (short) 1_000), ampere);
    instantaneousCurrent2 = electricCurrentQuantityFactory
        .create(randomShortRange((short) 0, (short) 1_000, instantaneousCurrent1.getValue().shortValue()), ampere);
    instantaneousConsumedPower1 = powerQuantityFactory.create(randomIntRange(0, 100_000), watt);
    instantaneousConsumedPower2 = powerQuantityFactory
        .create(randomIntRange(0, 100_000, instantaneousConsumedPower1.getValue().intValue()), watt);
    instantaneousProducedPower1 = powerQuantityFactory.create(randomIntRange(0, 100_000), watt);
    instantaneousProducedPower2 = powerQuantityFactory
        .create(randomIntRange(0, 100_000, instantaneousProducedPower1.getValue().intValue()), watt);

    electricPhasePowerReading =
        assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter1, localDateTime1, phaseNumber1,
            instantaneousVoltage1, instantaneousCurrent1, instantaneousConsumedPower1, instantaneousProducedPower1));
  }

  @Test
  void testFind() {
    final Optional<? extends ElectricPhasePowerReading> foundElectricPhasePowerReading =
        electricPhasePowerReadingFactory.find(meter1, localDateTime1, phaseNumber1);
    assertAll( //
        () -> assertNotNull(foundElectricPhasePowerReading), //
        () -> assertTrue(foundElectricPhasePowerReading.isPresent()), //
        () -> assertEquals(electricPhasePowerReading, foundElectricPhasePowerReading.get()) //
    );
  }

  @Test
  void testIdGenerated() {
    final Integer id1 = electricPhasePowerReading.getId();
    assertNotNull(id1);

    final ElectricPhasePowerReading electricPhasePowerReading2 =
        assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter2, localDateTime2, phaseNumber2,
            instantaneousVoltage2, instantaneousCurrent2, instantaneousConsumedPower2, instantaneousProducedPower2));
    assertNotNull(electricPhasePowerReading2);
    final Integer id2 = electricPhasePowerReading2.getId();
    assertNotNull(id2);

    assertNotEquals(id1, id2);
  }

  @Test
  void testInstantaneousConsumedPower() {
    final Quantity<Power> instantaneousConsumedPower = electricPhasePowerReading.getInstantaneousConsumedPower();
    assertEquals(instantaneousConsumedPower1, instantaneousConsumedPower);
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 999 })
  void testInstantaneousConsumedPower(final int value) {
    final Quantity<Power> instantaneousConsumedPower = powerQuantityFactory.create(value, watt);
    final ElectricPhasePowerReading electricPhasePowerReading2 =
        assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter2, localDateTime2, phaseNumber2,
            instantaneousVoltage2, instantaneousCurrent2, instantaneousConsumedPower, instantaneousProducedPower2));
    assertEquals(instantaneousConsumedPower, electricPhasePowerReading2.getInstantaneousConsumedPower());
  }

  @Test
  void testInstantaneousConsumedPowerDecaWatt() {
    final Unit<Power> decaWatt = watt.multiply(10);
    final Quantity<Power> instantaneousConsumedPower = powerQuantityFactory.create(1, decaWatt);
    final ElectricPhasePowerReading electricPhasePowerReading2 =
        assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter2, localDateTime2, phaseNumber2,
            instantaneousVoltage2, instantaneousCurrent2, instantaneousConsumedPower, instantaneousProducedPower2));
    final Quantity<Power> instantaneousConsumedPower3 = electricPhasePowerReading2.getInstantaneousConsumedPower();
    assertAll( //
        () -> assertEquals(watt, instantaneousConsumedPower3.getUnit()), //
        () -> assertEquals(10, instantaneousConsumedPower3.getValue().intValue()) //
    );
  }

  @Test
  void testInstantaneousConsumedPowerNotNull() {
    assertThrows(NullPointerException.class, () -> electricPhasePowerReadingFactory.create(meter2, localDateTime2,
        phaseNumber2, instantaneousVoltage2, instantaneousCurrent2, null, instantaneousProducedPower2));
  }

  @Test
  void testInstantaneousCurrent() {
    final Quantity<ElectricCurrent> instantaneousCurrent = electricPhasePowerReading.getInstantaneousCurrent();
    assertEquals(instantaneousCurrent1, instantaneousCurrent);
  }

  @ParameterizedTest
  @ValueSource(shorts = { 0, 1, 999 })
  void testInstantaneousCurrent(final short value) {
    final Quantity<ElectricCurrent> instantaneousCurrent = electricCurrentQuantityFactory.create(value, ampere);
    final ElectricPhasePowerReading electricPhasePowerReading2 =
        assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter2, localDateTime2, phaseNumber2,
            instantaneousVoltage2, instantaneousCurrent, instantaneousConsumedPower2, instantaneousProducedPower2));
    assertEquals(instantaneousCurrent, electricPhasePowerReading2.getInstantaneousCurrent());
  }

  @Test
  void testInstantaneousCurrentDecaAmpere() {
    final Unit<ElectricCurrent> decaAmpere = ampere.multiply(10);
    final Quantity<ElectricCurrent> instantaneousCurrent = electricCurrentQuantityFactory.create(1, decaAmpere);
    final ElectricPhasePowerReading electricPhasePowerReading2 =
        assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter2, localDateTime2, phaseNumber2,
            instantaneousVoltage2, instantaneousCurrent, instantaneousConsumedPower2, instantaneousProducedPower2));
    final Quantity<ElectricCurrent> instantaneousCurrent3 = electricPhasePowerReading2.getInstantaneousCurrent();
    assertAll( //
        () -> assertEquals(ampere, instantaneousCurrent3.getUnit()), //
        () -> assertEquals(10, instantaneousCurrent3.getValue().shortValue()) //
    );
  }

  @Test
  void testInstantaneousCurrentNotNull() {
    assertThrows(NullPointerException.class, () -> electricPhasePowerReadingFactory.create(meter2, localDateTime2,
        phaseNumber2, instantaneousVoltage2, null, instantaneousConsumedPower2, instantaneousProducedPower2));
  }

  @Test
  void testInstantaneousProducedPower() {
    final Quantity<Power> instantaneousProducedPower = electricPhasePowerReading.getInstantaneousProducedPower();
    assertEquals(instantaneousProducedPower1, instantaneousProducedPower);
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 999 })
  void testInstantaneousProducedPower(final int value) {
    final Quantity<Power> instantaneousProducedPower = powerQuantityFactory.create(value, watt);
    final ElectricPhasePowerReading electricPhasePowerReading2 =
        assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter2, localDateTime2, phaseNumber2,
            instantaneousVoltage2, instantaneousCurrent2, instantaneousConsumedPower2, instantaneousProducedPower));
    assertEquals(instantaneousProducedPower, electricPhasePowerReading2.getInstantaneousProducedPower());
  }

  @Test
  void testInstantaneousProducedPowerDecaWatt() {
    final Unit<Power> decaWatt = watt.multiply(10);
    final Quantity<Power> instantaneousProducedPower = powerQuantityFactory.create(1, decaWatt);
    final ElectricPhasePowerReading electricPhasePowerReading2 =
        assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter2, localDateTime2, phaseNumber2,
            instantaneousVoltage2, instantaneousCurrent2, instantaneousConsumedPower2, instantaneousProducedPower));
    final Quantity<Power> instantaneousProducedPower3 = electricPhasePowerReading2.getInstantaneousProducedPower();
    assertAll( //
        () -> assertEquals(watt, instantaneousProducedPower3.getUnit()), //
        () -> assertEquals(10, instantaneousProducedPower3.getValue().intValue()) //
    );
  }

  @Test
  void testInstantaneousProducedPowerNotNull() {
    assertThrows(NullPointerException.class, () -> electricPhasePowerReadingFactory.create(meter2, localDateTime2,
        phaseNumber2, instantaneousVoltage2, instantaneousCurrent2, instantaneousConsumedPower2, null));
  }

  @Test
  void testInstantaneousVoltage() {
    final Quantity<ElectricPotential> instantaneousVoltage = electricPhasePowerReading.getInstantaneousVoltage();
    assertEquals(instantaneousVoltage1, instantaneousVoltage);
  }

  @ParameterizedTest
  @ValueSource(shorts = { 0, 1, 9_999 })
  void testInstantaneousVoltage(final short value) {
    final Quantity<ElectricPotential> instantaneousVoltage = electricPotentialQuantityFactory.create(value, deciVolt);
    final ElectricPhasePowerReading electricPhasePowerReading2 =
        assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter2, localDateTime2, phaseNumber2,
            instantaneousVoltage, instantaneousCurrent2, instantaneousConsumedPower2, instantaneousProducedPower2));
    assertEquals(instantaneousVoltage, electricPhasePowerReading2.getInstantaneousVoltage());
  }

  @Test
  void testInstantaneousVoltageNotNull() {
    assertThrows(NullPointerException.class, () -> electricPhasePowerReadingFactory.create(meter2, localDateTime2,
        phaseNumber2, null, instantaneousCurrent2, instantaneousConsumedPower2, instantaneousProducedPower2));
  }

  @Test
  void testInstantaneousVoltageVolt() {
    final Unit<ElectricPotential> volt = deciVolt.multiply(10);
    final Quantity<ElectricPotential> instantaneousVoltage = electricPotentialQuantityFactory.create(1, volt);
    final ElectricPhasePowerReading electricPhasePowerReading2 =
        assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter2, localDateTime2, phaseNumber2,
            instantaneousVoltage, instantaneousCurrent2, instantaneousConsumedPower2, instantaneousProducedPower2));
    final Quantity<ElectricPotential> instantaneousVoltage3 = electricPhasePowerReading2.getInstantaneousVoltage();
    assertAll( //
        () -> assertEquals(deciVolt, instantaneousVoltage3.getUnit()), //
        () -> assertEquals(10, instantaneousVoltage3.getValue().shortValue()) //
    );
  }

  @Test
  void testLocalDateTime() {
    final LocalDateTime localDateTime = electricPhasePowerReading.getLocalDateTime();
    assertEquals(localDateTime1, localDateTime);
  }

  @ParameterizedTest
  @MethodSource("nl.vsmeets.amr.test.backend.database.DatabaseConstants#validLocalDateTimeValues")
  void testLocalDateTime(final LocalDateTime localDateTime) {
    final ElectricPhasePowerReading electricPhasePowerReading2 =
        assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter2, localDateTime, phaseNumber2,
            instantaneousVoltage2, instantaneousCurrent2, instantaneousConsumedPower2, instantaneousProducedPower2));
    assertEquals(localDateTime, electricPhasePowerReading2.getLocalDateTime());
  }

  @Test
  void testLocalDateTimeNotNull() {
    assertThrows(NullPointerException.class, () -> electricPhasePowerReadingFactory.create(meter2, null, phaseNumber2,
        instantaneousVoltage2, instantaneousCurrent2, instantaneousConsumedPower2, instantaneousProducedPower2));
  }

  @Test
  void testMeter() {
    final Meter meter = electricPhasePowerReading.getMeter();
    assertAll( //
        () -> assertEquals(meter1, meter), //
        () -> assertTrue(meter.getElectricPhasePowerReadings().contains(electricPhasePowerReading)) //
    );
  }

  @Test
  void testMeterNotNull() {
    assertThrows(NullPointerException.class,
        () -> electricPhasePowerReadingFactory.create(null, localDateTime2, phaseNumber2, instantaneousVoltage2,
            instantaneousCurrent2, instantaneousConsumedPower2, instantaneousProducedPower2));
  }

  @Test
  void testPhaseNumber() {
    final Byte phaseNumber = electricPhasePowerReading.getPhaseNumber();
    assertEquals(phaseNumber1, phaseNumber);
  }

  @Test
  void testPhaseNumberNotNull() {
    assertThrows(NullPointerException.class, () -> electricPhasePowerReadingFactory.create(meter2, localDateTime2, null,
        instantaneousVoltage2, instantaneousCurrent2, instantaneousConsumedPower2, instantaneousProducedPower2));
  }

  @Test
  void testUnique() {
    assertAll( //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricPhasePowerReadingFactory.create(meter1, localDateTime1, phaseNumber1, instantaneousVoltage1,
                instantaneousCurrent1, instantaneousConsumedPower1, instantaneousProducedPower1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricPhasePowerReadingFactory.create(meter1, localDateTime1, phaseNumber1, instantaneousVoltage2,
                instantaneousCurrent1, instantaneousConsumedPower1, instantaneousProducedPower1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricPhasePowerReadingFactory.create(meter1, localDateTime1, phaseNumber1, instantaneousVoltage1,
                instantaneousCurrent2, instantaneousConsumedPower1, instantaneousProducedPower1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricPhasePowerReadingFactory.create(meter1, localDateTime1, phaseNumber1, instantaneousVoltage1,
                instantaneousCurrent1, instantaneousConsumedPower2, instantaneousProducedPower1)), //
        () -> assertThrows(ConstraintViolationException.class,
            () -> electricPhasePowerReadingFactory.create(meter1, localDateTime1, phaseNumber1, instantaneousVoltage1,
                instantaneousCurrent1, instantaneousConsumedPower1, instantaneousProducedPower2)), //
        () -> assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter2, localDateTime1, phaseNumber1,
            instantaneousVoltage1, instantaneousCurrent1, instantaneousConsumedPower1, instantaneousProducedPower1)), //
        () -> assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter1, localDateTime2, phaseNumber1,
            instantaneousVoltage1, instantaneousCurrent1, instantaneousConsumedPower1, instantaneousProducedPower1)), //
        () -> assertDoesNotThrow(() -> electricPhasePowerReadingFactory.create(meter1, localDateTime1, phaseNumber2,
            instantaneousVoltage1, instantaneousCurrent1, instantaneousConsumedPower1, instantaneousProducedPower1)) //
    );
  }

}
