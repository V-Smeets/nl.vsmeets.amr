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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.vsmeets.amr.backend.jms.beans.BackendJmsProperties.BridgeProperties;
import nl.vsmeets.amr.libs.junit.RandomStringGenerator;

/**
 * Unit tests for the class {@link BackendJmsProperties}.
 *
 * @author vincent
 */
class BackendJmsPropertiesTest implements RandomStringGenerator, RandomBridgePropertiesGenerator {

  private BackendJmsProperties backendJmsProperties;
  private String name;
  private Map<String, String> connectorUris;
  private List<BridgeProperties> bridges;
  private String destinationName;
  private String headerFieldSite;

  @BeforeEach
  void setUp() throws Exception {
    backendJmsProperties = new BackendJmsProperties();
    name = randomString();
    backendJmsProperties.setName(name);
    connectorUris = new HashMap<>();
    connectorUris.put(randomString(), randomString());
    backendJmsProperties.setConnectorUris(connectorUris);
    bridges = new ArrayList<>();
    bridges.add(RandomBridgeProperties());
    backendJmsProperties.setBridges(bridges);
    destinationName = randomString();
    backendJmsProperties.setDestinationName(destinationName);
    headerFieldSite = randomString();
    backendJmsProperties.setHeaderFieldSite(headerFieldSite);
  }

  @Test
  void testGetBridges() {
    assertAll( //
        () -> assertEquals(bridges, backendJmsProperties.getBridges()), //
        () -> assertEquals(bridges.size(), backendJmsProperties.getBridges().size()) //
    );
  }

  @Test
  void testGetConnectorUris() {
    assertAll( //
        () -> assertEquals(connectorUris, backendJmsProperties.getConnectorUris()), //
        () -> assertEquals(connectorUris.keySet(), backendJmsProperties.getConnectorUris().keySet()), //
        () -> assertEquals(connectorUris.values(), backendJmsProperties.getConnectorUris().values()) //
    );
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
  void testGetName() {
    assertEquals(name, backendJmsProperties.getName());
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
