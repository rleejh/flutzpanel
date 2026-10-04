package com.flutzpanel.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepSequenceTest {

    @Test
    void normalStepSequenceIsValid() {
        StepSequence step = new StepSequence();

        assertTrue(step.isValid());
    }

}
