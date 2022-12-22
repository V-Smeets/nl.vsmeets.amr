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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.vsmeets.amr.backend.database.MeasuredMedium;

/**
 * Unit tests for the class {@link MeasuredMediumFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class MeasuredMediumFactoryBeanTest {

    /**
     * Values used during tests.
     */
    private static final Byte mediumId = Byte.MIN_VALUE;

    /**
     * Constructor parameters.
     */
    @Mock
    private MeasuredMediumRepository repository;

    /**
     * The object under test.
     */
    @InjectMocks
    private MeasuredMediumFactoryBean testObject;

    @Test
    void testFind(@Mock final MeasuredMedium measuredMedium) {
        final Optional<? extends MeasuredMedium> result = Optional.of(measuredMedium);

        Mockito.when(repository.findByMediumId(mediumId)).then(i -> result);

        assertEquals(result, testObject.find(mediumId));
    }

}
