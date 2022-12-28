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

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * This class will be a listener for the AMQP back end.
 *
 * @author vincent
 */
public class BackendAmqpITListener {

    public static final String CLIENT_QUEUE_ID = "clientQueue";
    public static final String CLIENT_QUEUE_QUEUE = "nl.vsmeets.amr.client";

    @RabbitListener(id = CLIENT_QUEUE_ID, queues = CLIENT_QUEUE_QUEUE)
    public void clientQueue(final Message message) {
    }

}
