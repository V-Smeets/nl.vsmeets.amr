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
package nl.vsmeets.amr.backend.database.beans;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.P1Telegram;
import nl.vsmeets.amr.backend.database.entities.P1TelegramEntity;
import nl.vsmeets.amr.backend.database.entities.SiteEntity;

/**
 * Unit tests for the class {@link P1TelegramFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class P1TelegramFactoryBeanTest {

    /**
     * Values used during tests.
     */
    private static final String headerInformation = "Header Information";
    private static final Byte versionInformation = Byte.MIN_VALUE;

    /**
     * Constructor parameters.
     */
    @Mock
    private P1TelegramRepository repository;

    /**
     * The object under test.
     */
    @InjectMocks
    private P1TelegramFactoryBean testObject;

    @Test
    void testCreate(@Mock final SiteEntity site) {
        Mockito.when(repository.save(any(P1TelegramEntity.class))).then(i -> i.getArgument(0));

        final P1Telegram result = assertDoesNotThrow(
                () -> testObject.create(site, headerInformation, versionInformation));
        verify(repository).refresh(any(P1TelegramEntity.class));
        assertAll( //
                () -> assertNull(result.getId()), //
                () -> assertEquals(site, result.getSite()), //
                () -> assertEquals(headerInformation, result.getHeaderInformation()), //
                () -> assertEquals(versionInformation, result.getVersionInformation()) //
        );
    }

    @Test
    void testCreateDataIntegrityViolationException(@Mock final SiteEntity site) {
        Mockito.when(repository.save(any(P1TelegramEntity.class))).thenThrow(new DataIntegrityViolationException(null));

        assertThrows(ConstraintViolationException.class,
                () -> testObject.create(site, headerInformation, versionInformation));
    }

    @Test
    void testFind(@Mock final SiteEntity site, @Mock final P1Telegram p1Telegram) {
        final Optional<? extends P1Telegram> expectedResult = Optional.of(p1Telegram);

        Mockito.when(repository.findBySiteAndHeaderInformationAndVersionInformation(site, headerInformation,
                versionInformation)).then(i -> expectedResult);

        assertEquals(expectedResult, testObject.find(site, headerInformation, versionInformation));
    }

}
