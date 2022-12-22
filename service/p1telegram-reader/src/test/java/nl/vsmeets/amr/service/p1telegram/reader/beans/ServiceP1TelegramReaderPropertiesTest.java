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
package nl.vsmeets.amr.service.p1telegram.reader.beans;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link ServiceP1TelegramReaderProperties}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ServiceP1TelegramReaderPropertiesTest {

    /**
     * Values used during tests.
     */
    private static final String site = "Site";

    @InjectMocks
    private ServiceP1TelegramReaderProperties serviceP1TelegramReaderProperties;

    @BeforeEach
    void setUp() throws Exception {
        serviceP1TelegramReaderProperties.setSite(site);
    }

    @Test
    void testGetSite() {
        assertEquals(site, serviceP1TelegramReaderProperties.getSite());
    }

    @Test
    void testToString() {
        final String toString = serviceP1TelegramReaderProperties.toString();
        assertAll( //
                () -> assertNotNull(toString), //
                () -> assertTrue(toString.contains(serviceP1TelegramReaderProperties.getClass().getSimpleName())), //
                () -> assertTrue(toString.contains(site)) //
        );
    }

}
