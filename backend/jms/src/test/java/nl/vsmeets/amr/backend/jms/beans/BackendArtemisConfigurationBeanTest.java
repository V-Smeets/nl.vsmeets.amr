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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.activemq.artemis.core.config.BridgeConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.vsmeets.amr.backend.jms.beans.BackendJmsProperties.BridgeProperties;
import nl.vsmeets.amr.libs.junit.RandomStringGenerator;

/**
 * Unit tests for the class {@link BackendArtemisConfigurationBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class BackendArtemisConfigurationBeanTest implements RandomStringGenerator {

  private final String connectionName = randomString();
  private final String connectionUri = randomString();
  private final Map<String, String> connectionUris = Map.of(connectionName, connectionUri);

  private final String bridgeName = randomString();
  private final String bridgeQueueName = randomString();
  private final String bridgeStaticConnector = randomString();
  private final String bridgeUser = randomString();
  private final String bridgePassword = randomString();
  private final String bridgeForwardingAddress = randomString();
  @Mock
  private BridgeProperties bridgeProperties;
  private List<BridgeProperties> bridges;
  private final List<BridgeConfiguration> bridgeConfigurations = new ArrayList<>();

  @Mock
  private BackendJmsProperties properties;

  @Mock
  private Configuration configuration;

  private BackendArtemisConfigurationBean backendArtemisConfigurationBean;

  private final String name = randomString();

  @BeforeEach
  void setUp() throws Exception {
    backendArtemisConfigurationBean = new BackendArtemisConfigurationBean(properties);
    bridges = List.of(bridgeProperties);
  }

  @Test
  void testCustomizeBridges() throws Exception {
    when(properties.getBridges()).thenReturn(bridges);
    when(bridgeProperties.getName()).thenReturn(bridgeName);
    when(bridgeProperties.getQueueName()).thenReturn(bridgeQueueName);
    when(bridgeProperties.getStaticConnector()).thenReturn(bridgeStaticConnector);
    when(bridgeProperties.getUser()).thenReturn(bridgeUser);
    when(bridgeProperties.getPassword()).thenReturn(bridgePassword);
    when(bridgeProperties.getForwardingAddress()).thenReturn(bridgeForwardingAddress);
    when(configuration.getBridgeConfigurations()).thenReturn(bridgeConfigurations);

    assertDoesNotThrow(() -> backendArtemisConfigurationBean.customize(configuration));

    assertAll( //
        () -> assertEquals(1, bridgeConfigurations.size()), //
        () -> {
          final BridgeConfiguration bridgeConfiguration = bridgeConfigurations.get(0);
          assertAll( //
              () -> assertEquals(bridgeName, bridgeConfiguration.getName()), //
              () -> assertEquals(bridgeQueueName, bridgeConfiguration.getQueueName()), //
              () -> assertEquals(1, bridgeConfiguration.getStaticConnectors().size()), //
              () -> {
                final String staticConnector = bridgeConfiguration.getStaticConnectors().get(0);
                assertEquals(bridgeStaticConnector, staticConnector);
              }, //
              () -> assertEquals(bridgeUser, bridgeConfiguration.getUser()), //
              () -> assertEquals(bridgePassword, bridgeConfiguration.getPassword()), //
              () -> assertEquals(bridgeForwardingAddress, bridgeConfiguration.getForwardingAddress()) //
          );
        } //
    );
  }

  @Test
  void testCustomizeConnectorConfigurations() throws Exception {
    when(properties.getConnectorUris()).thenReturn(connectionUris);

    assertDoesNotThrow(() -> backendArtemisConfigurationBean.customize(configuration));

    verify(configuration).addConnectorConfiguration(eq(connectionName), eq(connectionUri));
  }

  @Test
  void testCustomizeConnectorConfigurationsException() throws Exception {
    when(properties.getConnectorUris()).thenReturn(connectionUris);
    when(configuration.addConnectorConfiguration(eq(connectionName), eq(connectionUri))).thenThrow(Exception.class);

    assertDoesNotThrow(() -> backendArtemisConfigurationBean.customize(configuration));
  }

  @Test
  void testCustomizeName() {
    when(properties.getName()).thenReturn(name);

    assertDoesNotThrow(() -> backendArtemisConfigurationBean.customize(configuration));

    verify(configuration).setName(eq(name));
  }

}
