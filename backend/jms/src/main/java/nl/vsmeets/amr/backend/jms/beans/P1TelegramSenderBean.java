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

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import nl.vsmeets.amr.backend.jms.P1TelegramSender;

/**
 * Provide the communication with the JMS server to send P1 telegrams.
 *
 * @author vincent
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class P1TelegramSenderBean implements P1TelegramSender {

  /**
   * The properties for this bean.
   */
  @Valid
  private final BackendJmsProperties properties;

  /**
   * A template to send JMS messages.
   */
  private final JmsMessagingTemplate jmsMessagingTemplate;

  @Override
  public void send(final String site, final String p1Telegram) {
    final String headerFieldSite = properties.getHeaderFieldSite();
    final String destinationName = properties.getDestinationName();
    final Map<String, Object> headers = new HashMap<>();
    headers.put(headerFieldSite, site);

    log.debug("Sending message for site {} of length {} to {}", site, p1Telegram.length(), destinationName);
    jmsMessagingTemplate.convertAndSend(destinationName, p1Telegram, headers);
  }

}
