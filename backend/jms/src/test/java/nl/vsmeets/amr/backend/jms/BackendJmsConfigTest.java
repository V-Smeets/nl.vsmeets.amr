/**
 *
 */
package nl.vsmeets.amr.backend.jms;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the class {@link BackendJmsConfig}.
 *
 * @author vincent
 */
class BackendJmsConfigTest {

  private BackendJmsConfig backendJmsConfig;

  @BeforeEach
  void setUp() throws Exception {
    backendJmsConfig = new BackendJmsConfig();
  }

  @Test
  void test() {
    assertNotNull(backendJmsConfig);
  }

}
