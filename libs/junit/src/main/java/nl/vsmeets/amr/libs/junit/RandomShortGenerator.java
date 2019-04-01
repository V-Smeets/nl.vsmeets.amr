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
 * An interface that can be included in case a random <code>short</code> is
 * needed.
 *
 * @author vincent
 */
public interface RandomShortGenerator extends RandomIntGenerator {

  /**
   * Create a random <code>short</code>.
   *
   * @return A random <code>short</code>.
   */
  default short randomShort() {
    return (short) randomInt();
  }

}
