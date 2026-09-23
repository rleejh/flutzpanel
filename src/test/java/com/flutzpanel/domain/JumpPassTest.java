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


}