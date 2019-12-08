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

import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsMessagingTemplate;

import com.mockrunner.jms.JMSTestModule;
import com.mockrunner.mock.jms.JMSMockObjectFactory;

/**
 * The test configuration for the {@link P1TelegramReaderIT}.
 *
 * @author vincent
 */
@Configuration
public class P1TelegramReaderConfig {

  @Bean
  public ConnectionFactory connectionFactory(final JMSMockObjectFactory jmsMockObjectFactory) {
    return jmsMockObjectFactory.createMockConnectionFactory();
  }

  @Bean
  public JmsMessagingTemplate jmsMessagingTemplate(final ConnectionFactory connectionFactory) {
    return new JmsMessagingTemplate(connectionFactory);
  }

  @Bean
  public JMSMockObjectFactory jmsMockObjectFactory() {
    return new JMSMockObjectFactory();
  }

  @Bean
  public JMSTestModule jmsTestModule(final JMSMockObjectFactory mockFactory) {
    return new JMSTestModule(mockFactory);
  }

}
