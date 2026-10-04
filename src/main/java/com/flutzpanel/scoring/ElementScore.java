package com.flutzpanel.scoring;

import java.math.BigDecimal;

public class ElementScore {

    private BigDecimal baseValue;
    private BigDecimal goeContribution;
    private BigDecimal total;

    public BigDecimal getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(BigDecimal baseValue) {
        this.baseValue = baseValue;
    }

    public BigDecimal getGoeContribution() {
        return goeContribution;
    }

    public void setGoeContribution(BigDecimal goeContribution) {
        this.goeContribution = goeContribution;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}