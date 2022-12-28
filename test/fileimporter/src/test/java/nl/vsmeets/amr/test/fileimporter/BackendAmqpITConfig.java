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
package nl.vsmeets.amr.test.fileimporter;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.Channel;

import nl.vsmeets.amr.backend.amqp.BackendAmqpConfig;

/**
 * The configuration for the {@link BackendAmqpConfig}.
 *
 * @author vincent
 */
@Configuration
@RabbitListenerTest(capture = true, spy = true)
public class BackendAmqpITConfig {

    @Bean
    public BackendAmqpITListener backendAmqpITListener() {
        return new BackendAmqpITListener();
    }

    @Bean
    public ConnectionFactory connectionFactory() throws IOException {
        final Channel channel = mock(Channel.class);
        when(channel.isOpen()).then(i -> true);

        final Connection connection = mock(Connection.class);
        when(connection.createChannel(anyBoolean())).then(i -> channel);

        final ConnectionFactory factory = mock(ConnectionFactory.class);
        when(factory.createConnection()).then(i -> connection);

        return factory;
    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> rabbitListenerContainerFactory(
            final ConnectionFactory connectionFactory) {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public TestRabbitTemplate testRabbitTemplate(final ConnectionFactory connectionFactory) {
        return new TestRabbitTemplate(connectionFactory);
    }

}
