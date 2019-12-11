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

/**
 * An interface that can be included in case a random <code>long</code> is
 * needed.
 *
 * @author vincent
 */
public interface RandomLongGenerator extends RandomGenerator {

  /**
   * Create a random <code>long</code> that is guaranteed not equal to an element
   * of <code>notEqualTo</code>.
   *
   * @param notEqualTo
   *        The values that are not allowed.
   * @return A random <code>long</code>.
   */
  default long randomLong(final long... notEqualTo) {
    final long randomValue = getRandom().nextLong();
    boolean alreadyUsed = false;
    for (final long l : notEqualTo) {
      if (randomValue == l) {
        alreadyUsed = true;
        break;
      }
    }
    if (alreadyUsed) {
      return randomLong(notEqualTo);
    } else {
      return randomValue;
    }
  }

  /**
   * Create a random <code>long</code> within the specified range that is
   * guaranteed not equal to an element of <code>notEqualTo</code>.
   *
   * @param startInclusive
   *        The lower value of the range. This value is allowed to be returned.
   * @param endExclusive
   *        The upper value of the range. This value isn't allowed to be returned.
   * @param notEqualTo
   *        The values that are not allowed.
   * @return A random <code>long</code>.
   */
  default long randomLongRange(final long startInclusive, final long endExclusive, final long... notEqualTo) {
    if (endExclusive <= startInclusive) {
      throw new IllegalArgumentException(String.format("Invalid range: %d .. %d", startInclusive, endExclusive));
    }
    long randomValue = randomLong(notEqualTo);
    if (randomValue >= startInclusive && randomValue < endExclusive) {
      return randomValue;
    }
    final long size = endExclusive - startInclusive;
    final long shapedRandomValue;
    if (size > 0) {
      /* Size is less than half of the long range */
      final long moduloValue = randomValue % size;
      final long offsetValue = moduloValue + (moduloValue < 0 ? size : 0);
      shapedRandomValue = startInclusive + offsetValue;
      boolean alreadyUsed = false;
      for (final long l : notEqualTo) {
        if (shapedRandomValue == l) {
          alreadyUsed = true;
          break;
        }
      }
      if (alreadyUsed) {
        randomValue = randomLongRange(startInclusive, endExclusive, notEqualTo);
      } else {
        randomValue = shapedRandomValue;
      }
    } else {
      /*
       * Size is more than half of the long range. On average, less than 2 iterations
       * are needed to find a valid value.
       */
      randomValue = randomLongRange(startInclusive, endExclusive, notEqualTo);
    }
    return randomValue;
  }

}
