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

import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import nl.vsmeets.amr.backend.database.ConstraintViolationException;
import nl.vsmeets.amr.backend.database.Site;
import nl.vsmeets.amr.backend.database.entities.SiteEntity;

/**
 * Unit tests for the class {@link SiteFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class SiteFactoryBeanTest {

    /**
     * Values used during tests.
     */
    private static final String name = "Name";
    private static final ZoneId timeZone = ZoneId.systemDefault();

    /**
     * Constructor parameters.
     */
    @Mock
    private SiteRepository repository;

    /**
     * The object under test.
     */
    @InjectMocks
    private SiteFactoryBean testObject;

    @Test
    void testCreate() {
        Mockito.when(repository.save(any(SiteEntity.class))).then(i -> i.getArgument(0));

        final Site result = assertDoesNotThrow(() -> testObject.create(name, timeZone));
        verify(repository).refresh(any(SiteEntity.class));
        assertAll( //
                () -> assertNull(result.getId()), //
                () -> assertEquals(name, result.getName()), //
                () -> assertEquals(timeZone, result.getTimeZone()) //
        );
    }

    @Test
    void testCreateDataIntegrityViolationException() {
        Mockito.when(repository.save(any(SiteEntity.class))).thenThrow(new DataIntegrityViolationException(null));

        assertThrows(ConstraintViolationException.class, () -> testObject.create(name, timeZone));
    }

    @Test
    void testFind(@Mock final Site site) {
        final Optional<? extends Site> expectedResult = Optional.of(site);

        Mockito.when(repository.findByName(name)).then(i -> expectedResult);

        assertEquals(expectedResult, testObject.find(name));
    }

}
