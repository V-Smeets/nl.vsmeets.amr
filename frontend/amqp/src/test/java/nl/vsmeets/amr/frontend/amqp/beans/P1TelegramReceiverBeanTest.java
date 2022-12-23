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
package nl.vsmeets.amr.frontend.amqp.beans;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;

/**
 * Unit tests for the class {@link P1TelegramReceiverBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class P1TelegramReceiverBeanTest {

    @InjectMocks
    private P1TelegramReceiverBean p1TelegramReceiverBean;

    /**
     * Test method for
     * {@link nl.vsmeets.amr.frontend.amqp.beans.P1TelegramReceiverBean#onMessage(org.springframework.amqp.core.Message)}.
     */
    @Test
    void testOnMessage(@Mock final Message message) {
        assertDoesNotThrow(() -> p1TelegramReceiverBean.onMessage(message));
    }

}
