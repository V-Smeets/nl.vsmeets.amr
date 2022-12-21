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
package nl.vsmeets.amr.backend.amqp.beans;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import nl.vsmeets.amr.backend.amqp.P1TelegramSender;
import nl.vsmeets.amr.libs.amqp.AmqpConstants;

/**
 * Provide the communication with the JMS server to send P1 telegrams.
 *
 * @author vincent
 */
@Service
@Log4j2
public class P1TelegramSenderBean implements P1TelegramSender {

    /**
     * The character set that will be used to encode the message.
     */
    private static final Charset CHARACTER_SET = StandardCharsets.UTF_8;

    /**
     * The properties for this bean.
     */
    @Autowired
    @Valid
    private BackendAmqpProperties properties;

    /**
     * A template to send AMQP messages.
     */
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void send(final String site, final String p1Telegram) {
        final String exchange = properties.getExchangeName();
        final String routingKey = properties.getRoutingKey();

        final byte[] body = p1Telegram.getBytes(CHARACTER_SET);
        final MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        messageProperties.setContentEncoding(CHARACTER_SET.name());
        messageProperties.setContentLength(body.length);
        messageProperties.setHeader(AmqpConstants.HEADER_SITE, site);

        final Message message = new Message(body, messageProperties);
        log.debug("Sending message of length {} from site {} to the exchange {} with routing key {}",
                p1Telegram.length(), site, exchange, routingKey);
        amqpTemplate.send(exchange, routingKey, message);
    }

}
