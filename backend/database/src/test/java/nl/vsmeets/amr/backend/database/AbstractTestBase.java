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
package nl.vsmeets.amr.backend.database;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

/**
 * The base class for unit tests.
 *
 * @author vincent
 */
public abstract class AbstractTestBase {

  /**
   * The default length of a random string.
   */
  private static final int DEFAULT_STRING_LENGTH = 8;

  /**
   * Create a random byte.
   *
   * @return A random byte.
   */
  protected byte randomByte() {
    return (byte) RandomUtils.nextInt();
  }

  /**
   * Create a random long.
   *
   * @return A random long.
   */
  protected Duration randomDuration() {
    return Duration.ofSeconds(randomEpochSeconds(), randomNanoOfSeconds());
  }

  /**
   * Create a random int.
   *
   * @return A random int.
   */
  protected int randomInt() {
    return RandomUtils.nextInt();
  }

  /**
   * Create a random LocalDateTime.
   *
   * @return A random LocalDateTime.
   */
  protected LocalDateTime randomLocalDateTime() {
    return LocalDateTime.ofEpochSecond(randomEpochSeconds(), randomNanoOfSeconds(), ZoneOffset.UTC);
  }

  /**
   * Create a random long.
   *
   * @return A random long.
   */
  protected long randomLong() {
    return RandomUtils.nextLong();
  }

  /**
   * Create a random short.
   *
   * @return A random short.
   */
  protected short randomShort() {
    return (short) RandomUtils.nextInt();
  }

  /**
   * Create a random String.
   *
   * @return A random String.
   */
  protected String randomString() {
    return RandomStringUtils.random(DEFAULT_STRING_LENGTH);
  }

  /**
   * Create a random number of seconds from the epoch.
   *
   * @return A random number of seconds.
   */
  private long randomEpochSeconds() {
    final long epochSecondsMin = -999_999_999;
    final long epochSecondsMax = 999_999_999;
    return epochSecondsMin + randomLong() % (epochSecondsMax - epochSecondsMin + 1);
  }

  /**
   * Create a random number of nano seconds.
   *
   * @return A random number of nano seconds.
   */
  private int randomNanoOfSeconds() {
    return randomInt() % 1_000_000_000;
  }

}
