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
import java.time.ZoneOffset;

/**
 * An interface that can be included in case a random {@link LocalDateTime} is
 * needed.
 *
 * @author vincent
 */
public interface RandomLocalDateTimeGenerator extends RandomLongGenerator, RandomZoneOffsetGenerator {

  /**
   * Create a random {@link LocalDateTime} that is guaranteed not equal to an
   * element of <code>notEqualTo</code>.
   *
   * @param notEqualTo
   *        The values that are not allowed.
   * @return A random {@link LocalDateTime}.
   */
  default LocalDateTime randomLocalDateTime(final LocalDateTime... notEqualTo) {
    final ZoneOffset offset = ZoneOffset.UTC;
    final long startInclusiveSeconds = LocalDateTime.MIN.toEpochSecond(offset);
    final long endExclusiveSeconds = LocalDateTime.MAX.toEpochSecond(offset) + 1L;
    final LocalDateTime randomValue = LocalDateTime.ofEpochSecond(
        randomLongRange(startInclusiveSeconds, endExclusiveSeconds), randomIntRange(0, 1_000_000_000), offset);
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
    final ZoneOffset offset = ZoneOffset.UTC;
    final long startInclusiveSeconds = LocalDateTime.MIN.toEpochSecond(offset);
    final long endExclusiveSeconds = LocalDateTime.MAX.toEpochSecond(offset) + 1L;
    final LocalDateTime randomValue =
        LocalDateTime.ofEpochSecond(randomLongRange(startInclusiveSeconds, endExclusiveSeconds), 0, offset);
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

  /**
   * Create a random {@link LocalDateTime} within the specified range that is
   * guaranteed not equal to an element of <code>notEqualTo</code>.
   *
   * @param startInclusive
   *        The lower value of the range. This value is allowed to be returned.
   * @param endExclusive
   *        The upper value of the range. This value isn't allowed to be returned.
   * @param notEqualTo
   *        The values that are not allowed.
   * @return A random {@link LocalDateTime}.
   */
  default LocalDateTime randomLocalDateTimeZeroPrecisionRange(final LocalDateTime startInclusive,
      final LocalDateTime endExclusive, final LocalDateTime... notEqualTo) {
    if (!endExclusive.isAfter(startInclusive)) {
      throw new IllegalArgumentException(String.format("Invalid range: %s .. %s", startInclusive, endExclusive));
    }
    final ZoneOffset offset = ZoneOffset.UTC;
    final long startInclusiveSeconds = startInclusive.toEpochSecond(offset);
    final long endExclusiveSeconds = endExclusive.toEpochSecond(offset);
    final LocalDateTime randomValue =
        LocalDateTime.ofEpochSecond(randomLongRange(startInclusiveSeconds, endExclusiveSeconds), 0, offset);
    boolean alreadyUsed = false;
    for (final LocalDateTime localDateTime : notEqualTo) {
      if (randomValue.equals(localDateTime)) {
        alreadyUsed = true;
        break;
      }
    }
    if (alreadyUsed) {
      return randomLocalDateTimeZeroPrecisionRange(startInclusive, endExclusive, notEqualTo);
    } else {
      return randomValue;
    }
  }

}
