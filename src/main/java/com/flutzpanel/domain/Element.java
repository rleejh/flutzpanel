package com.flutzpanel.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Element {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal goe;

    // Constructor
    protected Element() {
        this.goe = BigDecimal.ZERO;
    }

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


    public Long getId() {
        return this.id;
    }


}
