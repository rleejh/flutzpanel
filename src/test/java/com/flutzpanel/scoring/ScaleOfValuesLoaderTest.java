package com.flutzpanel.scoring;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ScaleOfValuesLoaderTest {

    @Test
    void loadsScaleOfValues() throws Exception {
        ScaleOfValuesLoader loader = new ScaleOfValuesLoader();

        ScaleOfValues scaleOfValues = loader.load();

        assertNotNull(scaleOfValues);
    }

    @Test
    void loadsAllScoringCategories() throws Exception {
        ScaleOfValuesLoader loader = new ScaleOfValuesLoader();

        ScaleOfValues scaleOfValues = loader.load();

        assertNotNull(scaleOfValues.getJumps());
        assertNotNull(scaleOfValues.getSpins());
        assertNotNull(scaleOfValues.getStepSequences());
        assertNotNull(scaleOfValues.getChoreographicElements());
    }

    @Test
    void loadsTripleLutzCorrectly() throws Exception {
        ScaleOfValuesLoader loader = new ScaleOfValuesLoader();

        ScaleOfValues scaleOfValues = loader.load();

        ScoringElement tripleLutz = scaleOfValues.getJumps().stream()
                .filter(element -> element.getId().equals("3Lz"))
                .findFirst()
                .orElseThrow();

        assertEquals(0, new BigDecimal("5.90").compareTo(tripleLutz.getBaseValue()));
        assertEquals(new BigDecimal("0.59"), tripleLutz.getGoe().get(1));
        assertEquals(new BigDecimal("-2.95"), tripleLutz.getGoe().get(-5));
    }
}