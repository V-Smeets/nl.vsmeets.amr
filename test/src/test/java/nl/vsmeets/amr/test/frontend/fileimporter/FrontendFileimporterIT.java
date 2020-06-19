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
package nl.vsmeets.amr.test.frontend.fileimporter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

import nl.vsmeets.amr.frontend.fileimporter.FrontendFileimporterConfig;
import nl.vsmeets.amr.test.frontend.BackendAmqpITConfig;
import nl.vsmeets.amr.test.frontend.BackendAmqpITListener;

/**
 * The integration tests for the front end file importer.
 *
 * @author vincent
 */
@SpringBootTest(
// @formatter:off
    classes = {
        BackendAmqpITConfig.class,
        FrontendFileimporterConfig.class },
    args = {
        "--No-Runner" },
    properties = {
        "amr.backend.amqp.exchange-name=exchange",
        "amr.backend.amqp.routing-key=" + BackendAmqpITListener.CLIENT_QUEUE_QUEUE,
        "amr.service.p1telegram.reader.site=IntegrationTest" }
    // @formatter:on
)
@EnableConfigurationProperties
class FrontendFileimporterIT {

  @Autowired
  @Qualifier("fileImporterBean")
  private ApplicationRunner applicationRunner;

  @Autowired
  private RabbitListenerTestHarness harness;

  private ExitCodeGenerator exitCodeGenerator;

  @BeforeEach
  void setUp() throws Exception {
    exitCodeGenerator = (ExitCodeGenerator) applicationRunner;
  }

  @Test
  void testRunEmptyFile() throws Exception {
    final File filename = resourceToFile("/frontend/fileimporter/empty.txt");
    final ApplicationArguments args = new DefaultApplicationArguments(filename.getPath());

    applicationRunner.run(args);

    assertEquals(0, exitCodeGenerator.getExitCode());
  }

  @Test
  void testRunIncorrectCrc() throws Exception {
    final File filename = resourceToFile("/frontend/fileimporter/incorrect-crc.txt");
    final ApplicationArguments args = new DefaultApplicationArguments(filename.getPath());

    applicationRunner.run(args);

    assertEquals(0, exitCodeGenerator.getExitCode());
  }

  @Test
  void testRunMissingCrc() throws Exception {
    final File filename = resourceToFile("/frontend/fileimporter/missing-crc.txt");
    final ApplicationArguments args = new DefaultApplicationArguments(filename.getPath());

    applicationRunner.run(args);

    assertEquals(0, exitCodeGenerator.getExitCode());
  }

  @Test
  void testRunMissingHeader() throws Exception {
    final File filename = resourceToFile("/frontend/fileimporter/missing-header.txt");
    final ApplicationArguments args = new DefaultApplicationArguments(filename.getPath());

    applicationRunner.run(args);

    assertEquals(0, exitCodeGenerator.getExitCode());
  }

  @Test
  void testRunNoFiles() throws Exception {
    final ApplicationArguments args = new DefaultApplicationArguments();

    applicationRunner.run(args);

    assertEquals(0, exitCodeGenerator.getExitCode());
  }

  @Test
  void testRunP1Telegram() throws Exception {
    final File filename = resourceToFile("/frontend/fileimporter/p1-telegram.txt");
    final ApplicationArguments args = new DefaultApplicationArguments(filename.getPath());

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

  @Test
  void testRunUnknownFile() throws Exception {
    final ApplicationArguments args = new DefaultApplicationArguments("/Unknown file");

    applicationRunner.run(args);

    assertEquals(1, exitCodeGenerator.getExitCode());
  }

  /**
   * Convert a resource to a file and return it.
   *
   * @param resourceName
   *        The name of the resource.
   * @return The file.
   * @throws IOException
   */
  private File resourceToFile(final String resourceName) throws IOException {
    final InputStream inputStream = getClass().getResourceAsStream(resourceName);

    final Path resourcePath = Paths.get(resourceName);
    final String fileName = resourcePath.getFileName().toString();
    final int dotIndex = fileName.indexOf('.');
    String prefix;
    String suffix;
    if (dotIndex >= 3) {
      prefix = fileName.substring(0, dotIndex);
      suffix = fileName.substring(dotIndex);
    } else {
      prefix = fileName;
      suffix = null;
    }
    final File outputFile = File.createTempFile(prefix, suffix);
    outputFile.deleteOnExit();

    Files.copy(inputStream, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    return outputFile;
  }

}
