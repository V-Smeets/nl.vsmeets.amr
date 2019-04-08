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
 * An interface that can be included in case a random {@link String} is needed.
 *
 * @author vincent
 */
public interface RandomStringGenerator extends RandomIntGenerator {

  /**
   * Create a random {@link String}.
   *
   * @return A random {@link String}.
   */
  default String randomString() {
    return randomString(8);
  }

  /**
   * Create a random {@link String} of the specified number of characters. It can
   * contain UTF-16 character pairs.
   *
   * @param length
   *        The length of the random {@link String}. It should be greater or equal
   *        to zero.
   * @return The random {@link String}.
   */
  default String randomString(final int length) {
    if (length < 0) {
      throw new IllegalArgumentException(String.format("Invalid length: %d", length));
    }
    final StringBuilder stringBuilder = new StringBuilder(length);
    while (stringBuilder.length() < length) {
      final int codePoint = randomInt(Character.MIN_CODE_POINT, Character.MAX_CODE_POINT + 1);
      stringBuilder.appendCodePoint(codePoint);
    }
    if (stringBuilder.length() > length) {
      // The last code point required 2 characters.
      // Just drop off the low part.
      stringBuilder.setLength(length);
    }
    return stringBuilder.toString();
  }

}
