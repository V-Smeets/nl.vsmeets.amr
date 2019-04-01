/**
 *
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
public interface RandomLocalDateTimeGenerator extends RandomIntGenerator, RandomLongGenerator {

  /**
   * Create a random {@link LocalDateTime}.
   *
   * @return A random {@link LocalDateTime}.
   */
  default LocalDateTime randomLocalDateTime() {
    return LocalDateTime.ofEpochSecond(randomLong(-999_999_999L, 999_999_999L + 1L), randomInt(0, 1_000_000_000),
        ZoneOffset.UTC);
  }

}
