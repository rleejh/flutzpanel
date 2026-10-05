package com.flutzpanel.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JumpPassTest {

    @Test
    void addJumpAddsJumpToJumpPass() {
        JumpPass jumpPass = new JumpPass();
        Jump jump = new Jump();

        jumpPass.addJump(jump);

        assertEquals(1, jumpPass.getJumps().size());
        assertEquals(jump, jumpPass.getJumps().get(0));
    }

    @Test
    void jumpPassCanContainMultipleJumps() {
        JumpPass jumpPass = new JumpPass();
        Jump firstJump = new Jump();
        Jump secondJump = new Jump();

        jumpPass.addJump(firstJump);
        jumpPass.addJump(secondJump);

        assertEquals(2, jumpPass.getJumps().size());
        assertEquals(firstJump, jumpPass.getJumps().get(0));
        assertEquals(secondJump, jumpPass.getJumps().get(1));
    }

    @Test
    void newJumpPassHasNoJumps() {
        JumpPass jumpPass = new JumpPass();

        assertTrue(jumpPass.getJumps().isEmpty());
    }

    @Test
    void emptyJumpPassIsInvalid() {
        JumpPass jumpPass = new JumpPass();

        assertFalse(jumpPass.isValid());
    }

    @Test
    void jumpPassWithAtLeastOneJumpIsValid() {
        JumpPass jumpPass = new JumpPass();
        Jump jump = new Jump();

        jumpPass.addJump(jump);

        assertTrue(jumpPass.isValid());
    }

    @Test
    void nullJumpIsRejected() {
        JumpPass jumpPass = new JumpPass();

        assertThrows(
                IllegalArgumentException.class,
                () -> jumpPass.addJump(null)
        );
    }

    @Test
    void jumpPassWithOneJumpReturnsJumpCode() {
        Jump jump = new Jump();
        jump.setRotations(3);
        jump.setJumpType(JumpType.LUTZ);

        JumpPass pass = new JumpPass();
        pass.addJump(jump);

        assertEquals("3Lz", pass.getCode());
    }

    @Test
    void jumpPassWithMultipleJumpsJoinsCodesWithPlus() {
        Jump first = new Jump();
        first.setRotations(3);
        first.setJumpType(JumpType.LUTZ);

        Jump second = new Jump();
        second.setRotations(2);
        second.setJumpType(JumpType.TOE);

        JumpPass pass = new JumpPass();
        pass.addJump(first);
        pass.addJump(second);

        assertEquals("3Lz+2T", pass.getCode());
    }

    @Test
    void emptyJumpPassCannotReturnCode() {
        JumpPass pass = new JumpPass();

        assertThrows(
                IllegalStateException.class,
                pass::getCode
        );
    }



}