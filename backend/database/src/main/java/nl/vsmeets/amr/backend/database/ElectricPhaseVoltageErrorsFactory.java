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

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Create and find {@link ElectricPhaseVoltageErrors} entities.
 *
 * @author vincent
 */
public interface ElectricPhaseVoltageErrorsFactory {

    /**
     * Create a new {@link ElectricPhaseVoltageErrors}.
     *
     * @param meter             The meter of these errors.
     * @param localDateTime     The local date and time of these errors.
     * @param phaseNumber       The phase number.
     * @param nrOfVoltageSags   The number of voltage sags.
     * @param nrOfVoltageSwells The number of voltage swells.
     * @return The {@link ElectricPhaseVoltageErrors}.
     * @throws ConstraintViolationException A database constraint constraint has
     *                                      been violated.
     */
    ElectricPhaseVoltageErrors create(final Meter meter, final LocalDateTime localDateTime, final Byte phaseNumber,
            final Integer nrOfVoltageSags, final Integer nrOfVoltageSwells) throws ConstraintViolationException;

    /**
     * Find a {@link ElectricPhaseVoltageErrors}.
     *
     * @param meter         The meter of this reading.
     * @param localDateTime The local date and time of these errors.
     * @param phaseNumber   The phase number.
     * @return The {@link ElectricPhaseVoltageErrors}.
     */
    Optional<? extends ElectricPhaseVoltageErrors> find(final Meter meter, final LocalDateTime localDateTime,
            final Byte phaseNumber);

}
