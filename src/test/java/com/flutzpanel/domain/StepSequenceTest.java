package com.flutzpanel.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepSequenceTest {

    @Test
    void normalStepSequenceIsValid() {
        StepSequence step = new StepSequence();

        assertTrue(step.isValid());
    }

    @Test
    void basicStepSequenceReturnsCorrectCode() {
        StepSequence sequence = new StepSequence();

        assertEquals("StSqB", sequence.getCode());
    }

    @Test
    void levelFourStepSequenceReturnsCorrectCode() {
        StepSequence sequence = new StepSequence();
        sequence.setLevel(ElementLevel.FOUR);

        assertEquals("StSq4", sequence.getCode());
    }

    @Test
    void levelOneStepSequenceReturnsCorrectCode() {
        StepSequence sequence = new StepSequence();
        sequence.setLevel(ElementLevel.ONE);

        assertEquals("StSq1", sequence.getCode());
    }

}
