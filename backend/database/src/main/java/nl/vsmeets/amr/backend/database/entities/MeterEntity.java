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
package nl.vsmeets.amr.backend.database.entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import nl.vsmeets.amr.backend.database.Meter;

/**
 * The meter.
 *
 * @author vincent
 */
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "meter")
public class MeterEntity extends AbstractTableEntity implements Meter {

    /**
     * The P1 telegram.
     */
    @NonNull
    @Getter
    @EqualsAndHashCode.Include
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "p1_telegram_id", nullable = false)
    private P1TelegramEntity p1Telegram;

    /**
     * The measured medium.
     */
    @NonNull
    @Getter
    @EqualsAndHashCode.Include
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "measured_medium_id", nullable = false)
    private MeasuredMediumEntity measuredMedium;

    /**
     * 0-0:96.1.1.255 The equipment identifier.
     */
    @NonNull
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "equipment_identifier", nullable = false, length = 48)
    private String equipmentIdentifier;

    /**
     * The electric energy readings.
     */
    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meter")
    private Set<ElectricEnergyReadingEntity> electricEnergyReadings;

    /**
     * The electric power readings.
     */
    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meter")
    private Set<ElectricPowerReadingEntity> electricPowerReadings;

    /**
     * The electric power failures.
     */
    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meter")
    private Set<ElectricPowerFailuresEntity> electricPowerFailures;

    /**
     * The electric phase voltage errors.
     */
    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meter")
    private Set<ElectricPhaseVoltageErrorsEntity> electricPhaseVoltageErrors;

    /**
     * The electric messages.
     */
    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meter")
    private Set<ElectricMessageEntity> electricMessages;

    /**
     * The electric phase power readings.
     */
    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meter")
    private Set<ElectricPhasePowerReadingEntity> electricPhasePowerReadings;

    /**
     * The gas volume readings.
     */
    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meter")
    private Set<GasVolumeReadingEntity> gasVolumeReadings;

    /**
     * The thermal energy readings.
     */
    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meter")
    private Set<ThermalEnergyReadingEntity> thermalEnergyReadings;

    /**
     * The water volume readings.
     */
    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meter")
    private Set<WaterVolumeReadingEntity> waterVolumeReadings;

    /**
     * The slave energy readings.
     */
    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meter")
    private Set<SlaveEnergyReadingEntity> slaveEnergyReadings;

}
