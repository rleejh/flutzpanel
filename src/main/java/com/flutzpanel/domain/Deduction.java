package com.flutzpanel.domain;

public class Deduction {

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