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
package nl.vsmeets.amr.frontend.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

import jakarta.validation.Valid;
import nl.vsmeets.amr.frontend.amqp.beans.FrontendAmqpProperties;
import nl.vsmeets.amr.frontend.amqp.beans.P1TelegramReceiverBean;
import nl.vsmeets.amr.frontend.amqp.beans.ServerFatalExceptionStrategy;

/**
 * The configuration class for the AMQP front end.
 *
 * @author vincent
 */
@Configuration
@ComponentScan
@EnableRabbit
public class FrontendAmqpConfig {

    /**
     * Create the dead letter queue.
     *
     * @param properties The properties that define the name of the queue.
     * @return The queue.
     */
    @Bean
    public Queue deadLetterQueue(@Valid final FrontendAmqpProperties properties) {
        final String deadLetterQueueName = properties.getDeadLetterQueueName();
        return QueueBuilder.durable(deadLetterQueueName).lazy().build();
    }

    /**
     * Create the bean that will handle errors.
     *
     * @param exceptionStrategy The strategy to handle the errors.
     * @return The error handler.
     */
    @Bean
    public ErrorHandler errorHandler(final ServerFatalExceptionStrategy exceptionStrategy) {
        return new ConditionalRejectingErrorHandler(exceptionStrategy);
    }

    /**
     * Create a message listener container that will listen for messages on a queue.
     *
     * @param factory                The factory to create the container.
     * @param queue                  The queue to listen to.
     * @param p1TelegramReceiverBean The bean that will handle the received
     *                               messages.
     * @param errorHandler           The handler that will handle transmission
     *                               errors.
     * @return The container.
     */
    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(final SimpleRabbitListenerContainerFactory factory,
            @Qualifier("queue") final Queue queue, final P1TelegramReceiverBean p1TelegramReceiverBean,
            @Qualifier("errorHandler") final ErrorHandler errorHandler) {
        final SimpleMessageListenerContainer container = factory.createListenerContainer();
        container.setQueues(queue);
        container.setMessageListener(p1TelegramReceiverBean);
        container.setErrorHandler(errorHandler);
        return container;
    }

    /**
     * Create the queue that will receive the messages.
     *
     * @param properties The properties that define the name of the queue and the
     *                   associated dead letter queue.
     * @return The queue.
     */
    @Bean
    public Queue queue(@Valid final FrontendAmqpProperties properties) {
        final String queueName = properties.getQueueName();
        final String deadLetterQueueName = properties.getDeadLetterQueueName();
        return QueueBuilder.durable(queueName).deadLetterExchange("").deadLetterRoutingKey(deadLetterQueueName).build();
    }

}
