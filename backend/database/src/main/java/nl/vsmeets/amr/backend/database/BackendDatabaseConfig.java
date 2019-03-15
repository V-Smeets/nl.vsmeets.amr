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

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import nl.vsmeets.amr.backend.database.beans.ElectricEnergyReadingFactoryBean;
import nl.vsmeets.amr.backend.database.beans.ElectricMessageFactoryBean;
import nl.vsmeets.amr.backend.database.beans.ElectricPhasePowerReadingFactoryBean;
import nl.vsmeets.amr.backend.database.beans.ElectricPhaseVoltageErrorsFactoryBean;
import nl.vsmeets.amr.backend.database.beans.ElectricPowerFailureEventFactoryBean;
import nl.vsmeets.amr.backend.database.beans.ElectricPowerFailuresFactoryBean;
import nl.vsmeets.amr.backend.database.beans.ElectricPowerReadingFactoryBean;
import nl.vsmeets.amr.backend.database.beans.GasVolumeReadingFactoryBean;
import nl.vsmeets.amr.backend.database.beans.MeasuredMediumFactoryBean;
import nl.vsmeets.amr.backend.database.beans.MeterFactoryBean;
import nl.vsmeets.amr.backend.database.beans.P1TelegramFactoryBean;
import nl.vsmeets.amr.backend.database.beans.SiteFactoryBean;
import nl.vsmeets.amr.backend.database.beans.SlaveEnergyReadingFactoryBean;
import nl.vsmeets.amr.backend.database.beans.ThermalEnergyReadingFactoryBean;
import nl.vsmeets.amr.backend.database.beans.WaterVolumeReadingFactoryBean;
import nl.vsmeets.amr.backend.database.converters.ApplicationContextStore;
import nl.vsmeets.amr.libs.measure.LibsMeasureConfig;

/**
 * The configuration class for database backend.
 *
 * @author vincent
 */
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {})
@EnableTransactionManagement
@Import({
    // Components in this module.
    ElectricEnergyReadingFactoryBean.class, ElectricMessageFactoryBean.class,
    ElectricPhasePowerReadingFactoryBean.class, ElectricPhaseVoltageErrorsFactoryBean.class,
    ElectricPowerFailureEventFactoryBean.class, ElectricPowerFailuresFactoryBean.class,
    ElectricPowerReadingFactoryBean.class, GasVolumeReadingFactoryBean.class, MeasuredMediumFactoryBean.class,
    MeterFactoryBean.class, P1TelegramFactoryBean.class, SiteFactoryBean.class, SlaveEnergyReadingFactoryBean.class,
    ThermalEnergyReadingFactoryBean.class, WaterVolumeReadingFactoryBean.class,
    //
    ApplicationContextStore.class,
    // Other modules.
    LibsMeasureConfig.class })
public class BackendDatabaseConfig {

}
