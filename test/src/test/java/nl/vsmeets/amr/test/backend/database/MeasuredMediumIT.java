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
package nl.vsmeets.amr.test.backend.database;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import nl.vsmeets.amr.backend.database.BackendDatabaseConfig;
import nl.vsmeets.amr.backend.database.MeasuredMedium;
import nl.vsmeets.amr.backend.database.MeasuredMediumFactory;

/**
 * Integration tests for {@link MeasuredMedium}.
 *
 * @author vincent
 */
@ContextConfiguration(classes = { BackendDatabaseConfig.class })
@DataJpaTest(
 // @formatter:off
     properties = {
         "logging.level.org.springframework.test.context.transaction.TransactionContext=WARN" },
     showSql = false
 //@formatter:on
 )
class MeasuredMediumIT {

  private final Byte mediumId1 = 0x02;
  private final Byte mediumId2 = 0x03;

  @Autowired
  private MeasuredMediumFactory measuredMediumFactory;
  private MeasuredMedium measuredMedium;

  @BeforeEach
  void setup() {
    final Optional<? extends MeasuredMedium> optionalMeasuredMedium =
        assertDoesNotThrow(() -> measuredMediumFactory.find(mediumId1));
    assertNotNull(optionalMeasuredMedium);
    assertTrue(optionalMeasuredMedium.isPresent());
    measuredMedium = optionalMeasuredMedium.get();
    assertNotNull(measuredMedium);
  }

  @Test
  void testFind() {
    final Optional<? extends MeasuredMedium> foundMeasuredMedium = measuredMediumFactory.find(mediumId1);
    assertAll( //
        () -> assertNotNull(foundMeasuredMedium), //
        () -> assertTrue(foundMeasuredMedium.isPresent()), //
        () -> assertEquals(measuredMedium, foundMeasuredMedium.get()) //
    );
  }

  @Test
  void testIdGenerated() {
    final Integer id1 = measuredMedium.getId();
    assertNotNull(id1);

    final Optional<? extends MeasuredMedium> optionalMeasuredMedium2 =
        assertDoesNotThrow(() -> measuredMediumFactory.find(mediumId2));
    final MeasuredMedium measuredMedium2 = optionalMeasuredMedium2.get();
    assertNotNull(measuredMedium2);
    final Integer id2 = measuredMedium2.getId();
    assertNotNull(id2);

    assertNotEquals(id1, id2);
  }

  @Test
  void testMediumId() {
    assertEquals(mediumId1, measuredMedium.getMediumId());
  }

  @Test
  void testName() {
    final String name = measuredMedium.getName();
    assertAll( //
        () -> assertNotNull(name), //
        () -> assertFalse(name.isEmpty()) //
    );
  }

}
