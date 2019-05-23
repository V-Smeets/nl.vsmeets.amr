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

import java.time.ZoneOffset;

/**
 * An interface that can be included in case a random {@link ZoneOffset} is
 * needed.
 *
 * @author vincent
 */
public interface RandomZoneOffsetGenerator extends RandomIntGenerator {

  /**
   * Create a random {@link ZoneOffset} that is guaranteed not equal to an element
   * of <code>notEqualTo</code>.
   *
   * @param notEqualTo
   *        The values that are not allowed.
   * @return A random {@link ZoneOffset}.
   */
  default ZoneOffset randomZoneOffset(final ZoneOffset... notEqualTo) {
    final ZoneOffset randomValue = ZoneOffset
        .ofTotalSeconds(randomIntRange(ZoneOffset.MIN.getTotalSeconds(), ZoneOffset.MAX.getTotalSeconds() + 1));
    boolean alreadyUsed = false;
    for (final ZoneOffset zoneOffset : notEqualTo) {
      if (randomValue.equals(zoneOffset)) {
        alreadyUsed = true;
        break;
      }
    }
    if (alreadyUsed) {
      return randomZoneOffset(notEqualTo);
    } else {
      return randomValue;
    }
  }

}
