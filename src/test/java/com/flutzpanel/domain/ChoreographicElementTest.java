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

    @Test
    void choreographicSequenceReturnsCorrectCode() {
        ChoreographicElement element = new ChoreographicElement();

        element.setChoreoType(
                ChoreographicElementType.CHOREOGRAPHIC_SEQUENCE
        );

        assertEquals("ChSq1", element.getCode());
    }

    @Test
    void choreographicSpinReturnsCorrectCode() {
        ChoreographicElement element = new ChoreographicElement();

        element.setChoreoType(
                ChoreographicElementType.CHOREOGRAPHIC_SPIN
        );

        assertEquals("ChSq1", element.getCode());
    }

    @Test
    void choreographicElementWithoutTypeCannotReturnCode() {
        ChoreographicElement element = new ChoreographicElement();

        assertThrows(
                IllegalStateException.class,
                element::getCode
        );
    }
}