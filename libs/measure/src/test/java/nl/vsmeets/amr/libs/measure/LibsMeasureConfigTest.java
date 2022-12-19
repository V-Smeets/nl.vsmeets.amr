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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    @InjectMocks
    private LibsMeasureConfig libsMeasureConfig;

    @Test
    void testAmpere(@Mock final SystemOfUnits systemOfUnits, @Mock final Unit<?> unit,
            @Mock final Unit<ElectricCurrent> ampere) {
        Mockito.when(systemOfUnits.getUnit("A")).then(i -> unit);
        Mockito.when(unit.asType(ElectricCurrent.class)).then(i -> ampere);

        assertEquals(ampere, libsMeasureConfig.ampere(systemOfUnits));
    }

    @Test
    void testCubicDeciMeter(@Mock final Unit<Length> meter, @Mock final Unit<Length> deciMeter,
            @Mock final Unit<?> unit, @Mock final Unit<Volume> cubicDeciMeter) {
        Mockito.when(meter.prefix(MetricPrefix.DECI)).then(i -> deciMeter);
        Mockito.when(deciMeter.pow(3)).then(i -> unit);
        Mockito.when(unit.asType(Volume.class)).then(i -> cubicDeciMeter);

        assertEquals(cubicDeciMeter, libsMeasureConfig.cubicDeciMeter(meter));
    }

    @Test
    void testDeciVolt(@Mock final Unit<ElectricPotential> volt, @Mock final Unit<ElectricPotential> deciVolt) {
        Mockito.when(volt.prefix(MetricPrefix.DECI)).then(i -> deciVolt);

        assertEquals(deciVolt, libsMeasureConfig.deciVolt(volt));
    }

    @Test
    void testElectricCurrentQuantityFactory(@Mock final ServiceProvider serviceProvider,
            @Mock final QuantityFactory<ElectricCurrent> electricCurrentQuantityFactory) {
        Mockito.when(serviceProvider.getQuantityFactory(ElectricCurrent.class))
                .then(i -> electricCurrentQuantityFactory);

        assertEquals(electricCurrentQuantityFactory, libsMeasureConfig.electricCurrentQuantityFactory(serviceProvider));
    }

    @Test
    void testElectricPotentialQuantityFactory(@Mock final ServiceProvider serviceProvider,
            @Mock final QuantityFactory<ElectricPotential> electricPotentialQuantityFactory) {
        Mockito.when(serviceProvider.getQuantityFactory(ElectricPotential.class))
                .then(i -> electricPotentialQuantityFactory);

        assertEquals(electricPotentialQuantityFactory,
                libsMeasureConfig.electricPotentialQuantityFactory(serviceProvider));
    }

    @Test
    void testEnergyQuantityFactory(@Mock final ServiceProvider serviceProvider,
            @Mock final QuantityFactory<Energy> energyQuantityFactory) {
        Mockito.when(serviceProvider.getQuantityFactory(Energy.class)).then(i -> energyQuantityFactory);

        assertEquals(energyQuantityFactory, libsMeasureConfig.energyQuantityFactory(serviceProvider));
    }

    @Test
    void testHour(@Mock final SystemOfUnits systemOfUnits, @Mock final Unit<?> unit, @Mock final Unit<Time> hour) {
        Mockito.when(systemOfUnits.getUnit("h")).then(i -> unit);
        Mockito.when(unit.asType(Time.class)).then(i -> hour);

        assertEquals(hour, libsMeasureConfig.hour(systemOfUnits));
    }

    @Test
    void testJoule(@Mock final SystemOfUnits systemOfUnits, @Mock final Unit<?> unit, @Mock final Unit<Energy> joule) {
        Mockito.when(systemOfUnits.getUnit("J")).then(i -> unit);
        Mockito.when(unit.asType(Energy.class)).then(i -> joule);

        assertEquals(joule, libsMeasureConfig.joule(systemOfUnits));
    }

    @Test
    void testMegaJoule(@Mock final Unit<Energy> joule, @Mock final Unit<Energy> megaJoule) {
        Mockito.when(joule.prefix(MetricPrefix.MEGA)).then(i -> megaJoule);

        assertEquals(megaJoule, libsMeasureConfig.megaJoule(joule));
    }

    @Test
    void testMeter(@Mock final SystemOfUnits systemOfUnits, @Mock final Unit<?> unit, @Mock final Unit<Length> meter) {
        Mockito.when(systemOfUnits.getUnit("m")).then(i -> unit);
        Mockito.when(unit.asType(Length.class)).then(i -> meter);

        assertEquals(meter, libsMeasureConfig.meter(systemOfUnits));
    }

    @Test
    void testPowerQuantityFactory(@Mock final ServiceProvider serviceProvider,
            @Mock final QuantityFactory<Power> powerQuantityFactory) {
        Mockito.when(serviceProvider.getQuantityFactory(Power.class)).then(i -> powerQuantityFactory);

        assertEquals(powerQuantityFactory, libsMeasureConfig.powerQuantityFactory(serviceProvider));
    }

    @Test
    void testServiceProvider() {
        assertNotNull(libsMeasureConfig.serviceProvider());
    }

    @Test
    void testSystemOfUnits(@Mock final SystemOfUnitsService systemOfUnitsService,
            @Mock final SystemOfUnits systemOfUnits) {
        Mockito.when(systemOfUnitsService.getSystemOfUnits()).then(i -> systemOfUnits);

        assertEquals(systemOfUnits, libsMeasureConfig.systemOfUnits(systemOfUnitsService));
    }

    @Test
    void testSystemOfUnitsService(@Mock final ServiceProvider serviceProvider,
            @Mock final SystemOfUnitsService systemOfUnitsService) {
        Mockito.when(serviceProvider.getSystemOfUnitsService()).then(i -> systemOfUnitsService);

        assertEquals(systemOfUnitsService, libsMeasureConfig.systemOfUnitsService(serviceProvider));
    }

    @Test
    void testVolt(@Mock final SystemOfUnits systemOfUnits, @Mock final Unit<?> unit,
            @Mock final Unit<ElectricPotential> volt) {
        Mockito.when(systemOfUnits.getUnit("V")).then(i -> unit);
        Mockito.when(unit.asType(ElectricPotential.class)).then(i -> volt);

        assertEquals(volt, libsMeasureConfig.volt(systemOfUnits));
    }

    @Test
    void testVolumeQuantityFactory(@Mock final ServiceProvider serviceProvider,
            @Mock final QuantityFactory<Volume> volumeQuantityFactory) {
        Mockito.when(serviceProvider.getQuantityFactory(Volume.class)).then(i -> volumeQuantityFactory);

        assertEquals(volumeQuantityFactory, libsMeasureConfig.volumeQuantityFactory(serviceProvider));
    }

    @Test
    void testWatt(@Mock final SystemOfUnits systemOfUnits, @Mock final Unit<?> unit, @Mock final Unit<Power> watt) {
        Mockito.when(systemOfUnits.getUnit("W")).then(i -> unit);
        Mockito.when(unit.asType(Power.class)).then(i -> watt);

        assertEquals(watt, libsMeasureConfig.watt(systemOfUnits));
    }

    @Test
    void testWattHour(@Mock final Unit<Power> watt, @Mock final Unit<Time> hour, @Mock final Unit<?> unit,
            @Mock final Unit<Energy> wattHour) {
        Mockito.when(watt.multiply(hour)).then(i -> unit);
        Mockito.when(unit.asType(Energy.class)).then(i -> wattHour);

        assertEquals(wattHour, libsMeasureConfig.wattHour(watt, hour));
    }

}
