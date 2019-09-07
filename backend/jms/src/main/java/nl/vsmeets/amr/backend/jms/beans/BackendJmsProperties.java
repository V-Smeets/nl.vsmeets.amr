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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Properties for this module.
 *
 * @author vincent
 */
@Component
@ConfigurationProperties("amr.backend.jms")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BackendJmsProperties {

  /**
   * The properties to configure a bridge.
   *
   * @author vincent
   */
  @NoArgsConstructor
  @Getter
  @Setter
  @ToString
  static class BridgeProperties {

    /**
     * The name.
     */
    private String name;

    /**
     * The queue from where the messages are read.
     */
    private String queueName;

    /**
     * The name connection where the messages are forwarded to.
     */
    private String staticConnector;

    /**
     * The user to use at the connector.
     */
    private String user;

    /**
     * The password to use at the connector.
     */
    private String password;

    /**
     * The address to where the messages are forwarded.
     */
    private String forwardingAddress;

  }

  /**
   * The name of the Artemis broker.
   */
  private String name;

  /**
   * The URIs of the static connectors.
   * <dl>
   * <dt>key</dt>
   * <dd>The name of the connector</dd>
   * <dt>value</dt>
   * <dd>The URI of the connector</dd>
   * </dl>
   */
  private Map<String, String> connectorUris = new HashMap<>();

  /**
   * A list of bridge properties.
   */
  private List<BridgeProperties> bridges = new ArrayList<>();

  /**
   * The name of the destination to which the received telegrams are sent.
   */
  private String destinationName;

  /**
   * The name of the message header to hold the name of the site.
   */
  private String headerFieldSite;

}
