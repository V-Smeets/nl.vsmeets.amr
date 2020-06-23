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
package nl.vsmeets.amr.test.frontend.serial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness.InvocationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import nl.vsmeets.amr.frontend.serial.FrontendSerialConfig;
import nl.vsmeets.amr.test.frontend.BackendAmqpITConfig;
import nl.vsmeets.amr.test.frontend.BackendAmqpITListener;

/**
 * The integration tests for the serial front end.
 *
 * @author vincent
 */
@SpringBootTest(
// @formatter:off
    classes = {
        BackendAmqpITConfig.class,
        FrontendSerialITConfig.class,
        FrontendSerialConfig.class },
    args = {
        "--No-Runner" },
    properties = {
        "amr.backend.amqp.exchange-name=exchange",
        "amr.backend.amqp.routing-key=" + BackendAmqpITListener.CLIENT_QUEUE_QUEUE,
        "amr.service.p1telegram.reader.site=IntegrationTest" }
    // @formatter:on
)
@ActiveProfiles("test")
@EnableConfigurationProperties
class FrontendSerialIT {

  @Autowired
  @Qualifier("serialReaderBean")
  private ApplicationRunner applicationRunner;

  @Autowired
  private RabbitListenerTestHarness harness;

  private ExitCodeGenerator exitCodeGenerator;

  @BeforeEach
  void setUp() throws Exception {
    exitCodeGenerator = (ExitCodeGenerator) applicationRunner;
  }

  @Test
  void testRun() throws Exception {
    final ApplicationArguments args = new DefaultApplicationArguments();

    applicationRunner.run(args);

    assertEquals(0, exitCodeGenerator.getExitCode());

    final BackendAmqpITListener listener = harness.getSpy(BackendAmqpITListener.CLIENT_QUEUE_ID);
    assertNotNull(listener);
    verify(listener, times(2)).clientQueue(any(Message.class));

    InvocationData invocationData =
        harness.getNextInvocationDataFor(BackendAmqpITListener.CLIENT_QUEUE_ID, 10, TimeUnit.SECONDS);
    assertNotNull(invocationData);
    Object[] arguments = invocationData.getArguments();
    assertNotNull(arguments);
    assertEquals(1, arguments.length);
    Message message = (Message) arguments[0];
    assertNotNull(message);
    byte[] body = message.getBody();
    assertNotNull(body);
    assertEquals(54, body.length);
    MessageProperties messageProperties = message.getMessageProperties();
    assertNotNull(messageProperties);
    assertEquals("IntegrationTest", messageProperties.getHeader("Site"));
    assertEquals(MessageProperties.CONTENT_TYPE_TEXT_PLAIN, messageProperties.getContentType());
    assertEquals(body.length, messageProperties.getContentLength());
    assertEquals(MessageDeliveryMode.PERSISTENT, messageProperties.getDeliveryMode());

    invocationData = harness.getNextInvocationDataFor(BackendAmqpITListener.CLIENT_QUEUE_ID, 10, TimeUnit.SECONDS);
    assertNotNull(invocationData);
    arguments = invocationData.getArguments();
    assertNotNull(arguments);
    assertEquals(1, arguments.length);
    message = (Message) arguments[0];
    assertNotNull(message);
    body = message.getBody();
    assertNotNull(body);
    assertEquals(54, body.length);
    messageProperties = message.getMessageProperties();
    assertNotNull(messageProperties);
    assertEquals("IntegrationTest", messageProperties.getHeader("Site"));
    assertEquals(MessageProperties.CONTENT_TYPE_TEXT_PLAIN, messageProperties.getContentType());
    assertEquals(body.length, messageProperties.getContentLength());
    assertEquals(MessageDeliveryMode.PERSISTENT, messageProperties.getDeliveryMode());
  }

}
