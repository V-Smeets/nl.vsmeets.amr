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
   * Create a random {@link String} that is guaranteed not equal to an element of
   * <code>notEqualTo</code>.
   *
   * @param notEqualTo
   *        The values that are not allowed.
   * @return A random {@link String}.
   */
  default String randomString(final String... notEqualTo) {
    return randomStringOfCharacters(8, notEqualTo);
  }

  /**
   * Create a random {@link String} with a specified number of characters that is
   * guaranteed not equal to an element of <code>notEqualTo</code>.
   *
   * @param nrOfCharacters
   *        The number of characters.
   * @param notEqualTo
   *        The values that are not allowed.
   * @return A random {@link String}.
   */
  default String randomStringOfCharacters(final int nrOfCharacters, final String... notEqualTo) {
    if (nrOfCharacters < 0) {
      throw new IllegalArgumentException(String.format("Invalid number of characters: %d", nrOfCharacters));
    }
    final StringBuilder stringBuilder = new StringBuilder(nrOfCharacters);
    for (int i = 0; i < nrOfCharacters; i++) {
      stringBuilder.append((char) randomIntRange(Character.MIN_VALUE, Character.MAX_VALUE + 1));
    }
    final String randomValue = stringBuilder.toString();
    boolean alreadyUsed = false;
    for (final String string : notEqualTo) {
      if (randomValue.equals(string)) {
        alreadyUsed = true;
        break;
      }
    }
    if (alreadyUsed) {
      return randomStringOfCharacters(nrOfCharacters, notEqualTo);
    } else {
      return randomValue;
    }
  }

  /**
   * Create a random {@link String} with a specified number of code points that is
   * guaranteed not equal to an element of <code>notEqualTo</code>.
   *
   * @param nrOfCodePoints
   *        The number of code points.
   * @param notEqualTo
   *        The values that are not allowed.
   * @return A random {@link String}.
   */
  default String randomStringOfCodePoints(final int nrOfCodePoints, final String... notEqualTo) {
    if (nrOfCodePoints < 0) {
      throw new IllegalArgumentException(String.format("Invalid number of code points: %d", nrOfCodePoints));
    }
    final StringBuilder stringBuilder = new StringBuilder(nrOfCodePoints);
    for (int i = 0; i < nrOfCodePoints; i++) {
      stringBuilder.append(Character.toChars(randomIntRange(Character.MIN_CODE_POINT, Character.MAX_CODE_POINT + 1)));
    }
    final String randomValue = stringBuilder.toString();
    boolean alreadyUsed = false;
    for (final String string : notEqualTo) {
      if (randomValue.equals(string)) {
        alreadyUsed = true;
        break;
      }
    }
    if (alreadyUsed) {
      return randomStringOfCodePoints(nrOfCodePoints, notEqualTo);
    } else {
      return randomValue;
    }
  }

}
