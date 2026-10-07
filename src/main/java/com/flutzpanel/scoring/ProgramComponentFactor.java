package com.flutzpanel.scoring;

import com.flutzpanel.domain.Discipline;
import com.flutzpanel.domain.ProgramType;

import java.math.BigDecimal;

public class ProgramComponentFactor {

    private String season;
    private Discipline discipline;
    private ProgramType programType;
    private BigDecimal factor;

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public ProgramType getProgramType() {
        return programType;
    }

    public void setProgramType(ProgramType programType) {
        this.programType = programType;
    }

    public BigDecimal getFactor() {
        return factor;
    }

    public void setFactor(BigDecimal factor) {
        this.factor = factor;
    }
}