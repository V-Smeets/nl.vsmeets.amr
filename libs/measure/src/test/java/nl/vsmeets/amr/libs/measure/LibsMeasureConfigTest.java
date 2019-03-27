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
package nl.vsmeets.amr.libs.measure;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import javax.measure.MetricPrefix;
import javax.measure.Unit;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.ElectricPotential;
import javax.measure.quantity.Energy;
import javax.measure.quantity.Length;
import javax.measure.quantity.Power;
import javax.measure.quantity.Time;
import javax.measure.quantity.Volume;
import javax.measure.spi.QuantityFactory;
import javax.measure.spi.ServiceProvider;
import javax.measure.spi.SystemOfUnits;
import javax.measure.spi.SystemOfUnitsService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link LibsMeasureConfig}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class LibsMeasureConfigTest {

  /**
   * The object under test.
   */
  private final LibsMeasureConfig testObject = new LibsMeasureConfig();

  @Test
  void testAmpere(@Mock final SystemOfUnits systemOfUnits, @Mock final Unit<ElectricCurrent> ampere) {
    when(ampere.asType(ElectricCurrent.class)).then(i -> ampere);
    when(systemOfUnits.getUnit("A")).then(i -> ampere);

    assertEquals(ampere, testObject.ampere(systemOfUnits));
  }

  @Test
  void testCubicDeciMeter(@Mock final Unit<Length> meter, @Mock final Unit<Length> deciMeter,
      @Mock final Unit<Volume> cubicDeciMeter) {
    when(cubicDeciMeter.asType(Volume.class)).then(i -> cubicDeciMeter);
    when(deciMeter.pow(3)).then(i -> cubicDeciMeter);
    when(meter.prefix(MetricPrefix.DECI)).then(i -> deciMeter);

    assertEquals(cubicDeciMeter, testObject.cubicDeciMeter(meter));
  }

  @Test
  void testDeciVolt(@Mock final Unit<ElectricPotential> volt, @Mock final Unit<ElectricPotential> deciVolt) {
    when(volt.prefix(MetricPrefix.DECI)).then(i -> deciVolt);

    assertEquals(deciVolt, testObject.deciVolt(volt));
  }

  @Test
  void testElectricCurrentQuantityFactory(@Mock final ServiceProvider serviceProvider,
      @Mock final QuantityFactory<ElectricCurrent> electricCurrentQuantityFactory) {
    when(serviceProvider.getQuantityFactory(ElectricCurrent.class)).then(i -> electricCurrentQuantityFactory);

    assertEquals(electricCurrentQuantityFactory, testObject.electricCurrentQuantityFactory(serviceProvider));
  }

  @Test
  void testElectricPotentialQuantityFactory(@Mock final ServiceProvider serviceProvider,
      @Mock final QuantityFactory<ElectricPotential> electricPotentialQuantityFactory) {
    when(serviceProvider.getQuantityFactory(ElectricPotential.class)).then(i -> electricPotentialQuantityFactory);

    assertEquals(electricPotentialQuantityFactory, testObject.electricPotentialQuantityFactory(serviceProvider));
  }

  @Test
  void testEnergyQuantityFactory(@Mock final ServiceProvider serviceProvider,
      @Mock final QuantityFactory<Energy> energyQuantityFactory) {
    when(serviceProvider.getQuantityFactory(Energy.class)).then(i -> energyQuantityFactory);

    assertEquals(energyQuantityFactory, testObject.energyQuantityFactory(serviceProvider));
  }

  @Test
  void testHour(@Mock final SystemOfUnits systemOfUnits, @Mock final Unit<Time> hour) {
    when(hour.asType(Time.class)).then(i -> hour);
    when(systemOfUnits.getUnit("h")).then(i -> hour);

    assertEquals(hour, testObject.hour(systemOfUnits));
  }

  @Test
  void testJoule(@Mock final SystemOfUnits systemOfUnits, @Mock final Unit<Energy> joule) {
    when(joule.asType(Energy.class)).then(i -> joule);
    when(systemOfUnits.getUnit("J")).then(i -> joule);

    assertEquals(joule, testObject.joule(systemOfUnits));
  }

  @Test
  void testMegaJoule(@Mock final Unit<Energy> joule, @Mock final Unit<Energy> megaJoule) {
    when(joule.prefix(MetricPrefix.MEGA)).then(i -> megaJoule);

    assertEquals(megaJoule, testObject.megaJoule(joule));
  }

  @Test
  void testMeter(@Mock final SystemOfUnits systemOfUnits, @Mock final Unit<Length> meter) {
    when(meter.asType(Length.class)).then(i -> meter);
    when(systemOfUnits.getUnit("m")).then(i -> meter);

    assertEquals(meter, testObject.meter(systemOfUnits));
  }

  @Test
  void testPowerQuantityFactory(@Mock final ServiceProvider serviceProvider,
      @Mock final QuantityFactory<Power> powerQuantityFactory) {
    when(serviceProvider.getQuantityFactory(Power.class)).then(i -> powerQuantityFactory);

    assertEquals(powerQuantityFactory, testObject.powerQuantityFactory(serviceProvider));
  }

  @Test
  void testServiceProvider() {
    assertNotNull(testObject.serviceProvider());
  }

  @Test
  void testSystemOfUnits(@Mock final SystemOfUnitsService systemOfUnitsService,
      @Mock final SystemOfUnits systemOfUnits) {
    when(systemOfUnitsService.getSystemOfUnits()).then(i -> systemOfUnits);

    assertEquals(systemOfUnits, testObject.systemOfUnits(systemOfUnitsService));
  }

  @Test
  void testSystemOfUnitsService(@Mock final ServiceProvider serviceProvider,
      @Mock final SystemOfUnitsService systemOfUnitsService) {
    when(serviceProvider.getSystemOfUnitsService()).then(i -> systemOfUnitsService);

    assertEquals(systemOfUnitsService, testObject.systemOfUnitsService(serviceProvider));
  }

  @Test
  void testVolt(@Mock final SystemOfUnits systemOfUnits, @Mock final Unit<ElectricPotential> volt) {
    when(volt.asType(ElectricPotential.class)).then(i -> volt);
    when(systemOfUnits.getUnit("V")).then(i -> volt);

    assertEquals(volt, testObject.volt(systemOfUnits));
  }

  @Test
  void testVolumeQuantityFactory(@Mock final ServiceProvider serviceProvider,
      @Mock final QuantityFactory<Volume> volumeQuantityFactory) {
    when(serviceProvider.getQuantityFactory(Volume.class)).then(i -> volumeQuantityFactory);

    assertEquals(volumeQuantityFactory, testObject.volumeQuantityFactory(serviceProvider));
  }

  @Test
  void testWatt(@Mock final SystemOfUnits systemOfUnits, @Mock final Unit<Power> watt) {
    when(watt.asType(Power.class)).then(i -> watt);
    when(systemOfUnits.getUnit("W")).then(i -> watt);

    assertEquals(watt, testObject.watt(systemOfUnits));
  }

  @Test
  void testWattHour(@Mock final Unit<Power> watt, @Mock final Unit<Time> hour, @Mock final Unit<Energy> wattHour) {
    when(wattHour.asType(Energy.class)).then(i -> wattHour);
    when(watt.multiply(hour)).then(i -> wattHour);

    assertEquals(wattHour, testObject.wattHour(watt, hour));
  }

}
