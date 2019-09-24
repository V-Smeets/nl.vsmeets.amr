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

import nl.vsmeets.amr.backend.jms.beans.BackendJmsProperties.BridgeProperties;
import nl.vsmeets.amr.libs.junit.RandomStringGenerator;

/**
 * An interface that can be included in case a random {@link BridgeProperties}
 * is needed.
 *
 * @author vincent
 */
public interface RandomBridgePropertiesGenerator extends RandomStringGenerator {

  /**
   * Create a random {@link BridgeProperties}.
   *
   * @return A random {@link BridgeProperties}.
   */
  default BridgeProperties RandomBridgeProperties() {
    final BridgeProperties bridgeProperties = new BridgeProperties();
    bridgeProperties.setName(randomString());
    bridgeProperties.setQueueName(randomString());
    bridgeProperties.setStaticConnector(randomString());
    bridgeProperties.setUser(randomString());
    bridgeProperties.setPassword(randomString());
    bridgeProperties.setForwardingAddress(randomString());
    return bridgeProperties;
  }

}
