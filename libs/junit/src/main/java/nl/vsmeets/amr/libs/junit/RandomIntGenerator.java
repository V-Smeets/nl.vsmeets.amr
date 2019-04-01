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
 * An interface that can be included in case a random <code>int</code> is
 * needed.
 *
 * @author vincent
 */
public interface RandomIntGenerator extends RandomGenerator {

  /**
   * Create a random <code>int</code>.
   *
   * @return A random <code>int</code>.
   */
  default int randomInt() {
    return getRandom().nextInt();
  }

  /**
   * Create a random <code>int</code> within the specified range.
   *
   * @param startInclusive
   *        The lower value of the range. This value is allowed to be returned.
   * @param endExclusive
   *        The upper value of the range. This value isn't allowed to be returned.
   * @return A random <code>int</code>.
   */
  default int randomInt(final int startInclusive, final int endExclusive) {
    if (endExclusive <= startInclusive) {
      throw new IllegalArgumentException(String.format("Invalid range: %d .. %d", startInclusive, endExclusive));
    }
    final int size = endExclusive - startInclusive;
    if (size > 0) {
      /* Size is less than half of the int range */
      final int moduloValue = randomInt() % size;
      final int randomValue = moduloValue + (moduloValue < 0 ? size : 0);
      return startInclusive + randomValue;
    } else {
      /*
       * Size is more than half of the int range. On average, less than 2 iterations
       * are needed to find a valid value.
       */
      int value;
      do {
        value = randomInt();
      } while (value < startInclusive || value >= endExclusive);
      return value;
    }
  }

}
