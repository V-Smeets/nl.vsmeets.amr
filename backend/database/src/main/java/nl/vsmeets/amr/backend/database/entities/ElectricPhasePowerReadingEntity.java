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

import java.time.LocalDateTime;

import javax.measure.Quantity;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.ElectricPotential;
import javax.measure.quantity.Power;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import nl.vsmeets.amr.backend.database.ElectricPhasePowerReading;
import nl.vsmeets.amr.backend.database.converters.Ampere2ShortConverter;
import nl.vsmeets.amr.backend.database.converters.DeciVolt2ShortConverter;
import nl.vsmeets.amr.backend.database.converters.Watt2IntegerConverter;

/**
 * The electric power reading for a phase.
 *
 * @author vincent
 */
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "electric_phase_power_reading")
public class ElectricPhasePowerReadingEntity extends AbstractTableEntity implements ElectricPhasePowerReading {

    /**
     * The meter of this reading.
     */
    @NonNull
    @Getter
    @EqualsAndHashCode.Include
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "meter_id", nullable = false)
    private MeterEntity meter;

    /**
     * 0-0:1.0.0.255 The date and time of this reading in the local time zone.
     */
    @NonNull
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "local_date_time", nullable = false)
    private LocalDateTime localDateTime;

    /**
     * The phase number.
     */
    @NonNull
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "phase_number", nullable = false)
    private Byte phaseNumber;

    /**
     * 1-0:32.7.0.255 The instantaneous voltage in deci volt (dV).
     */
    @NonNull
    @Getter
    @ToString.Include
    @Column(name = "instantaneous_voltage", nullable = false)
    @Convert(converter = DeciVolt2ShortConverter.class)
    private Quantity<ElectricPotential> instantaneousVoltage;

    /**
     * 1-0:31.7.0.255 The instantaneous current in ampere (A).
     */
    @NonNull
    @Getter
    @ToString.Include
    @Column(name = "instantaneous_current", nullable = false)
    @Convert(converter = Ampere2ShortConverter.class)
    private Quantity<ElectricCurrent> instantaneousCurrent;

    /**
     * 1-0:*.7.0.255 The instantaneous consumed power in watt (W).
     */
    @NonNull
    @Getter
    @ToString.Include
    @Column(name = "instantaneous_consumed_power", nullable = false)
    @Convert(converter = Watt2IntegerConverter.class)
    private Quantity<Power> instantaneousConsumedPower;

    /**
     * 1-0:21.7.0.255 The instantaneous produced power in watt (W).
     */
    @NonNull
    @Getter
    @ToString.Include
    @Column(name = "instantaneous_produced_power", nullable = false)
    @Convert(converter = Watt2IntegerConverter.class)
    private Quantity<Power> instantaneousProducedPower;

}
