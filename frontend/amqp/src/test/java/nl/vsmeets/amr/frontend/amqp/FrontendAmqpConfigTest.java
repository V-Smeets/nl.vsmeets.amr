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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

  private FrontendAmqpConfig frontendAmqpConfig;

  @BeforeEach
  void setUp() throws Exception {
    frontendAmqpConfig = new FrontendAmqpConfig();
  }

  @Test
  void testDeadLetterQueue() {
    final FrontendAmqpProperties properties = mock(FrontendAmqpProperties.class);
    when(properties.getDeadLetterQueueName()).then(i -> deadLetterQueueName);

    final Queue deadLetterQueue = frontendAmqpConfig.deadLetterQueue(properties);

    assertNotNull(deadLetterQueue);
    assertEquals(deadLetterQueueName, deadLetterQueue.getName());
    assertTrue(deadLetterQueue.isDurable());
    assertFalse(deadLetterQueue.isExclusive());
    assertFalse(deadLetterQueue.isAutoDelete());
    assertEquals("lazy", deadLetterQueue.getArguments().get("x-queue-mode"));
  }

  @Test
  void testErrorHandler() {
    final ServerFatalExceptionStrategy exceptionStrategy = mock(ServerFatalExceptionStrategy.class);

    final ErrorHandler errorHandler = frontendAmqpConfig.errorHandler(exceptionStrategy);

    assertNotNull(errorHandler);
  }

  @Test
  void testMessageListenerContainer() {
    final SimpleRabbitListenerContainerFactory factory = mock(SimpleRabbitListenerContainerFactory.class);
    final Queue queue = mock(Queue.class);
    final P1TelegramReceiverBean p1TelegramReceiverBean = mock(P1TelegramReceiverBean.class);
    final ErrorHandler errorHandler = mock(ErrorHandler.class);
    final SimpleMessageListenerContainer container = mock(SimpleMessageListenerContainer.class);
    when(factory.createListenerContainer()).then(i -> container);

    final SimpleMessageListenerContainer simpleMessageListenerContainer =
        frontendAmqpConfig.messageListenerContainer(factory, queue, p1TelegramReceiverBean, errorHandler);

    assertEquals(container, simpleMessageListenerContainer);
    verify(container).setQueues(queue);
    verify(container).setMessageListener(p1TelegramReceiverBean);
    verify(container).setErrorHandler(errorHandler);
  }

  @Test
  void testQueue() {
    final FrontendAmqpProperties properties = mock(FrontendAmqpProperties.class);
    when(properties.getQueueName()).then(i -> queueName);
    when(properties.getDeadLetterQueueName()).then(i -> deadLetterQueueName);

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
