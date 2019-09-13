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
package nl.vsmeets.amr.test.service.p1telegram.reader;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import nl.vsmeets.amr.service.p1telegram.reader.P1TelegramReader;
import nl.vsmeets.amr.service.p1telegram.reader.ServiceP1TelegramReaderConfig;

/**
 * Integration tests for {@link P1TelegramReader}.
 *
 * @author vincent
 */
@SpringBootTest( //
    classes = { ServiceP1TelegramReaderConfig.class }, //
    properties = { //
        "amr.backend.jms.name=amr-test", //
        "amr.backend.jms.connectorUris.self=vm://localhost", //
        "amr.backend.jms.connectorUris.illegal=Not-Valid", //
        "amr.backend.jms.bridges[0].name=client-to-server", //
        "amr.backend.jms.bridges[0].queueName=client-queue", //
        "amr.backend.jms.bridges[0].staticConnector=self", //
        "amr.backend.jms.bridges[0].user=user", //
        "amr.backend.jms.bridges[0].password=password", //
        "amr.backend.jms.bridges[0].forwardingAddress=server-queue", //
        "amr.backend.jms.destination-name=client-queue", //
        "amr.backend.jms.header-field-site=Site", //
        "amr.service.p1telegram.reader.site=Here" //
    })
class P1TelegramReaderIT {

  private static final String CR_NL = "\r\n";

  @Autowired
  private P1TelegramReader p1TelegramReader;

  @Autowired
  private JmsMessagingTemplate jmsMessagingTemplate;

  @Test
  public void testSave() throws IOException {
    final StringBuilder inputData = new StringBuilder();
    // Missing start of the telegram
    inputData.append(CR_NL);
    inputData.append("Data").append(CR_NL);
    inputData.append("!BBA3").append(CR_NL);
    // Incorrect checksum
    inputData.append("/Header").append(CR_NL);
    inputData.append(CR_NL);
    inputData.append("Data").append(CR_NL);
    inputData.append("!0000").append(CR_NL);
    // Incorrect checksum length (too short)
    inputData.append("/Header").append(CR_NL);
    inputData.append(CR_NL);
    inputData.append("Data").append(CR_NL);
    inputData.append("!BB").append(CR_NL);
    // Incorrect checksum length (too long)
    inputData.append("/Header").append(CR_NL);
    inputData.append(CR_NL);
    inputData.append("Data").append(CR_NL);
    inputData.append("!BBA300").append(CR_NL);
    final StringBuilder p1Telegram = new StringBuilder();
    // Correct telegram
    p1Telegram.append("/Header").append(CR_NL);
    p1Telegram.append(CR_NL);
    p1Telegram.append("Data").append(CR_NL);
    p1Telegram.append("!BBA3").append(CR_NL);
    inputData.append(p1Telegram);
    // Missing end of the telegram
    inputData.append("/Header").append(CR_NL);
    inputData.append(CR_NL);
    inputData.append("Data").append(CR_NL);
    inputData.append("!");
    final BufferedReader bufferedReader = new BufferedReader(new StringReader(inputData.toString()));

    p1TelegramReader.save(bufferedReader);

    final String headerFieldSite = "Site";
    final String destinationName = "client-queue";
    final String site = "Here";
    jmsMessagingTemplate.getJmsTemplate().setReceiveTimeout(1_000L);
    Message<?> message = jmsMessagingTemplate.receive(destinationName);
    assertNotNull(message);
    final MessageHeaders headers = message.getHeaders();
    assertEquals(site, headers.get(headerFieldSite, String.class));
    assertEquals(p1Telegram.toString(), message.getPayload().toString());

    // No more messages
    message = jmsMessagingTemplate.receive(destinationName);
    assertNull(message);
  }

}
