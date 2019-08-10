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
package nl.vsmeets.amr.backend.jms.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsMessagingTemplate;

import nl.vsmeets.amr.libs.junit.RandomStringGenerator;

/**
 * Unit tests for the class {@link P1TelegramSenderBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class P1TelegramSenderBeanTest implements RandomStringGenerator {

  @Mock
  private BackendJmsProperties properties;

  @Mock
  private JmsMessagingTemplate jmsMessagingTemplate;

  private P1TelegramSenderBean p1TelegramSenderBean;

  private final String headerFieldSite = randomString();
  private final String destinationName = randomString();

  @BeforeEach
  void setUp() throws Exception {
    p1TelegramSenderBean = new P1TelegramSenderBean(properties, jmsMessagingTemplate);
  }

  @Test
  void testSend() {
    final String site = randomString();
    final String p1Telegram = randomString();
    when(properties.getDestinationName()).thenReturn(destinationName);
    when(properties.getHeaderFieldSite()).thenReturn(headerFieldSite);

    p1TelegramSenderBean.send(site, p1Telegram);

    @SuppressWarnings("unchecked")
    final ArgumentCaptor<Map<String, Object>> headers = ArgumentCaptor.forClass(Map.class);
    verify(jmsMessagingTemplate).convertAndSend(eq(destinationName), eq(p1Telegram), headers.capture());
    assertEquals(site, headers.getValue().get(headerFieldSite));
  }

}
