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
package nl.vsmeets.amr.backend.database;

import java.util.Set;

/**
 * The meter.
 *
 * @author vincent
 */
public interface Meter extends Table {

    /**
     * Get the electric energy readings.
     *
     * @return The electric energy readings.
     */
    Set<? extends ElectricEnergyReading> getElectricEnergyReadings();

    /**
     * Get the electric messages.
     *
     * @return The electric messages.
     */
    Set<? extends ElectricMessage> getElectricMessages();

    /**
     * Get the electric phase power readings.
     *
     * @return The electric phase power readings.
     */
    Set<? extends ElectricPhasePowerReading> getElectricPhasePowerReadings();

    /**
     * Get the electric phase voltage errors.
     *
     * @return The electric phase voltage errors.
     */
    Set<? extends ElectricPhaseVoltageErrors> getElectricPhaseVoltageErrors();

    /**
     * Get the electric power failures.
     *
     * @return The electric power failures.
     */
    Set<? extends ElectricPowerFailures> getElectricPowerFailures();

    /**
     * Get the electric power readings.
     *
     * @return The electric power readings.
     */
    Set<? extends ElectricPowerReading> getElectricPowerReadings();

    /**
     * Get the equipment identifier.
     *
     * @return The equipment identifier.
     */
    String getEquipmentIdentifier();

    /**
     * Get the gas volume readings.
     *
     * @return The gas volume readings.
     */
    Set<? extends GasVolumeReading> getGasVolumeReadings();

    /**
     * Get the measured medium.
     *
     * @return The measured medium.
     */
    MeasuredMedium getMeasuredMedium();

    /**
     * Get the P1 telegram.
     *
     * @return The P1 telegram.
     */
    P1Telegram getP1Telegram();

    /**
     * Get the slave energy readings.
     *
     * @return The slave energy readings.
     */
    Set<? extends SlaveEnergyReading> getSlaveEnergyReadings();

    /**
     * Get the thermal energy readings.
     *
     * @return The thermal energy readings.
     */
    Set<? extends ThermalEnergyReading> getThermalEnergyReadings();

    /**
     * Get the water volume readings.
     *
     * @return The water volume readings.
     */
    Set<? extends WaterVolumeReading> getWaterVolumeReadings();

}
