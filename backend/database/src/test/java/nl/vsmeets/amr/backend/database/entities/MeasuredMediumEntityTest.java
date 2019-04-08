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
  private final Byte mediumId2 = randomByte();
  private final String name1 = randomString();
  private final String name2 = randomString();

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

    // @formatter:off
    assertAll(
        () -> assertEquals(mediumId1, measuredMediumEntity.getMediumId()),
        () -> assertEquals(name1, measuredMediumEntity.getName()),
        () -> assertNull(measuredMediumEntity.getMeters()));
    // @formatter:on
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

    // @formatter:off
    assertAll(
        () -> assertNotNull(measuredMediumEntity.toString()),
        () -> assertTrue(measuredMediumEntity.toString().contains(measuredMediumEntity.getClass().getSimpleName())),
        () -> assertTrue(measuredMediumEntity.toString().contains(mediumId1.toString())),
        () -> assertTrue(measuredMediumEntity.toString().contains(name1)));
    // @formatter:on
  }

}
