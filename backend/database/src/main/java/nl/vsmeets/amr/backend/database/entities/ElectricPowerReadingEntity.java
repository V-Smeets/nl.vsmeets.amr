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
import nl.vsmeets.amr.backend.database.ElectricPowerReading;
import nl.vsmeets.amr.backend.database.converters.Watt2IntegerConverter;

/**
 * The electric power reading.
 *
 * @author vincent
 */
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "electric_power_reading")
public class ElectricPowerReadingEntity extends AbstractTableEntity implements ElectricPowerReading {

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
     * 1-0:1.7.0.255 The consumed power in watt (W).
     */
    @NonNull
    @Getter
    @ToString.Include
    @Column(name = "consumed_power", nullable = false)
    @Convert(converter = Watt2IntegerConverter.class)
    private Quantity<Power> consumedPower;

    /**
     * 1-0:2.7.0.255 The produced power in watt (W).
     */
    @NonNull
    @Getter
    @ToString.Include
    @Column(name = "produced_power", nullable = false)
    @Convert(converter = Watt2IntegerConverter.class)
    private Quantity<Power> producedPower;

}
