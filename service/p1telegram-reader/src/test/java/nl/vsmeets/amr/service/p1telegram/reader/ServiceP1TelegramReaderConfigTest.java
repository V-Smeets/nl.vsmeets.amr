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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link ServiceP1TelegramReaderConfig}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ServiceP1TelegramReaderConfigTest {

    @InjectMocks
    private ServiceP1TelegramReaderConfig serviceP1TelegramReaderConfig;

    @Test
    void testConfigExists() {
        assertNotNull(serviceP1TelegramReaderConfig);
    }

    @Test
    void testCrc() {
        assertNotNull(serviceP1TelegramReaderConfig.crc());
    }

}
