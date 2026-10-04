package com.flutzpanel.scoring;
import java.math.BigDecimal;
import java.util.Map;


public class ScoringElement {
    private String id;
    private BigDecimal baseValue;
    private Map<Integer, BigDecimal> goe;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(BigDecimal baseValue) {
        this.baseValue = baseValue;
    }

    public Map<Integer, BigDecimal> getGoe() {
        return goe;
    }

    public void setGoe(Map<Integer, BigDecimal> goe) {
        this.goe = goe;
    }
}
