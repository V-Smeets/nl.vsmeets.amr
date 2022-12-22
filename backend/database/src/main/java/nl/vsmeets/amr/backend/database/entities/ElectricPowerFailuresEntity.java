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
import nl.vsmeets.amr.backend.database.ElectricPowerFailures;

/**
 * The electric power failures.
 *
 * @author vincent
 */
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "electric_power_failures")
public class ElectricPowerFailuresEntity extends AbstractTableEntity implements ElectricPowerFailures {

    /**
     * The meter of these failures.
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
     * 0-0:96.7.21.255 The number of power failures.
     */
    @NonNull
    @Getter
    @ToString.Include
    @Column(name = "nr_of_power_failures", nullable = false)
    private Integer nrOfPowerFailures;

    /**
     * 0-0:96.7.9.255 The number of long power failures.
     */
    @NonNull
    @Getter
    @ToString.Include
    @Column(name = "nr_of_long_power_failures", nullable = false)
    private Integer nrOfLongPowerFailures;

    /**
     * The electric power failure events.
     */
    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "electricPowerFailures")
    private Set<ElectricPowerFailureEventEntity> electricPowerFailureEvents;

}
