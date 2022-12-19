/**
 * Copyright (C) 2018 Vincent Smeets
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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for the measure library.
 *
 * @author vincent
 */
@Configuration
public class LibsMeasureConfig {

    /**
     * Cubic is to the power of 3.
     */
    private static final int CUBIC = 3;

    /**
     * Get the unit: ampere (A).
     *
     * @param systemOfUnits The system of units.
     * @return The unit.
     */
    @Bean
    public Unit<ElectricCurrent> ampere(final SystemOfUnits systemOfUnits) {
        return systemOfUnits.getUnit("A").asType(ElectricCurrent.class);
    }

    /**
     * Get the unit: cubic deci meter (dm3).
     *
     * @param meter The unit: meter (m).
     * @return The unit.
     */
    @Bean
    public Unit<Volume> cubicDeciMeter(final Unit<Length> meter) {
        return MetricPrefix.DECI(meter).pow(CUBIC).asType(Volume.class);
    }

    /**
     * Get the unit: deci volt (dV).
     *
     * @param volt The unit: volt (V).
     * @return The unit.
     */
    @Bean
    public Unit<ElectricPotential> deciVolt(final Unit<ElectricPotential> volt) {
        return MetricPrefix.DECI(volt);
    }

    /**
     * Get the factory to create an electric current quantity.
     *
     * @param serviceProvider The service provider.
     * @return The factory to create an electric current quantity.
     */
    @Bean
    public QuantityFactory<ElectricCurrent> electricCurrentQuantityFactory(final ServiceProvider serviceProvider) {
        return serviceProvider.getQuantityFactory(ElectricCurrent.class);
    }

    /**
     * Get the factory to create an electric potential quantity.
     *
     * @param serviceProvider The service provider.
     * @return The factory to create an electric potential quantity.
     */
    @Bean
    public QuantityFactory<ElectricPotential> electricPotentialQuantityFactory(final ServiceProvider serviceProvider) {
        return serviceProvider.getQuantityFactory(ElectricPotential.class);
    }

    /**
     * Get the factory to create an energy quantity.
     *
     * @param serviceProvider The service provider.
     * @return The factory to create an energy quantity.
     */
    @Bean
    public QuantityFactory<Energy> energyQuantityFactory(final ServiceProvider serviceProvider) {
        return serviceProvider.getQuantityFactory(Energy.class);
    }

    /**
     * Get the unit: hour (h).
     *
     * @param systemOfUnits The system of units.
     * @return The unit.
     */
    @Bean
    public Unit<Time> hour(final SystemOfUnits systemOfUnits) {
        return systemOfUnits.getUnit("h").asType(Time.class);
    }

    /**
     * Get the unit: joule (J).
     *
     * @param systemOfUnits The system of units.
     * @return The unit.
     */
    @Bean
    public Unit<Energy> joule(final SystemOfUnits systemOfUnits) {
        return systemOfUnits.getUnit("J").asType(Energy.class);
    }

    /**
     * Get the unit: mega joule (MJ).
     *
     * @param joule The unit: joule (J).
     * @return The unit.
     */
    @Bean
    public Unit<Energy> megaJoule(final Unit<Energy> joule) {
        return MetricPrefix.MEGA(joule);
    }

    /**
     * Get the unit: meter (m).
     *
     * @param systemOfUnits The system of units.
     * @return The unit.
     */
    @Bean
    public Unit<Length> meter(final SystemOfUnits systemOfUnits) {
        return systemOfUnits.getUnit("m").asType(Length.class);
    }

    /**
     * Get the factory to create an power quantity.
     *
     * @param serviceProvider The service provider.
     * @return The factory to create a power quantity.
     */
    @Bean
    public QuantityFactory<Power> powerQuantityFactory(final ServiceProvider serviceProvider) {
        return serviceProvider.getQuantityFactory(Power.class);
    }

    /**
     * Get the service provider.
     *
     * @return The service provider.
     */
    @Bean
    public ServiceProvider serviceProvider() {
        return ServiceProvider.current();
    }

    /**
     * Get the system of units.
     *
     * @param systemOfUnitsService The system of units service.
     * @return The system of units
     */
    @Bean
    public SystemOfUnits systemOfUnits(final SystemOfUnitsService systemOfUnitsService) {
        return systemOfUnitsService.getSystemOfUnits();
    }

    /**
     * Get the system of units service.
     *
     * @param serviceProvider The service provider.
     * @return The system of units service.
     */
    @Bean
    public SystemOfUnitsService systemOfUnitsService(final ServiceProvider serviceProvider) {
        return serviceProvider.getSystemOfUnitsService();
    }

    /**
     * Get the unit: volt (V).
     *
     * @param systemOfUnits The system of units.
     * @return The unit.
     */
    @Bean
    public Unit<ElectricPotential> volt(final SystemOfUnits systemOfUnits) {
        return systemOfUnits.getUnit("V").asType(ElectricPotential.class);
    }

    /**
     * Get the factory to create a volume quantity.
     *
     * @param serviceProvider The service provider.
     * @return The factory to create a volume quantity.
     */
    @Bean
    public QuantityFactory<Volume> volumeQuantityFactory(final ServiceProvider serviceProvider) {
        return serviceProvider.getQuantityFactory(Volume.class);
    }

    /**
     * Get the unit: watt (W).
     *
     * @param systemOfUnits The system of units.
     * @return The unit.
     */
    @Bean
    public Unit<Power> watt(final SystemOfUnits systemOfUnits) {
        return systemOfUnits.getUnit("W").asType(Power.class);
    }

    /**
     * Get the unit: watt hour (Wh).
     *
     * @param watt The unit: watt (W)
     * @param hour The unit: hour (h)
     * @return The unit.
     */
    @Bean
    public Unit<Energy> wattHour(final Unit<Power> watt, final Unit<Time> hour) {
        return watt.multiply(hour).asType(Energy.class);
    }

}
