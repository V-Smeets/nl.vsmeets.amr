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
package nl.vsmeets.amr.test.backend.jms;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import nl.vsmeets.amr.backend.jms.BackendJmsConfig;
import nl.vsmeets.amr.backend.jms.P1TelegramSender;
import nl.vsmeets.amr.backend.jms.beans.BackendJmsProperties;
import nl.vsmeets.amr.libs.junit.RandomStringGenerator;

/**
 * Integration tests for {@link P1TelegramSender}.
 *
 * @author vincent
 */
@SpringBootTest( //
    classes = { BackendJmsConfig.class }, //
    properties = { //
        "amr.backend.jms.header-field-site=Site", //
        "amr.backend.jms.destination-name=client-queue" //
    })
class P1TelegramSenderIT implements RandomStringGenerator {

  @Autowired
  private BackendJmsProperties properties;

  @Autowired
  private JmsMessagingTemplate jmsMessagingTemplate;

  @Autowired
  private P1TelegramSender p1TelegramSender;

  @Test
  void testSend() {
    final String site = randomString();
    final String p1Telegram = randomString();
    final String headerFieldSite = properties.getHeaderFieldSite();
    final String destinationName = properties.getDestinationName();

    p1TelegramSender.send(site, p1Telegram);

    final Message<?> message = jmsMessagingTemplate.receive(destinationName);
    assertNotNull(message);
    final MessageHeaders headers = message.getHeaders();
    assertEquals(site, headers.get(headerFieldSite, String.class));
    assertEquals(p1Telegram, message.getPayload());
  }

}
