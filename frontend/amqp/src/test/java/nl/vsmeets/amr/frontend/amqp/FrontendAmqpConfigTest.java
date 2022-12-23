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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.util.ErrorHandler;

import nl.vsmeets.amr.frontend.amqp.beans.FrontendAmqpProperties;
import nl.vsmeets.amr.frontend.amqp.beans.P1TelegramReceiverBean;
import nl.vsmeets.amr.frontend.amqp.beans.ServerFatalExceptionStrategy;

/**
 * Unit tests for the class {@link FrontendAmqpConfig}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class FrontendAmqpConfigTest {

    /**
     * Values used during tests.
     */
    private static final String queueName = "Queue Name";
    private static final String deadLetterQueueName = "Dead Letter Queue Name";

    @InjectMocks
    private FrontendAmqpConfig frontendAmqpConfig;

    @Test
    void testDeadLetterQueue(@Mock final FrontendAmqpProperties properties) {
        Mockito.when(properties.getDeadLetterQueueName()).then(i -> deadLetterQueueName);

        final Queue deadLetterQueue = frontendAmqpConfig.deadLetterQueue(properties);

        assertNotNull(deadLetterQueue);
        assertEquals(deadLetterQueueName, deadLetterQueue.getName());
        assertTrue(deadLetterQueue.isDurable());
        assertFalse(deadLetterQueue.isExclusive());
        assertFalse(deadLetterQueue.isAutoDelete());
        assertEquals("lazy", deadLetterQueue.getArguments().get("x-queue-mode"));
    }

    @Test
    void testErrorHandler(@Mock final ServerFatalExceptionStrategy exceptionStrategy) {
        final ErrorHandler errorHandler = frontendAmqpConfig.errorHandler(exceptionStrategy);

        assertNotNull(errorHandler);
    }

    @Test
    void testMessageListenerContainer(@Mock final SimpleRabbitListenerContainerFactory factory, @Mock final Queue queue,
            @Mock final P1TelegramReceiverBean p1TelegramReceiverBean, @Mock final ErrorHandler errorHandler,
            @Mock final SimpleMessageListenerContainer container) {
        Mockito.when(factory.createListenerContainer()).then(i -> container);

        final SimpleMessageListenerContainer simpleMessageListenerContainer = frontendAmqpConfig
                .messageListenerContainer(factory, queue, p1TelegramReceiverBean, errorHandler);

        assertEquals(container, simpleMessageListenerContainer);
        verify(container).setQueues(queue);
        verify(container).setMessageListener(p1TelegramReceiverBean);
        verify(container).setErrorHandler(errorHandler);
    }

    @Test
    void testQueue(@Mock final FrontendAmqpProperties properties) {
        Mockito.when(properties.getQueueName()).then(i -> queueName);
        Mockito.when(properties.getDeadLetterQueueName()).then(i -> deadLetterQueueName);

        final Queue queue = frontendAmqpConfig.queue(properties);

        assertNotNull(queue);
        assertEquals(queueName, queue.getName());
        assertTrue(queue.isDurable());
        assertFalse(queue.isExclusive());
        assertFalse(queue.isAutoDelete());
        assertEquals("", queue.getArguments().get("x-dead-letter-exchange"));
        assertEquals(deadLetterQueueName, queue.getArguments().get("x-dead-letter-routing-key"));
    }

}
