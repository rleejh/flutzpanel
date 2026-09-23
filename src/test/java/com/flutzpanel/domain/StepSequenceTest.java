package com.flutzpanel.domain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepSequenceTest {

    @Test
    void normalStepSequenceIsValid() {
        StepSequence step = new StepSequence();

        assertTrue(step.isValid());
    }

    @Test
    void choreographicStepMustBeLevelOne() {
        StepSequence step = new StepSequence();
        step.setChoreoStep(true);
        step.setLevel(ElementLevel.TWO);

        assertFalse(step.isValid());
    }
}
