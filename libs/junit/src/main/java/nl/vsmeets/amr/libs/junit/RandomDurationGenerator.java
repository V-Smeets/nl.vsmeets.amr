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

import java.time.Duration;

/**
 * An interface that can be included in case a random {@link Duration} is
 * needed.
 *
 * @author vincent
 */
public interface RandomDurationGenerator extends RandomIntGenerator, RandomLongGenerator {

  /**
   * Create a random {@link Duration} that is guaranteed not equal to an element
   * of <code>notEqualTo</code>.
   *
   * @param notEqualTo
   *        The values that are not allowed.
   * @return A random {@link Duration}.
   */
  default Duration randomDuration(final Duration... notEqualTo) {
    final Duration randomValue = Duration.ofSeconds(randomLong(), randomIntRange(0, 1_000_000_000));
    boolean alreadyUsed = false;
    for (final Duration duration : notEqualTo) {
      if (randomValue.equals(duration)) {
        alreadyUsed = true;
        break;
      }
    }
    if (alreadyUsed) {
      return randomDuration(notEqualTo);
    } else {
      return randomValue;
    }
  }

  /**
   * Create a random {@link Duration} with only seconds that is guaranteed not
   * equal to an element of <code>notEqualTo</code>.
   *
   * @param notEqualTo
   *        The values that are not allowed.
   * @return A random {@link Duration}.
   */
  default Duration randomDurationSeconds(final Duration... notEqualTo) {
    final Duration randomValue = Duration.ofSeconds(randomLong());
    boolean alreadyUsed = false;
    for (final Duration duration : notEqualTo) {
      if (randomValue.equals(duration)) {
        alreadyUsed = true;
        break;
      }
    }
    if (alreadyUsed) {
      return randomDurationSeconds(notEqualTo);
    } else {
      return randomValue;
    }
  }

}
