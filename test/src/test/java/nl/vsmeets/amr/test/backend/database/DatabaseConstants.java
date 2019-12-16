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
package nl.vsmeets.amr.test.backend.database;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.stream.Stream;

/**
 * Constants that define the limits of the database.
 *
 * @author vincent
 */
final class DatabaseConstants {

  /**
   * The first date that the database should be able to handle. 1 AD
   */
  public static final LocalDateTime MIN_LOCAL_DATE_TIME = LocalDateTime.of(1, Month.JANUARY, 1, 0, 0, 0).withNano(0);

  /**
   * The last date that the database should be able to handle. 294276 AD
   */
  public static final LocalDateTime MAX_LOCAL_DATE_TIME =
      LocalDateTime.of(294276, Month.DECEMBER, 31, 23, 59, 59).withNano(0);

  public static Stream<LocalDateTime> validLocalDateTimeValues() {
    final ZoneOffset offset = ZoneOffset.UTC;
    return Stream.of(MIN_LOCAL_DATE_TIME, MIN_LOCAL_DATE_TIME.plusSeconds(1L),
        LocalDateTime.ofEpochSecond(-1L, 0, offset), LocalDateTime.ofEpochSecond(0L, 0, offset),
        LocalDateTime.ofEpochSecond(1L, 0, offset), MAX_LOCAL_DATE_TIME.minusSeconds(1L), MAX_LOCAL_DATE_TIME);
  }

  /**
   * No instance can be created.
   */
  private DatabaseConstants() {
  }

}
