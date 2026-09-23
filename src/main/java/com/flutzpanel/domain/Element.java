package com.flutzpanel.domain;
import java.math.BigDecimal;

public abstract class Element {

    private BigDecimal goe;

    // Constructor
    protected Element() {
        this.goe = BigDecimal.ZERO;
    }

    // Calculate Score // Implemntation To Be Done Later
    //public BigDecimal calculateElementScore() {}

    // Setting Method for GOE
    public void setGoe(BigDecimal goe) {
        if (goe == null) {
            throw new IllegalArgumentException("GOE cannot be null");
        }

        if (goe.compareTo(new BigDecimal("5")) > 0 || goe.compareTo(new BigDecimal("-5")) < 0 ) {
            throw new IllegalArgumentException("GOE Value must be between -5 and 5");
        }

        this.goe = goe;
    }

    // Getter Method for GOE
    public BigDecimal getGoe() {
        return this.goe;
    }

    // Abstract Getter Method for Element Name (Implementation in Sub-Class)
    public abstract String getName();

    // Abstract Getter Method for Element Code (Implementation in Sub-Class)
    public abstract String getCode();


}
