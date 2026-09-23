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
