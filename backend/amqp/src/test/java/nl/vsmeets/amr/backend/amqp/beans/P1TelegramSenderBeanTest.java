/**
 * Copyright (C) 2020 Vincent Smeets
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
package nl.vsmeets.amr.backend.amqp.beans;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import nl.vsmeets.amr.libs.amqp.AmqpConstants;
import nl.vsmeets.amr.libs.junit.RandomStringGenerator;

/**
 * Unit tests for the class {@link P1TelegramSenderBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class P1TelegramSenderBeanTest implements RandomStringGenerator {

  private final String exchangeName = randomString();
  private final String routingKey = randomString();
  private final BackendAmqpProperties properties = new BackendAmqpProperties();

  @Mock
  private AmqpTemplate amqpTemplate;

  private P1TelegramSenderBean p1TelegramSenderBean;

  @Captor
  private ArgumentCaptor<String> exchangeNameCaptor;
  @Captor
  private ArgumentCaptor<String> routingKeyCaptor;
  @Captor
  private ArgumentCaptor<Message> messageCaptor;

  @BeforeEach
  void setUp() throws Exception {
    p1TelegramSenderBean = new P1TelegramSenderBean(properties, amqpTemplate);

    properties.setExchangeName(exchangeName);
    properties.setRoutingKey(routingKey);
  }

  @Test
  void testSend() throws UnsupportedEncodingException {
    final String site = randomString();
    final String p1Telegram = randomString();

    p1TelegramSenderBean.send(site, p1Telegram);

    verify(amqpTemplate).send(exchangeNameCaptor.capture(), routingKeyCaptor.capture(), messageCaptor.capture());
    assertEquals(exchangeName, exchangeNameCaptor.getValue());
    assertEquals(routingKey, routingKeyCaptor.getValue());

    final Message message = messageCaptor.getValue();
    assertNotNull(message);

    final MessageProperties messageProperties = message.getMessageProperties();
    assertNotNull(messageProperties);

    final String encoding = messageProperties.getContentEncoding();
    final long length = messageProperties.getContentLength();
    assertEquals(MessageProperties.CONTENT_TYPE_TEXT_PLAIN, messageProperties.getContentType());
    assertEquals("UTF-8", encoding);
    assertEquals(site, messageProperties.getHeader(AmqpConstants.HEADER_SITE));

    final byte[] body = message.getBody();
    assertNotNull(body);
    assertEquals(body.length, length);
    assertEquals(p1Telegram, new String(body, encoding));
  }

}
