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

import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler.DefaultExceptionStrategy;
import org.springframework.stereotype.Service;

/**
 * In case the processing of a received message causes an exception, then this
 * bean will decide whether the message will be requeued or rejected.
 *
 * @author vincent
 */
@Service
public class ServerFatalExceptionStrategy extends DefaultExceptionStrategy {

    @Override
    protected boolean isUserCauseFatal(final Throwable cause) {
        // Every exception will be fatal and the message will be stored into the dead
        // letter queue.
        return true;
    }

}
