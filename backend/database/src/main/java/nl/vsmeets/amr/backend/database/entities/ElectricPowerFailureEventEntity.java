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

import java.time.Duration;
import java.time.LocalDateTime;

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
import nl.vsmeets.amr.backend.database.ElectricPowerFailureEvent;
import nl.vsmeets.amr.backend.database.converters.Duration2LongConverter;

/**
 * The electric power failure event.
 *
 * @author vincent
 */
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "electric_power_failure_event")
public class ElectricPowerFailureEventEntity extends AbstractTableEntity implements ElectricPowerFailureEvent {

    /**
     * The electric power failures.
     */
    @NonNull
    @Getter
    @EqualsAndHashCode.Include
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "electric_power_failures", nullable = false)
    private ElectricPowerFailuresEntity electricPowerFailures;

    /**
     * 0-0:96.7.19.255 The end of failure time.
     */
    @NonNull
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "end_of_failure_time", nullable = false)
    private LocalDateTime endOfFailureTime;

    /**
     * 0-0:96.7.19.255 The failure duration.
     */
    @NonNull
    @Getter
    @ToString.Include
    @Column(name = "failure_duration", nullable = false)
    @Convert(converter = Duration2LongConverter.class)
    private Duration failureDuration;

}
