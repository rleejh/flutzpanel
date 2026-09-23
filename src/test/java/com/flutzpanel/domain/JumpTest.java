package com.flutzpanel.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JumpTest {
    @Test
    void negativeRotationsAreRejected() {
        Jump jump = new Jump();

        assertThrows(
                IllegalArgumentException.class,
                () -> jump.setRotations(-1)
        );
    }

    @Test
    void zeroRotationsAreAccepted() {
        Jump jump = new Jump();

        jump.setRotations(0);

        assertEquals(0, jump.getRotations());
    }

    @Test
    void oneRotationIsAccepted() {
        Jump jump = new Jump();

        jump.setRotations(1);

        assertEquals(1, jump.getRotations());
    }

    @Test
    void fiveRotationsAreAccepted() {
        Jump jump = new Jump();

        jump.setRotations(5);

        assertEquals(5, jump.getRotations());
    }

    @Test
    void rotationsAboveFiveAreRejected() {
        Jump jump = new Jump();

        assertThrows(
                IllegalArgumentException.class,
                () -> jump.setRotations(6)
        );
    }
}
