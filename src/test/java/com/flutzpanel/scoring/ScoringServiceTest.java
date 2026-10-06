package com.flutzpanel.scoring;

import com.flutzpanel.domain.Jump;
import com.flutzpanel.domain.JumpPass;
import com.flutzpanel.domain.JumpType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ScoringServiceTest {

    private ScoringService scoringService;

    @BeforeEach
    void setUp() throws Exception {
        ScaleOfValues scaleOfValues = new ScaleOfValuesLoader().load();
        scoringService = new ScoringService(scaleOfValues);
    }

    @Test
    void scoresSimpleJumpCorrectly() {
        Jump jump = new Jump();
        jump.setRotations(3);
        jump.setJumpType(JumpType.LUTZ);

        JumpPass jumpPass = new JumpPass();
        jumpPass.addJump(jump);
        jumpPass.setGoe(BigDecimal.ONE);

        ElementScore result = scoringService.scoreElement(jumpPass);

        assertEquals(0, new BigDecimal("5.90").compareTo(result.getBaseValue()));
        assertEquals(new BigDecimal("0.59"), result.getGoeContribution());
        assertEquals(new BigDecimal("6.49"), result.getTotal());
    }

    @Test
    void scoresJumpCombinationUsingSumOfBaseValues() {
        Jump lutz = new Jump();
        lutz.setRotations(3);
        lutz.setJumpType(JumpType.LUTZ);

        Jump toe = new Jump();
        toe.setRotations(3);
        toe.setJumpType(JumpType.TOE);

        JumpPass jumpPass = new JumpPass();
        jumpPass.addJump(lutz);
        jumpPass.addJump(toe);
        jumpPass.setGoe(BigDecimal.ZERO);

        ElementScore result = scoringService.scoreElement(jumpPass);

        assertEquals(0, new BigDecimal("10.10").compareTo(result.getBaseValue()));
        assertEquals(new BigDecimal("0.00"), result.getGoeContribution());
        assertEquals(new BigDecimal("10.10"), result.getTotal());
    }

    @Test
    void combinationUsesHighestBaseValueJumpForGoe() {
        Jump lutz = new Jump();
        lutz.setRotations(3);
        lutz.setJumpType(JumpType.LUTZ);

        Jump toe = new Jump();
        toe.setRotations(3);
        toe.setJumpType(JumpType.TOE);

        JumpPass jumpPass = new JumpPass();
        jumpPass.addJump(lutz);
        jumpPass.addJump(toe);
        jumpPass.setGoe(BigDecimal.ONE);

        ElementScore result = scoringService.scoreElement(jumpPass);

        // 3Lz has the higher base value, so its +1 GOE value is used.
        assertEquals(new BigDecimal("0.59"), result.getGoeContribution());
        assertEquals(new BigDecimal("10.69"), result.getTotal());
    }

    @Test
    void bonusAppliesToEveryJumpInJumpPass() {
        Jump lutz = new Jump();
        lutz.setRotations(3);
        lutz.setJumpType(JumpType.LUTZ);

        Jump toe = new Jump();
        toe.setRotations(3);
        toe.setJumpType(JumpType.TOE);

        JumpPass jumpPass = new JumpPass();
        jumpPass.addJump(lutz);
        jumpPass.addJump(toe);
        jumpPass.setBonus(true);
        jumpPass.setGoe(BigDecimal.ZERO);

        ElementScore result = scoringService.scoreElement(jumpPass);

        // (5.90 + 4.20) × 1.10 = 11.11
        assertEquals(new BigDecimal("11.11"), result.getBaseValue());
        assertEquals(new BigDecimal("0.00"), result.getGoeContribution());
        assertEquals(new BigDecimal("11.11"), result.getTotal());
    }

    @Test
    void bonusDoesNotChangeGoeValue() {
        Jump lutz = new Jump();
        lutz.setRotations(3);
        lutz.setJumpType(JumpType.LUTZ);

        Jump toe = new Jump();
        toe.setRotations(3);
        toe.setJumpType(JumpType.TOE);

        JumpPass jumpPass = new JumpPass();
        jumpPass.addJump(lutz);
        jumpPass.addJump(toe);
        jumpPass.setBonus(true);
        jumpPass.setGoe(BigDecimal.ONE);

        ElementScore result = scoringService.scoreElement(jumpPass);

        // GOE still uses the 3Lz +1 value of 0.59.
        assertEquals(new BigDecimal("0.59"), result.getGoeContribution());
        assertEquals(new BigDecimal("11.70"), result.getTotal());
    }

    @Test
    void calculatesPositiveDecimalGoe() {
        Jump jump = new Jump();
        jump.setRotations(3);
        jump.setJumpType(JumpType.LUTZ);

        JumpPass jumpPass = new JumpPass();
        jumpPass.addJump(jump);
        jumpPass.setGoe(new BigDecimal("1.5"));

        ElementScore result = scoringService.scoreElement(jumpPass);

        // 0.59 × 1.5 = 0.885 → 0.89
        assertEquals(new BigDecimal("0.89"), result.getGoeContribution());
        assertEquals(new BigDecimal("6.79"), result.getTotal());
    }

    @Test
    void calculatesNegativeDecimalGoe() {
        Jump jump = new Jump();
        jump.setRotations(3);
        jump.setJumpType(JumpType.LUTZ);

        JumpPass jumpPass = new JumpPass();
        jumpPass.addJump(jump);
        jumpPass.setGoe(new BigDecimal("-1.5"));

        ElementScore result = scoringService.scoreElement(jumpPass);

        // -0.59 × 1.5 = -0.885 → -0.89
        assertEquals(new BigDecimal("-0.89"), result.getGoeContribution());
        assertEquals(new BigDecimal("5.01"), result.getTotal());
    }

    @Test
    void emptyJumpPassCannotBeScored() {
        JumpPass jumpPass = new JumpPass();

        assertThrows(
                IllegalArgumentException.class,
                () -> scoringService.scoreElement(jumpPass)
        );
    }
}
