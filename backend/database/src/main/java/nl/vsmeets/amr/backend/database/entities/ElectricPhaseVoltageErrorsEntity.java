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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import nl.vsmeets.amr.backend.database.ElectricPhaseVoltageErrors;

/**
 * The electric voltage errors for a phase.
 *
 * @author vincent
 */
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "electric_phase_voltage_errors")
public class ElectricPhaseVoltageErrorsEntity extends AbstractTableEntity implements ElectricPhaseVoltageErrors {

    /**
     * The meter of these errors.
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
     * 1-0:*.32.0.255 The number of voltage sags.
     */
    @NonNull
    @Getter
    @ToString.Include
    @Column(name = "nr_of_voltage_sags", nullable = false)
    private Integer nrOfVoltageSags;

    /**
     * 1-0:*.36.0.255 The number of voltage swells.
     */
    @NonNull
    @Getter
    @ToString.Include
    @Column(name = "nr_of_voltage_swells", nullable = false)
    private Integer nrOfVoltageSwells;

}
