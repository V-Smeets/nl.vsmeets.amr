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
 * An interface that can be included in case a random {@link LocalDateTime} is
 * needed.
 *
 * @author vincent
 */
public interface RandomLocalDateTimeGenerator extends RandomLongGenerator, RandomZoneOffsetGenerator {

  /**
   * The maximum number of seconds (relative to the epoch) that can be used for a
   * random date/time. It is about 4000 years after the epoch.
   *
   * @return The maximum number of seconds.
   */
  default long maxSeconds() {
    return 4000L * 365L * 24L * 60L * 60L;
  }

  /**
   * The minimum number of seconds (relative to the epoch) that can be used for a
   * random date/time.
   *
   * @return The minimum number of seconds.
   */
  default long minSeconds() {
    return -1970L * 365L * 24L * 60L * 60L;
  }

  /**
   * Create a random {@link LocalDateTime} that is guaranteed not equal to an
   * element of <code>notEqualTo</code>.
   *
   * @param notEqualTo
   *        The values that are not allowed.
   * @return A random {@link LocalDateTime}.
   */
  default LocalDateTime randomLocalDateTime(final LocalDateTime... notEqualTo) {
    final LocalDateTime randomValue = LocalDateTime.ofEpochSecond(randomLongRange(minSeconds(), maxSeconds() + 1L),
        randomIntRange(0, 1_000_000_000), randomZoneOffset());
    boolean alreadyUsed = false;
    for (final LocalDateTime localDateTime : notEqualTo) {
      if (randomValue.equals(localDateTime)) {
        alreadyUsed = true;
        break;
      }
    }
    if (alreadyUsed) {
      return randomLocalDateTime(notEqualTo);
    } else {
      return randomValue;
    }
  }

  /**
   * Create a random {@link LocalDateTime} with zero precision (no nanoseconds)
   * that is guaranteed not equal to an element of <code>notEqualTo</code>.
   *
   * @param notEqualTo
   *        The values that are not allowed.
   * @return A random {@link LocalDateTime}.
   */
  default LocalDateTime randomLocalDateTimeZeroPrecision(final LocalDateTime... notEqualTo) {
    final LocalDateTime randomValue =
        LocalDateTime.ofEpochSecond(randomLongRange(minSeconds(), maxSeconds() + 1L), 0, randomZoneOffset());
    boolean alreadyUsed = false;
    for (final LocalDateTime localDateTime : notEqualTo) {
      if (randomValue.equals(localDateTime)) {
        alreadyUsed = true;
        break;
      }
    }
    if (alreadyUsed) {
      return randomLocalDateTimeZeroPrecision(notEqualTo);
    } else {
      return randomValue;
    }
  }

}
