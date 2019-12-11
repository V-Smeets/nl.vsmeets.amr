/**
 * Copyright (C) 2019 Vincent Smeets
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
package nl.vsmeets.amr.libs.junit;

import java.time.LocalDateTime;

/**
 * Constant values used by the {@link RandomLocalDateTimeGenerator}.
 *
 * @author vincent
 */
final class LocalDateTimeGeneratorConstants {

  /**
   * The minimum value of the allowed date/time range.
   */
  public static final LocalDateTime RANGE_MINIMUM_DATE_TIME = LocalDateTime.of(0, 1, 1, 0, 0, 0);

  /**
   * The maximum value of the allowed date/time range.
   */
  public static final LocalDateTime RANGE_MAXIMUM_DATE_TIME = LocalDateTime.of(4000, 12, 31, 23, 59, 59);

  /**
   * No instance can be created.
   */
  private LocalDateTimeGeneratorConstants() {
  }

}
