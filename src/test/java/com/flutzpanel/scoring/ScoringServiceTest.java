package com.flutzpanel.scoring;

import com.flutzpanel.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ScoringServiceTest {

    private ScoringService scoringService;

    @BeforeEach
    void setUp() throws Exception {
        ScaleOfValues scaleOfValues = new ScaleOfValuesLoader().load();
        List<ProgramComponentFactor> programComponentFactors =
                new ProgramComponentFactorLoader().load();

        scoringService = new ScoringService(scaleOfValues, programComponentFactors);
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

    @Test
    void findsMensShortProgramComponentFactor() {
        Program program = new Program();
        program.setSeason("2026-27");
        program.setDiscipline(Discipline.MEN);
        program.setProgramType(ProgramType.SHORT_PROGRAM);

        BigDecimal factor = scoringService.findComponentFactor(program);

        assertEquals(0, factor.compareTo(new BigDecimal("1.67")));
    }

    @Test
    void calculatesComponentScore() {
        Program program = new Program();
        program.setSeason("2026-27");
        program.setDiscipline(Discipline.MEN);
        program.setProgramType(ProgramType.SHORT_PROGRAM);

        ProgramComponent composition = new ProgramComponent();
        composition.setType(ProgramComponentType.COMPOSITION);
        composition.setScore(new BigDecimal("8.50"));

        ProgramComponent presentation = new ProgramComponent();
        presentation.setType(ProgramComponentType.PRESENTATION);
        presentation.setScore(new BigDecimal("8.75"));

        ProgramComponent skatingSkills = new ProgramComponent();
        skatingSkills.setType(ProgramComponentType.SKATING_SKILLS);
        skatingSkills.setScore(new BigDecimal("9.00"));

        program.addProgramComponent(composition);
        program.addProgramComponent(presentation);
        program.addProgramComponent(skatingSkills);

        BigDecimal componentScore = scoringService.calculateComponentScore(program);

        assertEquals(0, componentScore.compareTo(new BigDecimal("43.84")));
    }

    @Test
    void roundsEachComponentScoreBeforeAdding() {
        Program program = new Program();
        program.setSeason("2026-27");
        program.setDiscipline(Discipline.MEN);
        program.setProgramType(ProgramType.SHORT_PROGRAM);

        ProgramComponent composition = new ProgramComponent();
        composition.setType(ProgramComponentType.COMPOSITION);
        composition.setScore(new BigDecimal("8.53"));

        ProgramComponent presentation = new ProgramComponent();
        presentation.setType(ProgramComponentType.PRESENTATION);
        presentation.setScore(new BigDecimal("8.53"));

        ProgramComponent skatingSkills = new ProgramComponent();
        skatingSkills.setType(ProgramComponentType.SKATING_SKILLS);
        skatingSkills.setScore(new BigDecimal("8.53"));

        program.addProgramComponent(composition);
        program.addProgramComponent(presentation);
        program.addProgramComponent(skatingSkills);

        BigDecimal componentScore = scoringService.calculateComponentScore(program);

        assertEquals(0, componentScore.compareTo(new BigDecimal("42.75")));;
    }


    @Test
    void calculatesDeductionsTotal() {
        Program program = new Program();

        Deduction fall = new Deduction();
        fall.setType(DeductionType.FALL);
        fall.setValue(1);

        Deduction timeViolation = new Deduction();
        timeViolation.setType(DeductionType.TIME_VIOLATION);
        timeViolation.setValue(2);

        program.addDeduction(fall);
        program.addDeduction(timeViolation);

        BigDecimal deductionsTotal = scoringService.calculateDeductionsTotal(program);

        assertEquals(0, deductionsTotal.compareTo(new BigDecimal("3")));
    }

    @Test
    void calculatesTotalProgramScore() {
        Program program = new Program();
        program.setSeason("2026-27");
        program.setDiscipline(Discipline.MEN);
        program.setProgramType(ProgramType.SHORT_PROGRAM);

        // 2A = 3.30 base value, GOE 0
        Jump jump = new Jump();
        jump.setRotations(2);
        jump.setJumpType(JumpType.AXEL);

        JumpPass jumpPass = new JumpPass();
        jumpPass.addJump(jump);

        program.addElement(jumpPass);

        // PCS = 8.50 + 8.75 + 9.00 = 26.25
        // 26.25 × 1.67 = 43.8375
        // Each component is factored and rounded:
        // 14.20 + 14.61 + 15.03 = 43.84
        ProgramComponent composition = new ProgramComponent();
        composition.setType(ProgramComponentType.COMPOSITION);
        composition.setScore(new BigDecimal("8.50"));

        ProgramComponent presentation = new ProgramComponent();
        presentation.setType(ProgramComponentType.PRESENTATION);
        presentation.setScore(new BigDecimal("8.75"));

        ProgramComponent skatingSkills = new ProgramComponent();
        skatingSkills.setType(ProgramComponentType.SKATING_SKILLS);
        skatingSkills.setScore(new BigDecimal("9.00"));

        program.addProgramComponent(composition);
        program.addProgramComponent(presentation);
        program.addProgramComponent(skatingSkills);

        Deduction deduction = new Deduction();
        deduction.setType(DeductionType.FALL);
        deduction.setValue(1);

        program.addDeduction(deduction);

        ProgramScore programScore = scoringService.calculateTotalProgramScore(program);

        assertEquals(0, programScore.getTechnicalScore()
                .compareTo(new BigDecimal("3.30")));

        assertEquals(0, programScore.getComponentScore()
                .compareTo(new BigDecimal("43.84")));

        assertEquals(0, programScore.getDeductionsTotal()
                .compareTo(new BigDecimal("1")));

        assertEquals(0, programScore.getTotalProgramScore()
                .compareTo(new BigDecimal("46.14")));
    }

}
