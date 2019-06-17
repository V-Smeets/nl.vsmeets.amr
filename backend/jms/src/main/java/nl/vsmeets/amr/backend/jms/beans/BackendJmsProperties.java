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
   * The name of the message header to hold the name of the site.
   */
  private String headerFieldSite = "X-Site";

  /**
   * The name of the destination to which the received telegrams are sent.
   */
  private String destinationName;

}
