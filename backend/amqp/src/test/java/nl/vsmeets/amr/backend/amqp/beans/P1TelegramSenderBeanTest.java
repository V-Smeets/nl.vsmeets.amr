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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import nl.vsmeets.amr.libs.amqp.AmqpConstants;

/**
 * Unit tests for the class {@link P1TelegramSenderBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class P1TelegramSenderBeanTest {

    /**
     * Values used during tests.
     */
    private static final String EXCHANG_NAME = "Exchange Name";
    private static final String ROUTING_KEY = "Routing Key";

    @Mock
    private BackendAmqpProperties properties;

    @Mock
    private AmqpTemplate amqpTemplate;

    /**
     * The object under test.
     */
    @InjectMocks
    private P1TelegramSenderBean p1TelegramSenderBean;

    @Captor
    private ArgumentCaptor<String> exchangeNameCaptor;
    @Captor
    private ArgumentCaptor<String> routingKeyCaptor;
    @Captor
    private ArgumentCaptor<Message> messageCaptor;

    @Test
    void testSend() throws UnsupportedEncodingException {
        final String site = "Site";
        final String p1Telegram = "P1 Telegram";

        Mockito.when(properties.getExchangeName()).then(i -> EXCHANG_NAME);
        Mockito.when(properties.getRoutingKey()).then(i -> ROUTING_KEY);

        p1TelegramSenderBean.send(site, p1Telegram);

        verify(amqpTemplate).send(exchangeNameCaptor.capture(), routingKeyCaptor.capture(), messageCaptor.capture());
        assertEquals(EXCHANG_NAME, exchangeNameCaptor.getValue());
        assertEquals(ROUTING_KEY, routingKeyCaptor.getValue());

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
