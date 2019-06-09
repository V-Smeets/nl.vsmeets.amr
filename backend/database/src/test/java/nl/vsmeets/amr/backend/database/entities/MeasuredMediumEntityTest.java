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
package nl.vsmeets.amr.backend.database.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.google.common.testing.EqualsTester;

import nl.vsmeets.amr.libs.junit.RandomByteGenerator;
import nl.vsmeets.amr.libs.junit.RandomStringGenerator;

/**
 * Unit tests for the class {@link MeasuredMediumEntity}.
 *
 * @author vincent
 */
class MeasuredMediumEntityTest implements RandomByteGenerator, RandomStringGenerator {

  private final Byte mediumId1 = randomByte();
  private final Byte mediumId2 = randomByte(mediumId1);
  private final String name1 = randomString();
  private final String name2 = randomString(name1);

  @Test
  void testEquals() {
    final EqualsTester equalsTester = new EqualsTester();
    equalsTester.addEqualityGroup(new MeasuredMediumEntity());
    Stream.of(mediumId1, mediumId2).forEach(mediumId -> //
    equalsTester.addEqualityGroup( //
        Stream.of(name1, name2).map(name -> //
        new MeasuredMediumEntity(mediumId, name)).toArray()));
    equalsTester.testEquals();
  }

  @Test
  void testGetters() {
    final MeasuredMediumEntity measuredMediumEntity = new MeasuredMediumEntity(mediumId1, name1);

    assertAll( //
        () -> assertEquals(mediumId1, measuredMediumEntity.getMediumId()), //
        () -> assertEquals(name1, measuredMediumEntity.getName()), //
        () -> assertNull(measuredMediumEntity.getMeters()) //
    );
  }

  @Test
  void testRequiredParameters() {
    assertThrows(NullPointerException.class, () -> new MeasuredMediumEntity(null, name1));
    assertThrows(NullPointerException.class, () -> new MeasuredMediumEntity(mediumId1, null));
  }

  @Test
  void testToString() {
    final MeasuredMediumEntity measuredMediumEntity = new MeasuredMediumEntity();

    assertNotNull(measuredMediumEntity.toString());
  }

  @Test
  void testToStringWithParameters() {
    final MeasuredMediumEntity measuredMediumEntity = new MeasuredMediumEntity(mediumId1, name1);

    assertAll( //
        () -> assertNotNull(measuredMediumEntity.toString()), //
        () -> assertTrue(measuredMediumEntity.toString().contains(measuredMediumEntity.getClass().getSimpleName())), //
        () -> assertTrue(measuredMediumEntity.toString().contains(mediumId1.toString())), //
        () -> assertTrue(measuredMediumEntity.toString().contains(name1)) //
    );
  }

}
