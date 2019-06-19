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
package nl.vsmeets.amr.backend.jms.beans;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.vsmeets.amr.libs.junit.RandomStringGenerator;

/**
 * Unit tests for the class {@link BackendJmsProperties}.
 *
 * @author vincent
 */
class BackendJmsPropertiesTest implements RandomStringGenerator {

  private BackendJmsProperties backendJmsProperties;
  private String destinationName;
  private String headerFieldSite;

  @BeforeEach
  void setUp() throws Exception {
    backendJmsProperties = new BackendJmsProperties();
    destinationName = randomString();
    backendJmsProperties.setDestinationName(destinationName);
    headerFieldSite = randomString();
    backendJmsProperties.setHeaderFieldSite(headerFieldSite);
  }

  @Test
  void testGetDestinationName() {
    assertEquals(destinationName, backendJmsProperties.getDestinationName());
  }

  @Test
  void testGetHeaderFieldSite() {
    assertEquals(headerFieldSite, backendJmsProperties.getHeaderFieldSite());
  }

  @Test
  void testToString() {
    final String toString = backendJmsProperties.toString();
    assertAll( //
        () -> assertNotNull(toString), //
        () -> assertTrue(toString.contains(backendJmsProperties.getClass().getSimpleName())), //
        () -> assertTrue(toString.contains(destinationName)), //
        () -> assertTrue(toString.contains(headerFieldSite)) //
    );
  }

}
