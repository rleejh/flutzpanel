package com.flutzpanel.domain;

import java.math.BigDecimal;

public class ProgramComponent {

    private ProgramComponentType type;
    private BigDecimal score;

    public ProgramComponent() {
        this.score = BigDecimal.ZERO;
    }

    public ProgramComponentType getType() {
        return type;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setType(ProgramComponentType type) {
        if (type == null) {
            throw new IllegalArgumentException("Program component type cannot be null.");
        }

        this.type = type;
    }

    public void setScore(BigDecimal score) {
        if (score == null) {
            throw new IllegalArgumentException("Program component score cannot be null.");
        }

        if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(BigDecimal.TEN) > 0) {
            throw new IllegalArgumentException("Program component score must be between 0 and 10.");
        }

        this.score = score;
    }
}