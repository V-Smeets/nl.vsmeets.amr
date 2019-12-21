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

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.activemq.artemis.core.config.BridgeConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import nl.vsmeets.amr.backend.jms.beans.BackendJmsProperties.BridgeProperties;

/**
 * Customize the artemis configuration for the backend.
 *
 * @author vincent
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class BackendArtemisConfigurationBean implements ArtemisConfigurationCustomizer {

  /**
   * The properties for this bean.
   */
  @Valid
  private final BackendJmsProperties properties;

  @Override
  public void customize(final Configuration configuration) {
    log.info("Properties: {}", properties);
    log.info("old Configuration: {}", configuration);

    Optional.ofNullable(properties.getName()).ifPresent(configuration::setName);

    for (final Entry<String, String> connectorEntry : properties.getConnectorUris().entrySet()) {
      try {
        configuration.addConnectorConfiguration(connectorEntry.getKey(), connectorEntry.getValue());
      } catch (final Exception e) {
        log.warn(String.format("Can't add a connector for %s", connectorEntry), e);
      }
    }

    for (final BridgeProperties bridgeProperties : properties.getBridges()) {
      final BridgeConfiguration bridgeConfiguration = new BridgeConfiguration();

      Optional.ofNullable(bridgeProperties.getName()).ifPresent(bridgeConfiguration::setName);
      Optional.ofNullable(bridgeProperties.getQueueName()).ifPresent(bridgeConfiguration::setQueueName);
      Optional.ofNullable(bridgeProperties.getStaticConnector()).map(List::of)
          .ifPresent(bridgeConfiguration::setStaticConnectors);
      Optional.ofNullable(bridgeProperties.getUser()).ifPresent(bridgeConfiguration::setUser);
      Optional.ofNullable(bridgeProperties.getPassword()).ifPresent(bridgeConfiguration::setPassword);
      Optional.ofNullable(bridgeProperties.getForwardingAddress()).ifPresent(bridgeConfiguration::setForwardingAddress);

      configuration.getBridgeConfigurations().add(bridgeConfiguration);
    }

    log.info("new Configuration: {}", configuration);
  }

}
