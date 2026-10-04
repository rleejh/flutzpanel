package com.flutzpanel.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChoreographicElementTest {

    @Test
    void newChoreographicElementIsInvalid() {
        ChoreographicElement element = new ChoreographicElement();

        assertFalse(element.isValid());
    }

    @Test
    void choreographicElementWithTypeIsValid() {
        ChoreographicElement element = new ChoreographicElement();

        element.setChoreoType(
                ChoreographicElementType.CHOREOGRAPHIC_SPIN
        );

        assertTrue(element.isValid());
    }

    @Test
    void choreographicElementCannotHaveNullType() {
        ChoreographicElement element = new ChoreographicElement();

        assertThrows(
                IllegalArgumentException.class,
                () -> element.setChoreoType(null)
        );
    }
}