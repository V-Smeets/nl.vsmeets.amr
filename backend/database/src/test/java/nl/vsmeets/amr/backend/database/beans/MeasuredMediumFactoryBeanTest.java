package nl.vsmeets.amr.backend.database.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.vsmeets.amr.backend.database.AbstractTestBase;
import nl.vsmeets.amr.backend.database.MeasuredMedium;

/**
 * Unit tests for the class {@link MeasuredMediumFactoryBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class MeasuredMediumFactoryBeanTest extends AbstractTestBase {

  /**
   * The object under test.
   */
  private MeasuredMediumFactoryBean testObject;

  /**
   * Constructor parameters.
   */
  @Mock
  private MeasuredMediumRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    testObject = new MeasuredMediumFactoryBean(repository);
  }

  @Test
  void testFind(@Mock final MeasuredMedium measuredMedium) {
    final Byte mediumId = randomByte();
    final Optional<? extends MeasuredMedium> result = Optional.of(measuredMedium);

    when(repository.findByMediumId(mediumId)).then(i -> result);

    assertEquals(result, testObject.find(mediumId));
  }

}
