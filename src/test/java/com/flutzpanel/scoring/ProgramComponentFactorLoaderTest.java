package com.flutzpanel.scoring;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProgramComponentFactorLoaderTest {

    @Test
    void loadsAllProgramComponentFactors() throws IOException {
        ProgramComponentFactorLoader loader = new ProgramComponentFactorLoader();

        List<ProgramComponentFactor> factors = loader.load();

        assertEquals(6, factors.size());
    }

    @Test
    void loadsMensShortProgramFactor() throws IOException {
        ProgramComponentFactorLoader loader = new ProgramComponentFactorLoader();

        List<ProgramComponentFactor> factors = loader.load();

        ProgramComponentFactor factor = factors.stream()
                .filter(f -> f.getDiscipline() == com.flutzpanel.domain.Discipline.MEN)
                .filter(f -> f.getProgramType() == com.flutzpanel.domain.ProgramType.SHORT_PROGRAM)
                .findFirst()
                .orElseThrow();

        assertEquals("2026-27", factor.getSeason());
        assertEquals(0, factor.getFactor().compareTo(new java.math.BigDecimal("1.67")));
    }
}