/**
 *
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
   * Create a random {@link Duration}.
   *
   * @return A random {@link Duration}.
   */
  default Duration randomDuration() {
    return Duration.ofSeconds(randomLong(-999_999_999L, 999_999_999L + 1L), randomInt(0, 1_000_000_000));
  }

}
