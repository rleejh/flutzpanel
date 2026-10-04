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

    @Test
    void cleanJumpReturnsBasicCode() {
        Jump jump = new Jump();

        jump.setRotations(3);
        jump.setJumpType(JumpType.LUTZ);

        assertEquals("3Lz", jump.getCode());
    }

    @Test
    void quarterJumpIncludesQuarterMarker() {
        Jump jump = new Jump();

        jump.setRotations(3);
        jump.setJumpType(JumpType.LUTZ);
        jump.setJumpRotation(JumpRotation.QUARTER);

        assertEquals("3Lzq", jump.getCode());
    }

    @Test
    void underrotatedJumpIncludesUnderrotationMarker() {
        Jump jump = new Jump();

        jump.setRotations(3);
        jump.setJumpType(JumpType.LUTZ);
        jump.setJumpRotation(JumpRotation.UNDERROTATED);

        assertEquals("3Lz<", jump.getCode());
    }

    @Test
    void downgradedJumpIncludesDowngradeMarker() {
        Jump jump = new Jump();

        jump.setRotations(3);
        jump.setJumpType(JumpType.LUTZ);
        jump.setJumpRotation(JumpRotation.DOWNGRADED);

        assertEquals("3Lz<<", jump.getCode());
    }

    @Test
    void unclearEdgeIncludesExclamationMark() {
        Jump jump = new Jump();

        jump.setRotations(3);
        jump.setJumpType(JumpType.LUTZ);
        jump.setJumpEdge(JumpEdge.UNCLEAR);

        assertEquals("3Lz!", jump.getCode());
    }

    @Test
    void wrongEdgeIncludesEdgeMarker() {
        Jump jump = new Jump();

        jump.setRotations(3);
        jump.setJumpType(JumpType.LUTZ);
        jump.setJumpEdge(JumpEdge.WRONG);

        assertEquals("3Lze", jump.getCode());
    }

    @Test
    void jumpWithoutTypeCannotReturnCode() {
        Jump jump = new Jump();

        assertThrows(IllegalStateException.class, jump::getCode);
    }
}
