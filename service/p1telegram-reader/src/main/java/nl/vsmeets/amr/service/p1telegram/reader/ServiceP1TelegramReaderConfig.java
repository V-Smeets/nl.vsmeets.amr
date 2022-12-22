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
package nl.vsmeets.amr.service.p1telegram.reader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.snksoft.crc.CRC;
import com.github.snksoft.crc.CRC.Parameters;

import nl.vsmeets.amr.backend.amqp.BackendAmqpConfig;

/**
 * The configuration class for service P1 telegram reader.
 *
 * @author vincent
 */
@Configuration
@ComponentScan
@Import({ BackendAmqpConfig.class })
public class ServiceP1TelegramReaderConfig {

    /**
     * A bean that calculates the CRC.
     *
     * @return The CRC bean.
     */
    @Bean
    public CRC crc() {
        return new CRC(Parameters.CRC16);
    }

}
