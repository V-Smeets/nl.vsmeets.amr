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
 * Create and find {@link ElectricPowerFailures} entities.
 *
 * @author vincent
 */
public interface ElectricPowerFailuresFactory {

  /**
   * Create a new {@link ElectricPowerFailures}.
   *
   * @param meter
   *        The meter of these failures.
   * @param dateTime
   *        The date and time of these failures in the sites time zone.
   * @param consumedPower
   *        The number of power failures.
   * @param producedPower
   *        The number of long power failures.
   * @return The {@link ElectricPowerFailures}.
   * @throws ConstraintViolationException
   *         A database constraint constraint has been violated.
   */
  ElectricPowerFailures create(final Meter meter, final LocalDateTime dateTime, final Integer nrOfPowerFailures,
      final Integer nrOfLongPowerFailures) throws ConstraintViolationException;

  /**
   * Find a {@link ElectricPowerFailures}.
   *
   * @param meter
   *        The meter of this reading.
   * @param dateTime
   *        The date and time of this reading in the sites time zone.
   * @return The {@link ElectricPowerFailures}.
   */
  Optional<? extends ElectricPowerFailures> find(final Meter meter, final LocalDateTime dateTime);

}
