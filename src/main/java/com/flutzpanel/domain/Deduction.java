package com.flutzpanel.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Deduction {

    @Enumerated(EnumType.STRING)
    private DeductionType type;

    private int value;

    public Deduction() {
        this.value = 0;
    }

    public DeductionType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public void setType(DeductionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Deduction type cannot be null.");
        }

        this.type = type;
    }

    public void setValue(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Deduction value must be greater than 0.");
        }

        this.value = value;
    }
}