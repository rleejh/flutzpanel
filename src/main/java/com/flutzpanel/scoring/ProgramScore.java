package com.flutzpanel.scoring;

import java.math.BigDecimal;

public class ProgramScore {

    private BigDecimal technicalScore;
    private BigDecimal componentScore;
    private BigDecimal deductionsTotal;
    private BigDecimal totalProgramScore;


    public BigDecimal getTechnicalScore() {
        return technicalScore;
    }

    public void setTechnicalScore(BigDecimal technicalScore) {
        this.technicalScore = technicalScore;
    }

    public BigDecimal getComponentScore() {
        return componentScore;
    }

    public void setComponentScore(BigDecimal componentScore) {
        this.componentScore = componentScore;
    }

    public BigDecimal getDeductionsTotal() {
        return deductionsTotal;
    }

    public void setDeductionsTotal(BigDecimal deductionsTotal) {
        this.deductionsTotal = deductionsTotal;
    }

    public BigDecimal getTotalProgramScore() {
        return totalProgramScore;
    }

    public void setTotalProgramScore(BigDecimal totalProgramScore) {
        this.totalProgramScore = totalProgramScore;
    }


}
