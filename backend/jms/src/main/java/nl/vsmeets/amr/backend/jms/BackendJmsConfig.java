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
package nl.vsmeets.amr.backend.jms;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;

import nl.vsmeets.amr.backend.jms.beans.BackendJmsProperties;
import nl.vsmeets.amr.backend.jms.beans.P1TelegramSenderBean;

/**
 * The configuration class for JMS backend.
 *
 * @author vincent
 */
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {})
@EnableJms
@Import({
    // Components in this module.
    BackendJmsProperties.class, //
    P1TelegramSenderBean.class
    // Other modules.
})
public class BackendJmsConfig {

}
